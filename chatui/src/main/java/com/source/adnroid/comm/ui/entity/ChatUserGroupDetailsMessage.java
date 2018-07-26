package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by zzw on 2018/4/17.
 */

public class ChatUserGroupDetailsMessage implements Serializable {

    /**
     * id : 21cfcfb2e7224c54a15c342cdfd38124
     * groupId : 91a938666dc146c1a394db7da01f4619
     * siteId : fdafa7a8c6e74088af6b2d0deb9d02b3
     * departId : 7f9a81f700704d918938cf9ddc53651a
     * userId : 023b0c8038da41fc983515d40d8d3514
     * addTime : 1523580714000
     * memberRole : 0
     * memberName : 二级医院管理员
     * jobtitle : null
     * siteName : null
     * mobile : null
     * description : null
     * photo : null
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
