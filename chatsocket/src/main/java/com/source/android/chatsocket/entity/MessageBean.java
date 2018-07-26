package com.source.android.chatsocket.entity;

/**
 * Created by zzw on 2018/4/9.
 */

public class MessageBean {

    /**
     * type : text
     * msg : 1123
     */
    private String type;
    private String msg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "type='" + type + '\'' +
                ", msg=" + msg +
                '}';
    }
}
