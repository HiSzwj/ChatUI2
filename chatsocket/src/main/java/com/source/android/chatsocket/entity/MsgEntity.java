package com.source.android.chatsocket.entity;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by zzw on 2018/4/9.
 */

public class MsgEntity implements Serializable{

    /**
     * type : toRoom
     * to : 11
     * message : {"type":"text","msg":1123}
     * fromUser : 004
     */
    private String id;
    private String type;
    private String to;
    private MessageBean message;
    private String from;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static MsgEntity parse(String fromUser, String myMsg, String roomId, String toTypeEnum, String textTypeEnum){
        MsgEntity msgEntity=new MsgEntity();
        msgEntity.setId(UUID.randomUUID().toString());
        msgEntity.setFrom(fromUser);
        msgEntity.setTo(roomId);
        msgEntity.setType(toTypeEnum);
        MessageBean messageBean=new MessageBean();
        messageBean.setMsg(myMsg);
        messageBean.setType(textTypeEnum);
        msgEntity.setMessage(messageBean);
        return msgEntity;
    }
    @Override
    public String toString() {
        return "MsgEntity{" +
                "type='" + type + '\'' +
                ", to='" + to + '\'' +
                ", message=" + message.toString() +
                ", from='" + from + '\'' +
                '}';
    }
}
