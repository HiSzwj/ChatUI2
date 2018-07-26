package com.source.android.chatsocket.messages;

/**
 * Created by zzw on 2018/5/4.
 */

public class ChatUpLoadFileCallBackMessage {
    public ChatUpLoadFileCallBackMessage(String type, String id, String url, String thumUrl, String status, String roomId) {
        this.id = id;
        this.url = url;
        this.status = status;
        this.roomId = roomId;
        this.type = type;
        this.thumUrl = thumUrl;
    }

    private String type;
    private String id;
    private String url;
    private String thumUrl;
    private String status;
    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumUrl() {
        return thumUrl;
    }

    public void setThumUrl(String thumUrl) {
        this.thumUrl = thumUrl;
    }
}
