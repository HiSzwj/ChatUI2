package com.source.android.chatsocket.messages;


import com.source.android.chatsocket.entity.MsgEntity;

import java.util.List;

/**
 * Created by zzw on 2018/4/4.
 */

public class HistoryMessageEvent {
    public HistoryMessageEvent(String roomId, List<MsgEntity> list,int type) {
        this.roomId = roomId;
        this.list = list;
        this.type=type;
    }

    private String roomId;
    private List<MsgEntity> list;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<MsgEntity> getList() {
        return list;
    }

    public void setList(List<MsgEntity> list) {
        this.list = list;
    }
}
