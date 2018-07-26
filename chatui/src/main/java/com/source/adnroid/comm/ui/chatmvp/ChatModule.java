package com.source.adnroid.comm.ui.chatmvp;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.source.adnroid.comm.ui.chatmvp.message.DBMessageEvent;
import com.source.adnroid.comm.ui.chatmvp.message.UserMessageEvent;
import com.source.adnroid.comm.ui.entity.ChatHistoryInside;
import com.source.adnroid.comm.ui.entity.ChatHistoryMessage;
import com.source.adnroid.comm.ui.entity.ChatUserGroupDetailsMessage;
import com.source.adnroid.comm.ui.entity.CommenResponse;
import com.source.adnroid.comm.ui.entity.MsgTypeEnum;
import com.source.adnroid.comm.ui.net.HttpReuqests;
import com.source.android.chatsocket.chatdb.ChatDBManager;
import com.source.android.chatsocket.entity.MsgEntity;
import com.source.android.chatsocket.entity.MsgViewEntity;
import com.source.android.chatsocket.messages.HistoryMessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatModule {
    private String TAG = "ChatModule";


    //加载历史数据 type  用于区分是否第一次加载历史数据 决定是否滑动到底部
    public void getHistoryMessage(int begin, int limit, final String roomId, String token, final int type) {
        Log.i(TAG, "begin==>" + begin + " limit=>" + limit);
        final List<MsgEntity> tempMsgList = new ArrayList<MsgEntity>();
        HttpReuqests.getInstance().getSnsDiscussListByPage(token, begin, limit, roomId, new Callback<ChatHistoryMessage>() {
            @Override
            public void onResponse(Call<ChatHistoryMessage> call, Response<ChatHistoryMessage> response) {
                Log.i(TAG, "getHistory success=+>" + response.body().getData());
                ChatHistoryMessage chatHistoryMessage = response.body();
                if (chatHistoryMessage.getData() != null) {
                    Log.i(TAG, "chatHistoryMessage ==>" + chatHistoryMessage.toString());
                    List<ChatHistoryInside> chatHistoryInsideList = chatHistoryMessage.getData().getData();
                    Log.i(TAG, "chatHistoryInsideList size==>" + chatHistoryInsideList.size());

                    for (ChatHistoryInside item : chatHistoryInsideList) {
                        String msgItem = item.getMessage();
                        try {
                            MsgEntity msgEntity = JSONObject.parseObject(msgItem, MsgEntity.class);
                            tempMsgList.add(msgEntity);
                        } catch (Exception e) {
                            Log.e(TAG, "msgEntity cast failed==>" + e.getMessage());
                        }
                    }
                    //  Collections.reverse(tempMsgList);
                }
                EventBus.getDefault().post(new HistoryMessageEvent(roomId, tempMsgList, type));
            }

            @Override
            public void onFailure(Call<ChatHistoryMessage> call, Throwable t) {
                Log.i(TAG, "getHistory failed");
            }
        });
        Log.i(TAG, "getHistory finish====>" + tempMsgList.size());
    }

    //加载本地发送失败数据
    public void getMessageFromNativeDB(String roomId, String userId, Context context) {
        Log.i(TAG, "getMessageFromNativeDB");
        ChatDBManager chatDBManager = new ChatDBManager(context);
        List<MsgViewEntity> tempList = chatDBManager.queryMsgByGroupId(userId, roomId);
        for (MsgViewEntity item : tempList) {
            Log.i(TAG, "item==>" + item.getId());
            try {
                if (item.getMessage().getType().equals(MsgTypeEnum.GALLERY.getType()) || item.getMessage().getType().equals(MsgTypeEnum.PICTURE.getType())) {
                    item.getMessage().setType(MsgTypeEnum.IMAGE_MSG.getType());
                }
            } catch (Exception e) {
                Log.i(TAG, "msgEntity cast failed==>" + e.getMessage());
            }
        }
        EventBus.getDefault().post(new DBMessageEvent(roomId, tempList));
    }

    //获取讨论组成员信息并维护在本地
    public void getGroupMembersMessage(String token, final String roomId) {
        final Map<String, ChatUserGroupDetailsMessage> userMap = new HashMap<String, ChatUserGroupDetailsMessage>();
        HttpReuqests.getInstance().getSnsMemberByGroupId(token, roomId, new Callback<CommenResponse<List<ChatUserGroupDetailsMessage>>>() {
            @Override
            public void onResponse(Call<CommenResponse<List<ChatUserGroupDetailsMessage>>> call, Response<CommenResponse<List<ChatUserGroupDetailsMessage>>> response) {
                CommenResponse<List<ChatUserGroupDetailsMessage>> commenResponse = response.body();
                Log.i(TAG, "getGroupMembersMessage==>" + commenResponse.getResultCode());
                for (ChatUserGroupDetailsMessage item : commenResponse.getData()) {
                    // Log.i(TAG, "userId==>" + item.getUserId());
                    userMap.put(item.getUserId(), item);
                }
                EventBus.getDefault().post(new UserMessageEvent(roomId, userMap, roomId));
            }

            @Override
            public void onFailure(Call<CommenResponse<List<ChatUserGroupDetailsMessage>>> call, Throwable t) {
                Log.i(TAG, "getGroupMembersMessage  onFailure");
            }
        });
    }

    //根据ID查询指定数据
    public MsgViewEntity getMsgById(String id, Context context) {
        ChatDBManager chatDBManager = new ChatDBManager(context);
        MsgViewEntity msgViewEntity = null;
        msgViewEntity = chatDBManager.queryMsgById(id);
        try {
            Log.i(TAG, "chatMsg From DB==>" + msgViewEntity.toString());
        } catch (Exception e) {
            Log.i(TAG, "getMsgById err" + e.getMessage());
        }
        return msgViewEntity;
    }

    //删除发送成功的数据
    public int deleteSuccessMsg(String msgId, Context context) {
        ChatDBManager chatDBManager = new ChatDBManager(context);
        return chatDBManager.deleteMessage(msgId);
    }
}



