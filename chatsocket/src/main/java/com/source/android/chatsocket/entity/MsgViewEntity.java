package com.source.android.chatsocket.entity;

import java.util.Map;

/**
 * Created by zzw on 2018/4/17.
 */

public class MsgViewEntity extends MsgEntity {
    private String userName;
    private String userPhoto;
    private String msgStatus;//0 发送中 1发送成功 2 发送失败
    private Map<String,String> map;
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }
}
