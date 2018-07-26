package com.source.adnroid.comm.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bsc.chat.commenbase.BaseConst;
import com.bumptech.glide.Glide;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.adapter.AnnListAdapter;
import com.source.adnroid.comm.ui.adapter.MemberListAdapter;
import com.source.adnroid.comm.ui.chatview.ToastUtil;
import com.source.adnroid.comm.ui.entity.ChatGroupAnn;
import com.source.adnroid.comm.ui.entity.ChatGroupAttributes;
import com.source.adnroid.comm.ui.entity.ChatGroupManager;
import com.source.adnroid.comm.ui.entity.ChatGroupMember;
import com.source.adnroid.comm.ui.entity.CommenResponse;
import com.source.adnroid.comm.ui.entity.Const;
import com.source.adnroid.comm.ui.interfaces.OnItemClickListener;
import com.source.adnroid.comm.ui.net.HttpReuqests;
import com.source.android.chatsocket.entity.SocketConst;
import com.source.android.chatsocket.messages.AddMemberEvent;
import com.source.android.chatsocket.messages.ChatGroupPeopleChangeCallBackMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatGroupDetailsActivity extends ChatBaseActivity implements OnItemClickListener {
    private String TAG = "ChatGroupDetailsActivity";
    private ImageView mBackImageView;
    private RelativeLayout mLoadingLayout;
    private Button deleteChatGroup;
    //标题栏 添加成员
    private ImageView mAddMemberImageView;
    //标题栏 添加公告
    private ImageView mAddAnnImageView;

    //属性 成员 公告
    private TextView mAttributesText;
    private TextView mMemberText;
    private TextView mAnnouncementText;

    //属性 成员 公告的选中条
    private View mAttributesSelected;
    private View mMemberSelected;
    private View mAnnouncementSelected;

    //属性页面
    private LinearLayout mAttributesLinearLayout;

    //属性页面控件
    private ImageView mUserHeadImageView;
    private TextView mUserNameTextView;
    private TextView mUserSiteName;
    private TextView mUserTitleTextView;
    private TextView mUserTelTextView;
    private TextView mSkillTextView;
    private TextView mUserWorkTextView;
    //群分类
    private TextView mGroupTypeTextView;
    //群介绍
    private TextView mGroupDescriptionTextView;

    //成员列表
    private ListView mMemberList;
    private MemberListAdapter mMemberListAdapter;

    //成员列表数据
    private List<ChatGroupMember> mMemberData;
    //管理员列表数据
    private List<ChatGroupManager> mManagerData;

    //公告列表
    private SwipeToLoadLayout mAnnSwipeToLoadLayout;
    private RecyclerView mAnnRecyclerView;
    private LinearLayoutManager mAnnLayoutManager;
    private boolean isLoading;
    private int mAnnTotal = 0;
    private int mAnnStart = 0;
    private AnnListAdapter mAnnListAdapter;
    private Handler mAnnHandler = new Handler();

    //公告数据
    public ArrayList<ChatGroupAnn> mAnnItem = new ArrayList<ChatGroupAnn>();

    //当前用户权限
    private int mMyRole = 1;

    //房间id
    private String mRoomId;
    //token
    private String mToken;
    MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_details);
        EventBus.getDefault().register(this);
        init();
        mRoomId = roomId;
        mToken = token;
        Log.i("test", "userId==>" + userId);
        Log.i("test", "roomId==>" + roomId);
        getAttData(token, mRoomId);
        //getManagerData(token, mRoomId);
    }

    private void init() {
        mBackImageView = findViewById(R.id.BackImageView);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mUserWorkTextView = findViewById(R.id.UserWorkTextView);
        mLoadingLayout = findViewById(R.id.LoadingLayout);

        mAddMemberImageView = findViewById(R.id.chat_add_member_image);
        mAddMemberImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatGroupDetailsActivity.this, ChatGroupAddMemberActivity.class);
                intent.putExtra(Const.TOKEN_KEY, token);
                intent.putExtra(Const.ROOM_ID, mRoomId);
                startActivity(intent);
            }
        });

        mAddAnnImageView = findViewById(R.id.chat_add_ann_image);
        mAddAnnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatGroupDetailsActivity.this, ChatGroupAddAnnActivity.class);
                intent.putExtra(Const.TOKEN_KEY, token);
                intent.putExtra(Const.ROOM_ID, mRoomId);
                intent.putExtra(Const.USER_ID, userId);
                startActivity(intent);
            }
        });

        mAttributesText = findViewById(R.id.group_attributes_text);
        mMemberText = findViewById(R.id.group_member_text);
        mAnnouncementText = findViewById(R.id.group_announcement_text);

        mAttributesSelected = findViewById(R.id.group_attributes_selected);
        mMemberSelected = findViewById(R.id.group_member_selected);
        mAnnouncementSelected = findViewById(R.id.group_announcement_selected);

        mAttributesLinearLayout = findViewById(R.id.group_attributes_layout);

        mMemberList = findViewById(R.id.group_member_list);
        mMemberData = new ArrayList<ChatGroupMember>();
        mMemberListAdapter = new MemberListAdapter(this, mMemberData);
        mMemberListAdapter.setOnItemClickListener(this);
        mMemberList.setAdapter(mMemberListAdapter);
        mMemberListAdapter.setOnItemClickListener(ChatGroupDetailsActivity.this);

        mAnnSwipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.group_announcement_Layout);
        mAnnRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);

        mAnnLayoutManager = new LinearLayoutManager(this);
        mAnnRecyclerView.setLayoutManager(mAnnLayoutManager);
        mAnnListAdapter = new AnnListAdapter(this, mAnnItem);
        mAnnRecyclerView.setAdapter(mAnnListAdapter);
        mAnnListAdapter.setOnItemClickListener(this);
        mAnnRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int Alliancetate) {
                super.onScrollStateChanged(recyclerView, Alliancetate);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = mAnnLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == mAnnListAdapter.getItemCount()) {
                    Log.i("test", "到底部mAnnTotal: " + mAnnTotal + " mAnnItem.size(): " + mAnnItem.size());

                    if (mAnnTotal > mAnnItem.size()) {
                        if (!isLoading) {
                            isLoading = true;
                            mAnnHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getAnnListData(mToken, mRoomId);
                                    Log.d("test", "load more completed");
                                    isLoading = false;
                                }
                            }, 300);
                        }
                    }
                }
            }
        });

        mUserHeadImageView = findViewById(R.id.UserHeadImageView);
        mUserNameTextView = findViewById(R.id.UserNameTextView);
        mUserSiteName = findViewById(R.id.UserSiteName);
        mUserTitleTextView = findViewById(R.id.UserTitleTextView);
        mUserTelTextView = findViewById(R.id.UserTelTextView);
       // mSkillTextView = findViewById(R.id.SkillTextView);
        mGroupTypeTextView = findViewById(R.id.GroupTypeTextView);
        mGroupDescriptionTextView = findViewById(R.id.GroupDescriptionTextView);
        deleteChatGroup = findViewById(R.id.deleteChatGroup);
        deleteChatGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
        setTabListener();
    }

    //设置Tab的监听
    private void setTabListener() {
        mAttributesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeSelected();
                mAttributesSelected.setVisibility(View.VISIBLE);

                initializeShowItem();
                mAttributesLinearLayout.setVisibility(View.VISIBLE);

                mAddMemberImageView.setVisibility(View.GONE);
                mAddAnnImageView.setVisibility(View.GONE);
            }
        });
        mMemberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeSelected();
                mMemberSelected.setVisibility(View.VISIBLE);

                initializeShowItem();
                mMemberList.setVisibility(View.VISIBLE);

                if (mMyRole == 0 || mMyRole == 2) {
                    mAddMemberImageView.setVisibility(View.VISIBLE);
                }
                mAddAnnImageView.setVisibility(View.GONE);
            }
        });
        mAnnouncementText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeSelected();
                mAnnouncementSelected.setVisibility(View.VISIBLE);

                initializeShowItem();
                mAnnSwipeToLoadLayout.setVisibility(View.VISIBLE);

                mAddMemberImageView.setVisibility(View.GONE);
                if (mMyRole == 0 || mMyRole == 2) {
                    mAddAnnImageView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //获取群管理员
    private void getManagerData(String token, String id) {
        HttpReuqests.getInstance().getGroupManager(token, id, new Callback<CommenResponse<List<ChatGroupManager>>>() {
            @Override
            public void onResponse(Call<CommenResponse<List<ChatGroupManager>>> call, Response<CommenResponse<List<ChatGroupManager>>> response) {
                CommenResponse<List<ChatGroupManager>> member = response.body();
                int resultCode = member.getResultCode();
                if (resultCode == 200) {
                    Log.i("test", "getManagerData 200");
                    mManagerData = member.getData();
                    for (int i = 0; i < mManagerData.size(); i++) {
                        String uid = mManagerData.get(i).getUserId();
                        //判断当前用户id是否在管理员列表中，并保存权限等级
                        if (userId.equals(uid)) {
                            mMyRole = mManagerData.get(i).getMemberRole();
                            mMemberListAdapter.setMyRole(mMyRole);
                            if (mMyRole == 0) {
                                deleteChatGroup.setVisibility(View.VISIBLE);
                            }

                        }
                    }

                    getMemberData(mToken, mRoomId);
                    getAnnListData(mToken, mRoomId);
                } else if (resultCode == 401) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<CommenResponse<List<ChatGroupManager>>> call, Throwable t) {

            }
        });
    }

    //获取群属性
    private void getAttData(String token, String id) {
        HttpReuqests.getInstance().getGroupAttributes(token, id, new Callback<ChatGroupAttributes>() {
            @Override
            public void onResponse(Call<ChatGroupAttributes> call, Response<ChatGroupAttributes> response) {
                ChatGroupAttributes att = response.body();
                Log.i(TAG, "ChatGroupAttributes ==>" + att.getData().getGroupType());
                int resultCode = att.getResultCode();
                if (resultCode == 200) {
                    String photo = att.getData().getPhoto();
                    Glide.with(ChatGroupDetailsActivity.this).load(BaseConst.CHAT_PIC_URL + photo).into(mUserHeadImageView);
                    String realName = att.getData().getRealName();
                    String jobTitle = att.getData().getJobtitle();
                    mUserNameTextView.setText(realName);
                    mUserWorkTextView.setText(" (" + jobTitle + ")");
                    String siteName = att.getData().getSiteName();
                    mUserSiteName.setText(siteName);
                    String departName = att.getData().getDepartName();
                    mUserTitleTextView.setText(departName);
                    String mobile = att.getData().getMobile();
                    mUserTelTextView.setText(mobile);
                    //String skill = att.getData().getSkill();
                   // mSkillTextView.setText(skill);
                    String groupType = att.getData().getGroupType();
                    mGroupTypeTextView.setText(groupType);
                    String description = att.getData().getDescription();
                    mGroupDescriptionTextView.setText(description);
                } else if (resultCode == 401) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<ChatGroupAttributes> call, Throwable t) {

            }
        });
    }

    //获取群成员
    private void getMemberData(String token, String id) {
        //mobile/snsgroupMember/getSnsMemberByGroupId
        mMemberData.clear();
        HttpReuqests.getInstance().getGroupMember(token, id, new Callback<CommenResponse<List<ChatGroupMember>>>() {
            @Override
            public void onResponse(Call<CommenResponse<List<ChatGroupMember>>> call, Response<CommenResponse<List<ChatGroupMember>>> response) {
                CommenResponse<List<ChatGroupMember>> member = response.body();
                int resultCode = member.getResultCode();
                if (resultCode == 200) {
                    Log.i("test", "getmember 200");
                    mMemberData.addAll(member.getData());
                    mMemberListAdapter.notifyDataSetChanged();

                } else if (resultCode == 401) {


                } else {

                }
            }

            @Override
            public void onFailure(Call<CommenResponse<List<ChatGroupMember>>> call, Throwable t) {

            }
        });
    }

    //通过socket移除
    private void delMemberBySocket(int position) {
        EventBus.getDefault().post(new AddMemberEvent(SocketConst.CHAT_GROUP_REMOVE_PEOPLE, roomId, mMemberData.get(Integer.valueOf(position)).getUserId()));
    }

    //人员变动提示
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(ChatGroupPeopleChangeCallBackMessage chatManager) {
        Log.d(TAG, "从socket中获取到的信息人员变动回掉==" + chatManager.getResult() + "chatManager userid==" + chatManager.getUserId() + "mMemberItem size" + mMemberData.size());
        if (!chatManager.getType().equals("removeUserFromRoom")) {
            return;
        }
        if (chatManager.getResult() == 1) {
            Log.d(TAG, "移除成功");
            Message message = new Message();
            message.what = 1;
            message.obj = chatManager;
            handler.sendMessage(message);
        } else if (chatManager.getResult() == 0) {
            Log.d(TAG, "移除失败");
            handler.sendEmptyMessage(0);
        }
    }

    static class MyHandler extends Handler {
        private final WeakReference<ChatGroupDetailsActivity> mActivity;

        public MyHandler(ChatGroupDetailsActivity mactivity) {
            mActivity = new WeakReference<ChatGroupDetailsActivity>(mactivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity == null) {
                return;
            }
            if (msg.what == 1) {
                Toast.makeText(mActivity.get(), "移除成功", Toast.LENGTH_SHORT).show();
                mActivity.get().removePeopleChangePosition((ChatGroupPeopleChangeCallBackMessage) msg.obj);
            } else if (msg.what == 0) {
                Toast.makeText(mActivity.get(), "移除失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void removePeopleChangePosition(ChatGroupPeopleChangeCallBackMessage chatManager) {
        for (int i = 0; i < mMemberData.size(); i++) {
            Log.i(TAG, "人员==>" + mMemberData.get(i).getUserId());
            if (chatManager.getUserId() == mMemberData.get(i).getUserId()) {
                Log.i(TAG, "人员移除==>" + i);
                mMemberData.remove(i);
                mMemberListAdapter.notifyDataSetChanged();
            }
        }
    }

    //移除群成员
    private void delMember(String token, String id, final int itemNum) {
        //mobile/snsgroupMember/delete
        HttpReuqests.getInstance().delMember(token, id, new Callback<CommenResponse>() {
            @Override
            public void onResponse(Call<CommenResponse> call, Response<CommenResponse> response) {
                CommenResponse c = response.body();
                int resultCode;
                if (c == null) {
                    resultCode = 400;
                }
                resultCode = c.getResultCode();
                if (resultCode == 200) {
                    mMemberData.remove(itemNum);
                    mMemberListAdapter.notifyDataSetChanged();
                } else if (resultCode == 401) {

                } else {

                }
                mLoadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommenResponse> call, Throwable t) {

            }
        });

    }

    //设置 移除 管理员
    private void setManager(String token, String id, String gid, final int itemNum) {
        //mobile/snsgroupMember/setGroupManager
        Log.i("test", token + " " + id + " " + gid);
        HttpReuqests.getInstance().setManager(token, id, gid, new Callback<CommenResponse>() {
            @Override
            public void onResponse(Call<CommenResponse> call, Response<CommenResponse> response) {
                CommenResponse c = response.body();
                int resultCode;
                if (c == null) {
                    resultCode = 400;
                }
                resultCode = c.getResultCode();
                Log.i("test", "setManager " + resultCode);
                if (resultCode == 200) {
                    int memberRole = mMemberData.get(Integer.valueOf(itemNum)).getMemberRole();
                    if (memberRole != 2) {
                        mMemberData.get(Integer.valueOf(itemNum)).setMemberRole(2);
                    } else {
                        mMemberData.get(Integer.valueOf(itemNum)).setMemberRole(1);
                    }
                    mMemberListAdapter.notifyDataSetChanged();
                } else if (resultCode == 401) {

                } else {

                }
                mLoadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommenResponse> call, Throwable t) {

            }
        });

    }

    //获取公告
    private void getAnnListData(String token, String id) {
        //mobile/snsgroupMember/getUserListBySiteId
        Log.i("test", "开始请求mAnnTotal: " + mAnnTotal + " mAnnStart: " + mAnnStart);
        HttpReuqests.getInstance().getAnnList(token, id, mAnnStart, 5, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject j = new JSONObject(result);
                    if (j.has("resultCode")) {
                        String resultCode = j.getString("resultCode");
                        if (resultCode.equals("200")) {
                            if (j.has("data")) {
                                String data = j.getString("data");
                                JSONObject k = new JSONObject(data);
                                int total = 0;
                                if (k.has("total")) {
                                    total = k.getInt("total");
                                    mAnnTotal = total;
                                    Log.i("test", "请求返回mAnnTotal: " + mAnnTotal);
                                }
                                if (k.has("data")) {
                                    String array = k.getString("data");
                                    JSONArray AllianceArray = new JSONArray(array);
                                    for (int i = 0; i < AllianceArray.length(); i++) {
                                        JSONObject l = AllianceArray.getJSONObject(i);
                                        ChatGroupAnn item = new ChatGroupAnn();
                                        item.total = total;
                                        item.role = mMyRole;
                                        if (l.has("id")) {
                                            item.id = l.getString("id");
                                        }
                                        if (l.has("noticeText")) {
                                            item.noticeText = l.getString("noticeText");
                                        }
                                        if (l.has("addTime")) {
                                            item.addTime = l.getString("addTime");
                                        }
                                        mAnnItem.add(item);
                                    }
                                    mAnnStart = mAnnStart + AllianceArray.length();
                                    mAnnListAdapter.notifyDataSetChanged();
                                    mAnnSwipeToLoadLayout.setRefreshing(false);
                                    mAnnListAdapter.notifyItemRemoved(mAnnListAdapter.getItemCount());
                                }
                            }
                        } else if (resultCode.equals("401")) {

                        } else {

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    //移除公告
    private void delAnn(String token, String id, final int itemNum) {
        //mobile/ snsGroupNotice /delete
        HttpReuqests.getInstance().delAnn(token, id, new Callback<CommenResponse>() {
            @Override
            public void onResponse(Call<CommenResponse> call, Response<CommenResponse> response) {
                CommenResponse c = response.body();
                int resultCode = c.getResultCode();

                if (resultCode == 200) {
                    mAnnTotal = mAnnTotal - 1;
                    mAnnStart = mAnnStart - 1;
                    mAnnItem.remove(itemNum);
                    mAnnListAdapter.notifyDataSetChanged();
                } else if (resultCode == 401) {

                } else {

                }
                mLoadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommenResponse> call, Throwable t) {

            }
        });

    }

    private void initializeSelected() {
        mAttributesSelected.setVisibility(View.INVISIBLE);
        mMemberSelected.setVisibility(View.INVISIBLE);
        mAnnouncementSelected.setVisibility(View.INVISIBLE);
    }

    private void initializeShowItem() {
        mAttributesLinearLayout.setVisibility(View.GONE);
        mMemberList.setVisibility(View.GONE);
        mAnnSwipeToLoadLayout.setVisibility(View.GONE);
    }

    //解散讨论组
    private void deleteChatGroup() {
        HttpReuqests.getInstance().deleteChatGroupById(token, roomId, new Callback<CommenResponse>() {
            @Override
            public void onResponse(Call<CommenResponse> call, Response<CommenResponse> response) {
                CommenResponse commenResponse = response.body();
                Log.i(TAG, "response==>" + commenResponse.getResultCode());
                if (commenResponse.getResultCode() == 200) {
           /*         ToastUtil toastUtil = new ToastUtil(ChatGroupDetailsActivity.this, "讨论组解散成功");
                    toastUtil.show();*/
                    Toast.makeText(ChatGroupDetailsActivity.this, "讨论群解散成功", Toast.LENGTH_SHORT).show();
                    ChatGroupDetailsActivity.this.setResult(2);
                    finish();

                } else {
          /*          ToastUtil toastUtil = new ToastUtil(ChatGroupDetailsActivity.this, "讨论组解散失败");
                    toastUtil.show();*/
                    Toast.makeText(ChatGroupDetailsActivity.this, "讨论群解散失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommenResponse> call, Throwable t) {

            }
        });
    }

    //公告删除修改监听 成员删除管理监听
    @Override
    public void onClick(String type, final String position) {
        if (type.equals("del")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatGroupDetailsActivity.this);
            builder.setTitle("提示");
            builder.setMessage("是否删除本条公告");

            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //执行删除接口
                    delAnn(mToken, mAnnItem.get(Integer.valueOf(position)).id, Integer.valueOf(position));
                    mLoadingLayout.setVisibility(View.VISIBLE);
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else if (type.equals("edit")) {
            Log.i("test", position);
            //修改公告
        } else if (type.equals("delmember")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatGroupDetailsActivity.this);
            builder.setTitle("提示");
            builder.setMessage("是否移除此人");

            builder.setPositiveButton("移除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //执行移除接口
                    // delMember(mToken, mMemberData.get(Integer.valueOf(position)).getId(), Integer.valueOf(position));
                    delMemberBySocket(Integer.valueOf(position));
                    // mLoadingLayout.setVisibility(View.VISIBLE);
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else if (type.equals("manager")) {
            int memberRole = mMemberData.get(Integer.valueOf(position)).getMemberRole();
            String dialogMessage = "";
            if (memberRole != 2) {
                dialogMessage = "是否将此人设为管理员";
            } else {
                dialogMessage = "是否将此人取消管理员";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatGroupDetailsActivity.this);
            builder.setTitle("提示");
            builder.setMessage(dialogMessage);

            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //执行管理接口
                    setManager(mToken, mMemberData.get(Integer.valueOf(position)).getUserId(), mRoomId, Integer.valueOf(position));
                    mLoadingLayout.setVisibility(View.VISIBLE);
                }
            });

            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else if (type.equals("memberitem")) {
            Bundle bundle = new Bundle();
            bundle.putString("Expert_ID", mMemberData.get(Integer.valueOf(position)).getUserId());
            bundle.putString("Expert_NAME", mMemberData.get(Integer.valueOf(position)).getMemberName());
            Intent intent = new Intent(Const.Expert_URL);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatGroupDetailsActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定解散讨论群？");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //执行管理接口
                deleteChatGroup();
            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onLongClick(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAnnItem.clear();
        mAnnTotal = 0;
        mAnnStart = 0;
        mAnnListAdapter.notifyDataSetChanged();
        getManagerData(token, mRoomId);
    }
}
