package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by zzw on 2018/4/12.
 */

public class ChatHistoryInside implements Serializable {

    private String id;
    private String groupId;
    private String userId;
    private String message;
    private int messageType;
    private long addTime;
    private long validTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public long getValidTime() {
        return validTime;
    }

    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    @Override
    public String toString() {
        return "ChatHistoryInside{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", userId='" + userId + '\'' +
                ", message='" + message + '\'' +
                ", messageType=" + messageType +
                ", addTime=" + addTime +
                ", validTime=" + validTime +
                '}';
    }
}
