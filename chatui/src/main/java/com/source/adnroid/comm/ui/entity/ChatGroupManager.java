package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by Feng on 2018/4/19.
 */

public class ChatGroupManager implements Serializable {

    /**
     * id : 6f496050f9ea4f86893f5b6559857d42
     * groupId : 44fe04a293f241758fe9335349b5f984
     * siteId : fdafa7a8c6e74088af6b2d0deb9d02b3
     * departId : 7f9a81f700704d918938cf9ddc53651a
     * userId : 071fe71521eb48df95d2c244250cbe2f
     * addTime : 1524119008000
     * memberRole : 2
     * memberName : 跳转专家
     * jobtitle : 主任医师
     * siteName : 专家医院
     * mobile :
     * description :
     * photo :
     */

    private String id;
    private String groupId;
    private String siteId;
    private String departId;
    private String userId;
    private long addTime;
    private int memberRole;
    private String memberName;
    private String jobtitle;
    private String siteName;
    private String mobile;
    private String description;
    private String photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public int getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(int memberRole) {
        this.memberRole = memberRole;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
