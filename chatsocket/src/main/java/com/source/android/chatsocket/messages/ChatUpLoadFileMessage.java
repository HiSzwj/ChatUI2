package com.source.android.chatsocket.messages;

/**
 * Created by zzw on 2018/5/4.
 */

public class ChatUpLoadFileMessage {
    public ChatUpLoadFileMessage(String type, String id,String roomId, String path, String token, String result) {
        this.type = type;
        this.id = id;
        this.path = path;
        this.token = token;
        this.result = result;
        this.roomId=roomId;
    }

    private String type;
    private String id;//消息表示
    private String roomId;//房间标识
    private String path;//文件路径
    private String token;
    private String result;//上传结果
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
