package com.source.adnroid.comm.ui.chatmvp.message;

import com.source.adnroid.comm.ui.entity.ChatUserGroupDetailsMessage;

import java.util.Map;

public class UserMessageEvent {
    private String userId;
    private Map<String, ChatUserGroupDetailsMessage> userMap;
    private String roomID;

    public UserMessageEvent(String userId, Map<String, ChatUserGroupDetailsMessage> userMap,String roomID) {
        this.userId = userId;
        this.userMap = userMap;
        this.roomID=roomID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, ChatUserGroupDetailsMessage> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, ChatUserGroupDetailsMessage> userMap) {
        this.userMap = userMap;
    }
}
