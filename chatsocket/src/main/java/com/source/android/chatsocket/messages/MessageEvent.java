package com.source.android.chatsocket.messages;


import com.source.android.chatsocket.entity.MsgEntity;

/**
 * Created by zzw on 2018/4/4.
 */

public class MessageEvent {
    String userId;
    MsgEntity msgEntity;
    public MessageEvent(MsgEntity msgEntity,String userId) {
        this.msgEntity = msgEntity;
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MsgEntity getMsgEntity() {
        return msgEntity;
    }

    public void setMsgEntity(MsgEntity msgEntity) {
        this.msgEntity = msgEntity;
    }
}
