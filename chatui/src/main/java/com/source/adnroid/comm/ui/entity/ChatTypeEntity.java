package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzw on 2018/4/11.
 */

public class ChatTypeEntity implements Serializable{



    private int resultCode;
    private String resultMsg;
    private List<ChatTypeItemEntity> data;

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

    public List<ChatTypeItemEntity> getData() {
        return data;
    }

    public void setData(List<ChatTypeItemEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ChatTypeEntity{" +
                "resultCode=" + resultCode +
                ", resultMsg='" + resultMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
