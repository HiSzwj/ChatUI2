package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by zzw on 2018/4/10.
 */

public class MessagePatientEntity implements Serializable {
    private String patientId;
    private String userMessage;
    private String userSex;
    private String userPatientMessage;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getUserPatientMessage() {
        return userPatientMessage;
    }

    public String getUserSex() {
        if (userSex==null){
            return "未知";
        }
        if (userSex.equals("0") || userSex.equals("男")) {
            return  "男";
        } else if (userSex.equals("1") || userSex.equals("女")) {
            return  "女";
        } else {
            return "未知";
        }

    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setUserPatientMessage(String userPatientMessage) {
        this.userPatientMessage = userPatientMessage;
    }
}
