package com.source.android.chatsocket.messages;

/**
 * Created by zzw on 2018/4/27.
 */

public class CommenSocketResponse {
    private String id;
    private String type;
    private int resultCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
