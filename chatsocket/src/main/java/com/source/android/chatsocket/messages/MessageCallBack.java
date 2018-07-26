package com.source.android.chatsocket.messages;

import com.source.android.chatsocket.entity.MsgEntity;

/**
 * Created by zzw on 2018/4/25.
 */

public class MessageCallBack {
    private MsgEntity msg;
    private int status;

    public MessageCallBack(MsgEntity msg, int status) {
        this.msg = msg;
        this.status = status;
    }

    public MsgEntity getMsg() {
        return msg;
    }

    public void setMsg(MsgEntity msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
