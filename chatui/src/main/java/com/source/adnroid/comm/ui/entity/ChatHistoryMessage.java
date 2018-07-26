package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by zzw on 2018/4/12.
 */

public class ChatHistoryMessage implements Serializable {

    private int resultCode;
    private String resultMsg;
    private ChatHistoryMiddle data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public ChatHistoryMiddle getData() {
        return data;
    }

    public void setData(ChatHistoryMiddle data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ChatHistoryMessage{" +
                "resultCode=" + resultCode +
                ", resultMsg='" + resultMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
