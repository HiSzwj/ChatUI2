package com.source.adnroid.comm.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.Toast;


import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;

import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.adapter.UserListAdapter;
import com.source.adnroid.comm.ui.chatview.ToastUtil;
import com.source.adnroid.comm.ui.entity.ChatTypeEntity;
import com.source.adnroid.comm.ui.entity.ChatTypeItem;
import com.source.adnroid.comm.ui.entity.ChatTypeItemEntity;
import com.source.adnroid.comm.ui.entity.Const;

import com.source.adnroid.comm.ui.entity.RoomEntity;
import com.source.adnroid.comm.ui.interfaces.OnUserListItemClickListener;
import com.source.adnroid.comm.ui.net.HttpReuqests;

import com.source.android.chatsocket.messages.NetMessage;
import com.source.android.chatsocket.messages.NetReconnectMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends UserShareBaseActivity implements OnUserListItemClickListener, OnLoadMoreListener, OnRefreshListener {
    private String TAG = "UserListActivity";
    MsgHandler msgHandler = new MsgHandler(this);
    UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        EventBus.getDefault().register(this);
        initBaseView();
        setTitleName("讨论群");


        initAdapterAndListener();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initData();
        initChatType();
        getSPMessage();
    }

    @Override
    public void initAdapterAndListener() {
        userListAdapter = new UserListAdapter(list);
        //为recyclerView设置适配器
        recyclerView.setAdapter(userListAdapter);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        userListAdapter.setOnItemClickListener(this);
    }

    @Override
    public void OnTabItemClick() {
        if (selectedType == 0) {
            initData();
        } else {
            Log.i(TAG, "mTabItem code=>" + mTabItem.get(selectedType).getCode());
            upDateChat(mTabItem.get(selectedType).getCode());
        }
    }

    //加载顶部聊天室类别
    private void initChatType() {
        mTabItem.clear();
        ChatTypeItem item = new ChatTypeItem();
        item.setName("全部");
        item.setCode("");
        mTabItem.add(item);
        HttpReuqests.getInstance().getSnsGroupTypeByUserId(token, userId, new Callback<ChatTypeEntity>() {
            @Override
            public void onResponse(Call<ChatTypeEntity> call, Response<ChatTypeEntity> response) {
                ChatTypeEntity chatTypeEntity = response.body();
                Log.i(TAG, "getTypeListInfo success===>" + chatTypeEntity.toString());
                List<ChatTypeItemEntity> list = new ArrayList<>();
                list.addAll(response.body().getData());
                for (ChatTypeItemEntity dataBean : list) {
                    if (dataBean != null) {
                        ChatTypeItem item = new ChatTypeItem();
                        item.setName(dataBean.getItemname());
                        item.setCode(dataBean.getItemid());
                        mTabItem.add(item);
                    }
                }
                mTabSize = mTabItem.size();
                msgHandler.sendEmptyMessage(2);
            }

            @Override
            public void onFailure(Call<ChatTypeEntity> call, Throwable t) {
                Log.e(TAG, "getTypeListInfo onFailure");
            }
        });


    }

    //加载所有聊天室
    private void initData() {
        Log.i(TAG, "initData==userId==>" + userId);
        HttpReuqests.getInstance().getSnsGroupListByType(token, "", userId, new Callback<RoomEntity>() {
            @Override
            public void onResponse(Call<RoomEntity> call, Response<RoomEntity> response) {
                RoomEntity roomEntity = response.body();
                Log.d(TAG, "getSnsGroupList success");
                Message msg = new Message();
                msg.what = 1;
                msg.obj = roomEntity;
                msgHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<RoomEntity> call, Throwable t) {
                Log.e(TAG, "getSnsGroupList onFailure" + t.getMessage());
            }
        });
    }

    //根据类型查询聊天室聊天室
    private void upDateChat(String grouptype) {
        Log.i(TAG, "upDateChat==userId==>" + userId);
        HttpReuqests.getInstance().getSnsGroupListByType(token, grouptype, userId, new Callback<RoomEntity>() {
            @Override
            public void onResponse(Call<RoomEntity> call, Response<RoomEntity> response) {
                Log.d(TAG, "getSnsGroupList success" + response.toString());
                RoomEntity roomEntity = response.body();
                Message msg = new Message();
                msg.what = 3;
                msg.obj = roomEntity;
                msgHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<RoomEntity> call, Throwable t) {
                Log.e(TAG, "getSnsGroupList onFailure" + t.getMessage());
            }
        });
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onRefresh() {
        if (selectedType == 0) {
            initData();
        } else {
            Log.i(TAG, "mTabItem code=>" + mTabItem.get(selectedType).getCode());
            upDateChat(mTabItem.get(selectedType).getCode());
        }
        swipeToLoadLayout.setRefreshing(false);
    }

    static class MsgHandler extends Handler {
        private final WeakReference<UserListActivity> mActivity;

        public MsgHandler(UserListActivity mactivity) {
            mActivity = new WeakReference<UserListActivity>(mactivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity == null) {
                return;
            }
            if (msg.what == 1) {//加载所有聊天室
                mActivity.get().list.clear();
                try {
                    mActivity.get().list.addAll(((RoomEntity) msg.obj).getData());
                    mActivity.get().userListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(mActivity.get().TAG, mActivity.get().TAG + "==>" + e.getMessage());
              /*      ToastUtil toastUtil = new ToastUtil(mActivity.get(), "聊天室加载失败");
                    toastUtil.show();*/
                    Toast.makeText(mActivity.get(), "聊天室加载失败", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 2) {//加载顶部聊天室类别
                mActivity.get().setTabData();
            } else if (msg.what == 3) {//根据类别查询聊天室
                mActivity.get().list.clear();
                mActivity.get().userListAdapter.notifyDataSetChanged();
                try {
                    mActivity.get().list.addAll(((RoomEntity) msg.obj).getData());
                    mActivity.get().userListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(mActivity.get().TAG, mActivity.get().TAG + "==>" + e.getMessage());
          /*          ToastUtil toastUtil = new ToastUtil(mActivity.get(), "聊天室加载失败");
                    toastUtil.show();*/
                    Toast.makeText(mActivity.get(), "聊天室加载失败", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 4) {
                Log.i(mActivity.get().TAG, "4 change status==>" + msg.arg1);
                mActivity.get().changeNetStatus(msg.arg1);
            } else if (msg.what == 5) {
                Log.i(mActivity.get().TAG, "5 change reconnect status==>" + msg.arg1);
                mActivity.get().changeNetReconnectStatus(msg.arg1);
            }
        }
    }

    //接受到socket 重新注册状态
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(NetReconnectMessage netReconnectMessage) {
        Log.d(TAG, "从socket中获取到的netReconnectMessage信息=>" + netReconnectMessage.getNetStatus());
        Message msg = new Message();
        msg.what = 5;
        msg.arg1 = netReconnectMessage.getNetStatus();
        msgHandler.sendMessage(msg);
    }

    //接受到socket状态
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(NetMessage netMessage) {
        Log.d(TAG, "从socket中获取到的NetMessage信息=>" + netMessage.getNetStatus());
        Message msg = new Message();
        msg.what = 4;
        msg.arg1 = netMessage.getNetStatus();
        msgHandler.sendMessage(msg);
    }

    @Override
    public void onClick(String roomId, String roomName) {
        Log.i(TAG, "UserListActivity onClick roomId==>" + roomId + " roomName==>" + roomName);
        Intent intent = new Intent();
        intent.putExtra(Const.ROOM_ID, roomId);
        intent.putExtra(Const.USER_ID, userId);
        intent.putExtra(Const.TOKEN_KEY, token);
        intent.putExtra(Const.ROOM_NAME, roomName);
        intent.setClass(this, ChatActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {

    }

    public void createNewGroup(View v) {
        Intent intent = new Intent();
        intent.setClass(UserListActivity.this, CreateNewGroupActivity.class);
        intent.putExtra(Const.TOKEN_KEY, token);
        intent.putExtra(Const.USER_ID, userId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
