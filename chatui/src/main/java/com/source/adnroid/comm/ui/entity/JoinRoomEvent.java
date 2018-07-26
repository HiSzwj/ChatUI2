package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by zzw on 2018/4/9.
 */

public class JoinRoomEvent implements Serializable {
    //Id: “joinRoom”  // message  类型
    //roomId: “12313213”  /房间id
    public String Id;
    public String roomId;

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
