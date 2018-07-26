package com.source.android.chatsocket.messages;

/**
 * Created by zzw on 2018/4/27.
 */

public class ChatGroupPeopleChangeCallBackMessage {
    private String type;
    private String userId;
    private int result;
    private String roomId;

    public ChatGroupPeopleChangeCallBackMessage(String type, int result, String userId, String roomId) {
        this.type=type;
        this.result = result;
        this.userId = userId;
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
