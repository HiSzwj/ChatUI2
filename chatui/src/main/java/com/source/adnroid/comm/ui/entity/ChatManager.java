package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by zzw on 2018/4/8.
 */

public class ChatManager implements Serializable {
    private String Id;
    private String roomId;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
