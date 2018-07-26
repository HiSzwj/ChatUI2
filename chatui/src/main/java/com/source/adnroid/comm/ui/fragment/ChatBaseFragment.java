/*
package com.source.adnroid.comm.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.adapter.ChatMessageAdapter;
import com.source.android.chatsocket.chatdb.ChatDBManager;
import com.source.adnroid.comm.ui.chatutils.KeybordUtils;
import com.source.adnroid.comm.ui.chatutils.PhotoUtils;
import com.source.adnroid.comm.ui.entity.Const;
import com.source.adnroid.comm.ui.entity.MsgTypeEnum;
import com.source.android.chatsocket.entity.MsgViewEntity;
import com.source.android.chatsocket.messages.ChatActivityStatusMessage;
import com.source.android.chatsocket.messages.ChatUpLoadFileMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;


import static android.app.Activity.RESULT_OK;

public abstract class ChatBaseFragment extends Fragment {
    private String TAG = "ChatBaseFragment";
    SwipeToLoadLayout swipeToLoadLayout;//刷新 加载 控件
    RecyclerView recyclerView;//聊天信息控件
    ChatMessageAdapter chatMessageAdapter;//聊天信息适配器
    EditText chatEdit;
    TextView componentShowButton;//最下方组件显示按钮
    LinearLayout componentContent;//最下方组件显示控件
    TextView chatImageButton;//图片按钮
    TextView sendMsgButton;//发送消息按钮
    LinearLayoutManager linearLayoutManager;
    TextView msgNotRead;//未读消息显示控件
    View view;
    ArrayList<MsgViewEntity> list = new ArrayList<MsgViewEntity>();//维护消息列表
    String roomId;
    String userId;//本地用户ID
    String token;
    TextView chatNetStatue;//net消息
    TextView chatNetReconnect;//重连提示
    ChatDBManager chatDBManager;//数据库操作句柄

    private Uri imageUri;
    private Uri cropImageUri;
    private File fileUri;
    private String FILE_PATH;
    //private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/test/crop_photo.jpg");
    //private File fileUri = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/photo.jpg");
    // private File fileCropUri = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/crop_photo.jpg");
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new ChatActivityStatusMessage(true));
        chatDBManager = new ChatDBManager(getActivity());
        getRoomID();
        initHistoryData();
        initView();
        initListener();
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().post(new ChatActivityStatusMessage(false));
        EventBus.getDefault().unregister(this);
        if (chatDBManager != null) {
            chatDBManager.closeDB();
        }
        super.onDestroy();
    }

    private void getRoomID() {
        Bundle bundle = getArguments();
        roomId = bundle.getString(Const.ROOM_ID);
        userId = bundle.getString(Const.USER_ID);
        token = bundle.getString(Const.TOKEN_KEY);
        Log.i(TAG, "roomId=>" + roomId + " userId=>" + userId + " token=>" + token);
    }

    public abstract void initHistoryData();//加载历史数据

    public abstract void initListener();//加载上拉 下拉监听器

    public abstract void onKeyListener();//加载上拉 下拉监听器

    public abstract void sendMsgClick();//发送普通消息

    public abstract String sendImageClick(String type,String url);//发送图片消息

    public abstract void updateImageClick(String id, String url, String status);//更新图片消息状态

    public abstract void reconnectClick();//重连按钮点击

    private void initView() {
        chatNetReconnect = view.findViewById(R.id.chat_net_reconnect);
        chatNetReconnect.setOnClickListener(new OnClickListener());
        chatNetStatue = view.findViewById(R.id.chat_net_statue);
        sendMsgButton = view.findViewById(R.id.chat_sendMsg);
        sendMsgButton.setOnClickListener(new OnClickListener());
        msgNotRead = view.findViewById(R.id.msg_not_read);
        msgNotRead.setOnClickListener(new OnClickListener());
        componentShowButton = view.findViewById(R.id.component_button_show);
        //componentShowButton.setVisibility(View.INVISIBLE);
        componentShowButton.setOnClickListener(new OnClickListener());
        chatImageButton = view.findViewById(R.id.chat_image_bt);
        chatImageButton.setOnClickListener(new OnClickListener());
        componentContent = view.findViewById(R.id.component_button_content);
        chatEdit = view.findViewById(R.id.chatEdit);
        //chatEdit.setOnKeyListener(new OnKeyListener());
        chatEdit.setOnClickListener(new OnClickListener());
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        recyclerView = view.findViewById(R.id.swipe_target);
        recyclerView.setOnTouchListener(new OnToucheListener());
        //为recyclerView设置布局管理器
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.devider_chat_item_shape));
        recyclerView.addItemDecoration(divider);
        chatMessageAdapter = new ChatMessageAdapter(getActivity(), list, userId);
        //为recyclerView设置适配器
        recyclerView.setAdapter(chatMessageAdapter);


    }

    public class OnToucheListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(TAG, "On RecycleView Touch");
                closeKeyBord();
            }
            return false;
        }
    }

    public class OnClickListener implements View.OnClickListener {
        @SuppressLint("WrongConstant")
        @Override
        public void onClick(View v) {
            Log.d(TAG, v.getId() + " clicked");
            if (v.getId() == R.id.component_button_show) {
                closeKeyBord();
                Log.d(TAG, "VISIBLE" + componentContent.getVisibility());
                updateBottomContent();

            } else if (v.getId() == R.id.msg_not_read) {
                recyclerView.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
                msgNotRead.setText("");
            } else if (v.getId() == R.id.chatEdit) {
                closeBottomContent();
            } else if (v.getId() == R.id.chat_sendMsg) {
                sendMsgClick();
            } else if (v.getId() == R.id.chat_net_reconnect) {
                Log.i(TAG, "chat_net_reconnect click");
                reconnectClick();
            } else if (v.getId() == R.id.chat_image_bt) {
                showDialog();
            }
        }
    }

    public class OnKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                Log.i(TAG, "KEYCODE_ENTER click");
                onKeyListener();
                return true;
            }
            return false;
        }
    }


    // 调整最下方组件 如果开则关 如果关则开
    public void updateBottomContent() {
        if (componentContent.getVisibility() == componentContent.VISIBLE) {
            componentContent.setVisibility(View.GONE);
        } else if (componentContent.getVisibility() == componentContent.GONE) {
            componentContent.setVisibility(View.VISIBLE);
        }
    }


    // 关闭最下方组件
    public void closeBottomContent() {
        if (componentContent.getVisibility() == componentContent.VISIBLE) {
            componentContent.setVisibility(View.GONE);
        }
    }


    //关闭软键盘
    public void closeKeyBord() {
        if (KeybordUtils.isSoftInputShow(getActivity())) {
            KeybordUtils.closeKeybord(chatEdit, getActivity());
        }
    }

    //改变socket状态
    public void changeView(int netStatus) {
        if (netStatus == 1) {
            chatNetStatue.setVisibility(View.GONE);
            sendMsgButton.setEnabled(true);
        } else {
            chatNetStatue.setVisibility(View.VISIBLE);
            sendMsgButton.setEnabled(false);
            changeNetReconnectStatus(1);
        }
    }

    //改变重连按钮状态
    public void changeNetReconnectStatus(int netReconnectStatue) {

        if (netReconnectStatue == 1) {
            if (chatNetReconnect.getVisibility() == chatNetReconnect.VISIBLE) {
                chatNetReconnect.setVisibility(View.GONE);
                sendMsgButton.setEnabled(true);
            }
        } else if (netReconnectStatue == 0) {
            if (chatNetReconnect.getVisibility() == chatNetReconnect.GONE) {
                chatNetReconnect.setVisibility(View.VISIBLE);
                sendMsgButton.setEnabled(false);
            }
        }
    }

    //发送文件
    public void sendFile(String type,String pcid,String path){
        EventBus.getDefault().post(new ChatUpLoadFileMessage(type, pcid,roomId,path, token, "failer"));
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    //调用相机or图库回掉
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: requestCode: " + requestCode + "  resultCode:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.e(TAG, "onActivityResult: resultCode!=RESULT_OK");
            return;
        }
        switch (requestCode) {
            //相机返回
            case CODE_CAMERA_REQUEST:
                //todo 临时路径同时缓存到内存和数据库
                String picid = sendImageClick(MsgTypeEnum.PICTURE.getType(),FILE_PATH);
                sendFile(MsgTypeEnum.PICTURE.getType(),picid,FILE_PATH);
                //File file = new File(FILE_PATH);
                // Uri uri = Uri.fromFile(file);
                // Log.i(TAG, "相册文件路径==>" + uri.getPath());
                //File picfile = new File(uri.getPath());
                //Log.i(TAG, "相册file==>" + picfile.length());
                // upLoadFiles(picid,file);
                //todo 裁剪后加入聊天列表
                //cropImageUri = Uri.fromFile(fileCropUri);
                // PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                break;
            //相册返回
            case CODE_GALLERY_REQUEST:
                if (hasSdcard()) {
                    //todo 临时路径同时缓存到内存和数据库
                    String galleryPath = PhotoUtils.getPath(getActivity(), data.getData());
                    String galleryid = sendImageClick(MsgTypeEnum.GALLERY.getType(),galleryPath);
                    sendFile(MsgTypeEnum.GALLERY.getType(),galleryid,galleryPath);
                   // EventBus.getDefault().post(new ChatUpLoadFileMessage("gallery", galleryid, galleryPath, token, "failer"));
                    //  Uri newUri = Uri.parse(galleryPath);
                    // Log.i(TAG, "newUri==>" + newUri.getPath());
                    // File galleryfile = new File(newUri.getPath());
                    //Log.i(TAG, "相册file==>" + galleryfile.length());
                    // upLoadFiles(galleryid, galleryfile);
                    //todo 裁剪后加入聊天列表
                    //cropImageUri = Uri.fromFile(fileCropUri);
                    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //   newUri = FileProvider.getUriForFile(getActivity(), "com.source.adnroid.comm.ui.fileprovider", file);
                    // }
                    // PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                } else {
                    Toast.makeText(getActivity(), "设备没有SD卡！", Toast.LENGTH_SHORT).show();
                }
                break;
            //裁剪返回
            case CODE_RESULT_REQUEST:
                Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, getActivity());
                if (bitmap != null) {
                    // showImages(bitmap);
                }
                break;
            default:
        }
    }

    //拍照相册dialog
    public void showDialog() {
        final String items[] = {"拍照", "相册"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("列表");
        // builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.i(TAG, "selecet==>" + items[which] + which);
                if (which == 0) {
                    autoObtainCameraPermission();
                } else {
                    autoObtainStoragePermission();
                }


            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.i(TAG, "selecet==>取消");
            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
                        Log.i(TAG,"FILE_PATH==>"+FILE_PATH);
                        fileUri = new File(FILE_PATH);
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //通过FileProvider创建一个content类型的Uri
                            imageUri = FileProvider.getUriForFile(getActivity(), "com.source.adnroid.comm.ui.fileprovider", fileUri);
                        }
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        Toast.makeText(getActivity(), "设备没有SD卡", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "请允许打开相机", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    Toast.makeText(getActivity(), "请允许打操作SDCard", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    //动态申请sdcard读写权限
    public void autoObtainStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
            }
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }
    }

    // 申请访问相机权限
    public void autoObtainCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    Toast.makeText(getActivity(), "您已经拒绝过一次", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
            } else {//有权限直接调用系统相机拍照
                if (hasSdcard()) {
                    FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
                    Log.i(TAG,"FILE_PATH==>"+FILE_PATH);
                    fileUri = new File(FILE_PATH);
                    imageUri = Uri.fromFile(fileUri);
                    //通过FileProvider创建一个content类型的Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(getActivity(), "com.source.adnroid.comm.ui.fileprovider", fileUri);
                    }
                    PhotoUtils.takePicture(ChatBaseFragment.this, imageUri, CODE_CAMERA_REQUEST);
                } else {
                    Toast.makeText(getActivity(), "设备没有SD卡！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    //检查设备是否存在SDCard的工具方法
    public boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

}


*/
