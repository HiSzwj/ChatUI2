package com.source.android.chatsocket.entity;

/**
 * Created by zzw on 2018/4/8.
 */

public class DataBean {
    private String id;
    private Object groupType;
    private String name;
    private String description;
    private Object createTime;
    private Object createUserId;
    private Object groupHeadId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getGroupType() {
        return groupType;
    }

    public void setGroupType(Object groupType) {
        this.groupType = groupType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Object createUserId) {
        this.createUserId = createUserId;
    }

    public Object getGroupHeadId() {
        return groupHeadId;
    }

    public void setGroupHeadId(Object groupHeadId) {
        this.groupHeadId = groupHeadId;
    }
}

