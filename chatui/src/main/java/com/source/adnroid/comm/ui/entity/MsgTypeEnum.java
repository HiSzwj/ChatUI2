package com.source.adnroid.comm.ui.entity;

/**
 * Created by zzw on 2018/4/9.
 */

public enum MsgTypeEnum {
    TO_ROOM("toRoom"),//消息类型（聊天室（toRoom）/个人（toUser）
    TEXT_MSG("text"),//普通文本消息
    RESEND_TEXT_MSG("resendtext"),//重发普通文本消息
    IMAGE_MSG("image"),//图片消息
    VIDEO_MSG("video"),//视频消息
    PATIENT_MSG("patient"),//病例消息
    INVITE_JOIN_ROOM("inviteJoinRoom"),//人员增添消息
    REMOVE_FROM_ROOM("removedFromRoom"),//人员踢出消息
    GALLERY("gallery"),
    PICTURE("picture"),
    ;
    private String type;
    MsgTypeEnum(String type) {
        this.type = type;
    }
   public String getType() {
        return this.type;
    }
}
