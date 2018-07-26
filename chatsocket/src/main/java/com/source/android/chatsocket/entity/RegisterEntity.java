package com.source.android.chatsocket.entity;

/**
 * Created by zzw on 2018/4/4.
 */

public class RegisterEntity {
    String userId;

    public RegisterEntity(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
