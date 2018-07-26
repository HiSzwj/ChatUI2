package com.source.adnroid.comm.ui.chatmvp.message;

import com.source.android.chatsocket.entity.MsgViewEntity;

import java.util.List;

public class DBMessageEvent {
    private String roomId;
    private List<MsgViewEntity> list;

    public DBMessageEvent(String roomId, List<MsgViewEntity> list) {
        this.roomId = roomId;
        this.list = list;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<MsgViewEntity> getList() {
        return list;
    }

    public void setList(List<MsgViewEntity> list) {
        this.list = list;
    }
}
