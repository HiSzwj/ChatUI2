package com.source.adnroid.comm.ui.chatmvp;

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
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.activity.ChatImageShowActivity;
import com.source.adnroid.comm.ui.adapter.ChatMessageAdapter;
import com.source.adnroid.comm.ui.chatmvp.base.IBasePresenter;
import com.source.adnroid.comm.ui.chatutils.KeybordUtils;
import com.source.adnroid.comm.ui.chatutils.PhotoUtils;
import com.source.adnroid.comm.ui.entity.ChatFileEntity;
import com.source.adnroid.comm.ui.entity.ChatUserGroupDetailsMessage;
import com.source.adnroid.comm.ui.entity.Const;
import com.source.adnroid.comm.ui.entity.MsgTypeEnum;
import com.source.adnroid.comm.ui.interfaces.OnItemClickListener;
import com.source.android.chatsocket.chatdb.ChatDBManager;
import com.source.android.chatsocket.entity.MsgEntity;
import com.source.android.chatsocket.entity.MsgViewEntity;
import com.source.android.chatsocket.messages.ChatUpLoadFileCallBackMessage;
import com.source.android.chatsocket.messages.MessageCallBack;
import com.source.android.chatsocket.messages.NetMessage;
import com.source.android.chatsocket.messages.NetReconnectMessage;
import com.source.android.chatsocket.utils.SPUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
//todo 进一步封装 提取成库
public class ChatFragment extends Fragment implements IChatConstract.IChatView, OnLoadMoreListener, OnRefreshListener, OnItemClickListener {
    private Map<String, ChatUserGroupDetailsMessage> userMap = new HashMap<String, ChatUserGroupDetailsMessage>();
    ChatMsgHandler handler = new ChatMsgHandler(this);
    public ChatPresenterImpl chatPresenter;//
    private String TAG = this.getClass().getSimpleName();
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
    private int begin = 0;
    private int limit = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDB();
        getBundule();
        initPresenter();
        initView();

    }

    //初始化数据库操作
    private void initDB() {
        chatDBManager = new ChatDBManager(getActivity());
    }

    private void initPresenter() {
        chatPresenter = new ChatPresenterImpl(roomId, token, userId, getActivity());
        chatPresenter.attachView(this);
        chatPresenter.start();
        chatPresenter.getUserMessage();//加载用户信息-》完成-》加载历史数据

    }

    private void getBundule() {
        Bundle bundle = getArguments();
        roomId = bundle.getString(Const.ROOM_ID);
        userId = bundle.getString(Const.USER_ID);
        token = bundle.getString(Const.TOKEN_KEY);
        Log.i(TAG, "roomId=>" + roomId + " userId=>" + userId + " token=>" + token);
    }

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
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        chatMessageAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        begin += limit;
        chatPresenter.getHistoryMessage(begin, limit, 2);
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        Log.i(TAG, "onLoadMore");
        //设置上拉加载更多结束
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onClick(String type, String position) {
        //type判断消息类型
        if (type.equals(MsgTypeEnum.PATIENT_MSG.getType())) {//病例消息
            Bundle bundle = new Bundle();
            bundle.putString(Const.TELEMEDICINE_INFO_ID, position);
            Intent intent = new Intent(Const.PATIENT_URL);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (type.equals("imageClick")) {
            Bundle bundle = new Bundle();
            bundle.putString("Expert_ID", position);
            Intent intent = new Intent(Const.Expert_URL);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (type.equals("longClick")) {
            Log.i(TAG, "longClick====>" + position);
            showResendOrDlelteDialog(position);
        } else if (type.equals(MsgTypeEnum.IMAGE_MSG.getType())) {
            Intent intent = new Intent(getActivity(), ChatImageShowActivity.class);
            Log.i(TAG, "url====>" + position);
            intent.putExtra("url", position);
            startActivity(intent);
        }
    }

    @Override
    public void onLongClick(int position) {

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
                sendData("text", "");
            } else if (v.getId() == R.id.chat_net_reconnect) {
                Log.i(TAG, "chat_net_reconnect click");
                // reconnectClick();
            } else if (v.getId() == R.id.chat_image_bt) {
                showDialog();
            }
        }
    }

    //发送消息 只执行UI更新操作
    public void sendData(String type, String message) {
        switch (type) {
            case "text":
                if (!TextUtils.isEmpty(chatEdit.getText())) {
                    String myMsg = chatEdit.getText().toString();
                    MsgEntity msgEntity = MsgEntity.parse(userId, myMsg, roomId, MsgTypeEnum.TO_ROOM.getType(), MsgTypeEnum.TEXT_MSG.getType());
                    Log.i(TAG, "send msgEntity==>" + msgEntity.toString());
                    String textMessage = com.alibaba.fastjson.JSONObject.toJSONString(msgEntity);
                    chatDBManager.add(msgEntity.getId(), msgEntity.getTo(), userId, textMessage, MsgTypeEnum.TEXT_MSG.getType(), new Date().getTime(), new Date().getTime(), 0);
                    chatPresenter.sendTextMessage(msgEntity);
                    MsgViewEntity msgViewEntity = ConverToMsgViewEntity(msgEntity, "0");
                    list.add(msgViewEntity);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else {
                    Toast.makeText(getActivity(), "不能发送空消息", Toast.LENGTH_SHORT).show();
                }
                break;
            case "resendtext":
                MsgEntity resendtextEntity = MsgEntity.parse(userId, message, roomId, MsgTypeEnum.TO_ROOM.getType(), MsgTypeEnum.TEXT_MSG.getType());
                Log.i(TAG, "send msgEntity==>" + resendtextEntity.toString());
                String resendtextMessage = com.alibaba.fastjson.JSONObject.toJSONString(resendtextEntity);
                chatDBManager.add(resendtextEntity.getId(), resendtextEntity.getTo(), userId, resendtextMessage, MsgTypeEnum.TEXT_MSG.getType(), new Date().getTime(), new Date().getTime(), 0);
                chatPresenter.sendTextMessage(resendtextEntity);
                MsgViewEntity resendtextViewEntity = ConverToMsgViewEntity(resendtextEntity, "0");
                list.add(resendtextViewEntity);
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                break;
            case "picture":
                ChatFileEntity picFileMsg = ConverToChatFileEntity(message, message);
                String realPictureMsg = JSONObject.toJSONString(picFileMsg);
                MsgEntity picEntity = MsgEntity.parse(userId, realPictureMsg, roomId, MsgTypeEnum.TO_ROOM.getType(), MsgTypeEnum.PICTURE.getType());
                String picMessage = com.alibaba.fastjson.JSONObject.toJSONString(picEntity);
                chatDBManager.add(picEntity.getId(), picEntity.getTo(), userId, picMessage, MsgTypeEnum.PICTURE.getType(), new Date().getTime(), new Date().getTime(), 0);
                chatPresenter.sendTextMessage(picEntity);
                MsgViewEntity picViewEntity = ConverToMsgViewEntity(picEntity, "0");
                picViewEntity.getMessage().setType(MsgTypeEnum.IMAGE_MSG.getType());
                list.add(picViewEntity);
                Message picMsg = new Message();
                picMsg.what = 1;
                handler.sendMessage(picMsg);
                break;
            case "gallery":
                ChatFileEntity gallleryFileMsg = ConverToChatFileEntity(message, message);
                String realGalleryMsg = JSONObject.toJSONString(gallleryFileMsg);
                MsgEntity galleryEntity = MsgEntity.parse(userId, realGalleryMsg, roomId, MsgTypeEnum.TO_ROOM.getType(), MsgTypeEnum.GALLERY.getType());
                String galleryMessage = com.alibaba.fastjson.JSONObject.toJSONString(galleryEntity);
                chatDBManager.add(galleryEntity.getId(), galleryEntity.getTo(), userId, galleryMessage, MsgTypeEnum.GALLERY.getType(), new Date().getTime(), new Date().getTime(), 0);
                chatPresenter.sendTextMessage(galleryEntity);
                MsgViewEntity galleryViewEntity = ConverToMsgViewEntity(galleryEntity, "0");
                galleryEntity.getMessage().setType(MsgTypeEnum.IMAGE_MSG.getType());
                list.add(galleryViewEntity);
                Message galleryMessageMsg = new Message();
                galleryMessageMsg.what = 1;
                handler.sendMessage(galleryMessageMsg);
                break;
        }
    }

    //转化成标准消息展示实体
    private MsgViewEntity ConverToMsgViewEntity(MsgEntity msgEntity, String msgStatus) {
        MsgViewEntity msgViewEntity = new MsgViewEntity();
        msgViewEntity.setId(msgEntity.getId());
        msgViewEntity.setMsgStatus(msgStatus);
        msgViewEntity.setFrom(msgEntity.getFrom());
        msgViewEntity.setTo(msgEntity.getTo());
        msgViewEntity.setType(msgEntity.getType());
        msgViewEntity.setMessage(msgEntity.getMessage());
        if (userMap.containsKey(msgEntity.getFrom()) || userMap.containsKey(msgEntity.getTo())) {
            msgViewEntity.setUserName(userMap.get(msgEntity.getFrom()).getMemberName());
            msgViewEntity.setUserPhoto(userMap.get(msgEntity.getFrom()).getPhoto());
        }
        return msgViewEntity;
    }

    private void showRemoteMessage(MsgViewEntity msgViewEntity) {
        list.add(msgViewEntity);
        handler.sendEmptyMessage(0);
    }

    //todo Handler 优化
    //Handler
    static class ChatMsgHandler extends Handler {
        private final WeakReference<ChatFragment> mFragment;

        public ChatMsgHandler(ChatFragment mfragment) {
            mFragment = new WeakReference<ChatFragment>(mfragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:  //接受远端消息
                    if (mFragment == null) {
                        return;
                    }
                    mFragment.get().chatMessageAdapter.notifyDataSetChanged();
                    //  mFragment.chatMessageAdapter.notifyDataSetChanged();
                    if (mFragment.get().isShowLastPosition()) {
                        mFragment.get().scrollToLastMsg();
                    } else {
                        Log.d(mFragment.get().TAG, "一条未读新消息");
                        mFragment.get().msgNotRead.setText("有未读消息点击跳转");
                    }
                    break;
                case 1://发送普通消息
                    mFragment.get().chatMessageAdapter.notifyDataSetChanged();
                    mFragment.get().chatEdit.setText("");
                    mFragment.get().scrollToLastMsg();
                    break;
                case 2://历史数据加载完成
                    mFragment.get().chatMessageAdapter.notifyDataSetChanged();
                    if (msg.arg1 == 1) {
                        mFragment.get().scrollToLastMsg();
                        mFragment.get().chatPresenter.getMessageFromNativeDB();//开始加载本地数据
                    }

                    break;
                case 3://本地数据加载完成
                    mFragment.get().chatMessageAdapter.notifyDataSetChanged();
                    mFragment.get().scrollToLastMsg();
                    break;
                case 4://socket重连提示
                    mFragment.get().changeView(msg.arg1);
                    break;
                case 5://socket 用户注册提示
                    mFragment.get().changeNetReconnectStatus(msg.arg1);
                    break;
                case 6://消息回掉更新状态
                    MessageCallBack messageCallBack = (MessageCallBack) msg.obj;
                    mFragment.get().updateMsgStatue(messageCallBack.getMsg(), String.valueOf(messageCallBack.getStatus()));
                    break;
                case 7://用户信息加载完成
                    mFragment.get().chatPresenter.getHistoryMessage(mFragment.get().begin, mFragment.get().limit, 1);//开始加载历史消息
                    break;
            }
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
            componentShowButton.setEnabled(true);
        } else {
            chatNetStatue.setVisibility(View.VISIBLE);
            sendMsgButton.setEnabled(false);
            componentShowButton.setEnabled(false);
            changeNetReconnectStatus(1);
        }
    }

    //改变重连按钮状态
    public void changeNetReconnectStatus(int netReconnectStatue) {

        if (netReconnectStatue == 1) {
            if (chatNetReconnect.getVisibility() == chatNetReconnect.VISIBLE) {
                chatNetReconnect.setVisibility(View.GONE);
                sendMsgButton.setEnabled(true);
                componentShowButton.setEnabled(true);
            }
        } else if (netReconnectStatue == 0) {
            if (chatNetReconnect.getVisibility() == chatNetReconnect.GONE) {
                chatNetReconnect.setVisibility(View.VISIBLE);
                sendMsgButton.setEnabled(false);
                componentShowButton.setEnabled(false);
            }
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

    //权限回掉
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: " + grantResults.length);
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                Log.d(TAG, "onRequestPermissionsResult: " + grantResults[i] + " name==" + permissions[i]);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        //FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
                        FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/ChinaBSC/";
                        Log.i(TAG, "FILE_PATH==>" + FILE_PATH);
                        File tempFile = new File(FILE_PATH);
                        if (!tempFile.exists()) {
                            tempFile.mkdirs();
                        }
                        FILE_PATH = FILE_PATH + System.currentTimeMillis() + ".jpg";
                        Log.i(TAG, "FILE_PATH==>" + FILE_PATH);
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
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

                    ) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    Toast.makeText(getActivity(), "您已经拒绝过一次", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
            } else {//有权限直接调用系统相机拍照
                if (hasSdcard()) {
                    // FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
                    FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/ChinaBSC/";
                    Log.i(TAG, "FILE_PATH=====>" + FILE_PATH);
                    File tempFile = new File(FILE_PATH);
                    if (!tempFile.exists()) {
                        tempFile.mkdirs();
                    }
                    FILE_PATH = FILE_PATH + System.currentTimeMillis() + ".jpg";
                    Log.i(TAG, "FILE_PATH==>" + FILE_PATH);
                    fileUri = new File(FILE_PATH);
                    imageUri = Uri.fromFile(fileUri);
                    //通过FileProvider创建一个content类型的Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(getActivity(), "com.source.adnroid.comm.ui.fileprovider", fileUri);
                    }
                    PhotoUtils.takePicture(ChatFragment.this, imageUri, CODE_CAMERA_REQUEST);
                } else {
                    Toast.makeText(getActivity(), "设备没有SD卡！", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
                sendData(MsgTypeEnum.PICTURE.getType(), FILE_PATH);
                //String picid = sendImageClick(MsgTypeEnum.PICTURE.getType(), FILE_PATH);
                // sendFile(MsgTypeEnum.PICTURE.getType(), picid, FILE_PATH);
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
                    sendData(MsgTypeEnum.GALLERY.getType(), galleryPath);
                    // String galleryid = sendImageClick(MsgTypeEnum.GALLERY.getType(), galleryPath);
                    //sendFile(MsgTypeEnum.GALLERY.getType(), galleryid, galleryPath);
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

    //检查设备是否存在SDCard的工具方法
    public boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    //更新消息状态
    public void updateMsgStatue(MsgEntity msg, String status) {
        try {
            for (int i = list.size(); i > 0; i--) {
                if (list.get(i - 1).getId().equals(msg.getId())) {
                    Log.i(TAG, "upstatus==>" + i);
                    list.get(i - 1).setMsgStatus(status);
                    switch (msg.getMessage().getType()) {
                        case "image":
                            list.get(i - 1).getMessage().setMsg(msg.getMessage().getMsg());
                            break;
                    }
                    chatMessageAdapter.notifyItemChanged(i - 1);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "message update err==>" + e.getMessage());
        }
    }

    //滚动到聊天底部最后一条
    private void scrollToLastMsg() {
        recyclerView.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
    }

    //判断是否显示最后一条
    public boolean isShowLastPosition() {
        Log.d(TAG, "lastPosition=>" + linearLayoutManager.findLastVisibleItemPosition() + "ItemCount" + chatMessageAdapter.getItemCount());
        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
        if (lastPosition == chatMessageAdapter.getItemCount() - 2) {
            return true;
        }
        return false;
    }

    //转化未标准图片实体
    private ChatFileEntity ConverToChatFileEntity(String url, String thumUrl) {
        ChatFileEntity chatFileEntity = new ChatFileEntity();
        chatFileEntity.setThumbUrl(thumUrl);
        chatFileEntity.setUrl(url);
        return chatFileEntity;
    }


    //recycle移除指定数据
    public void removeItem(String id) {
        for (int i = list.size(); i > 0; i--) {
            if (id == list.get(i - 1).getId()) {
                chatMessageAdapter.notifyItemRemoved(i - 1);
                list.remove(i - 1);
                chatMessageAdapter.notifyDataSetChanged();

            }
        }
    }

    //重发or删除消息 dialog
    public void showResendOrDlelteDialog(final String position) {
        final String items[] = {"重发", "删除"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请选择");
        // builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.i(TAG, "selecet==>" + items[which] + which);
                if (which == 0) {
                    chatPresenter.findDBMessageByID(position);
                } else {
                    Log.i(TAG, "开始删除==>" + position);
                    chatPresenter.deleteDBMessageByID(position);
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

    // View接口实现
    @Override
    public void showText(String s) {
        Log.i(TAG, TAG + "==>" + s);
    }

    @Override
    public void showRemoteMessage(MsgEntity msgEntity) {
        MsgViewEntity msgViewEntity = ConverToMsgViewEntity(msgEntity, "1");
        if (msgEntity.getTo().equals(roomId)) {
 /*           if (msgEntity.getMessage().getType().equals(MsgTypeEnum.REMOVE_FROM_ROOM.getType())) {//移除
                JSONObject jsonObject = JSON.parseObject(msgViewEntity.getMessage().getMsg());
                String userId = jsonObject.getString(Const.USER_ID);
                String removeName = userMap.get(userId).getMemberName();
                Map<String, String> tempMap = new HashMap<String, String>();
                tempMap.put("removeName", removeName);
                msgViewEntity.setMap(tempMap);
                updateData(msgViewEntity);
                getGroupMembersMessage();
            } else if (msgEntity.getMessage().getType().equals(MsgTypeEnum.INVITE_JOIN_ROOM.getType())) {//添加
                chatPresenter.getUserMessage();
            }*/
            if (msgEntity.getMessage().getType().equals(MsgTypeEnum.INVITE_JOIN_ROOM.getType())) {//添加
                chatPresenter.getUserMessage();
            }
            showRemoteMessage(msgViewEntity);

        }
    }

    @Override
    public void messageCallBack(MessageCallBack messageCallBack) {
        Message message = new Message();
        message.what = 6;
        message.obj = messageCallBack;
        handler.sendMessage(message);
    }


    @Override
    public void loadLocalDBFinish(List<MsgViewEntity> locallist) {
        Log.i(TAG, "loadLocalDBFinish==>" + locallist.size());
        for (MsgViewEntity item : locallist) {
            Log.i(TAG, " locallist item type==>" + item.getMessage().getType());
            if (userMap.containsKey(item.getFrom()) || userMap.containsKey(item.getTo())) {
                item.setUserName(userMap.get(item.getFrom()).getMemberName());
                item.setUserPhoto(userMap.get(item.getFrom()).getPhoto());
            }
            list.add(ConverToMsgViewEntity(item, item.getMsgStatus()));
        }
        handler.sendEmptyMessage(3);
    }

    @Override
    public void loadHistoryFinish(List<MsgEntity> tlist, int type) {
        Log.i(TAG, "loadHistoryFinish==>" + list.size());
        for (MsgEntity item : tlist) {
            list.add(0, ConverToMsgViewEntity(item, "1"));
        }
        Message message = new Message();
        message.what = 2;
        message.obj = list;
        message.arg1 = type;
        handler.sendMessage(message);

    }

    @Override
    public void loadUserMessageFinish(Map<String, ChatUserGroupDetailsMessage> userMap) {
        this.userMap = userMap;
        handler.sendEmptyMessage(7);
    }

    @Override
    public void findMsgSuccess(MsgViewEntity msgEntity) {
        if (msgEntity != null) {
            if (msgEntity.getMessage().getType().equals(MsgTypeEnum.TEXT_MSG.getType())) {
                msgEntity.getMessage().setType(MsgTypeEnum.RESEND_TEXT_MSG.getType());
            } else if (msgEntity.getMessage().getType().equals(MsgTypeEnum.GALLERY.getType())
                    || msgEntity.getMessage().getType().equals(MsgTypeEnum.PICTURE.getType())) {
                ChatFileEntity chatFileEntity = JSONObject.parseObject(msgEntity.getMessage().getMsg(), ChatFileEntity.class);
                msgEntity.getMessage().setMsg(chatFileEntity.getUrl());
            }
            sendData(msgEntity.getMessage().getType(), msgEntity.getMessage().getMsg());
        }
    }

    @Override
    public void deleteMsgSuccess(String position) {
        if (!position.equals("failer")) {
            removeItem(position);
        }
    }

    @Override
    public void refereshNetReconnectStatus(NetReconnectMessage netReconnectMessage) {
        Message message = new Message();
        message.what = 5;
        message.arg1 = netReconnectMessage.getNetStatus();
        handler.sendMessage(message);
    }

    @Override
    public void refereshNetStatus(NetMessage netMessage) {
        Message message = new Message();
        message.what = 4;
        message.arg1 = netMessage.getNetStatus();
        handler.sendMessage(message);
    }

    @Override
    public void setPresenter(IBasePresenter presenter) {

    }

    //声明周期方法
    @Override
    public void onDestroy() {
        super.onDestroy();

        chatPresenter.detacheView();
    }

    //用于加载网络状态
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume netstatus==>" + SPUtils.get(getActivity(), Const.Socket_STATUS, 0).toString() + "register status==>" + SPUtils.get(getActivity(), Const.Socket_Register_STATUS, 0).toString());
        changeView((int) SPUtils.get(getActivity(), Const.Socket_STATUS, 0));
        changeNetReconnectStatus(((int) SPUtils.get(getActivity(), Const.Socket_Register_STATUS, 0)));
    }
}
