package com.source.android.chatsocket.messages;


import com.source.android.chatsocket.entity.MsgEntity;

/**
 * Created by zzw on 2018/4/4.
 */

public class ServiceEvent {
    MsgEntity msgEntity;


    public ServiceEvent(MsgEntity msgEntity) {
        this.msgEntity = msgEntity;
    }

    public MsgEntity getMsgEntity() {
        return msgEntity;
    }

    public void setMsgEntity(MsgEntity msgEntity) {
        this.msgEntity = msgEntity;
    }
}
