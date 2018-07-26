package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by Feng on 2018/4/17.
 */

public class ChatGroupMember implements Serializable {

    /**
     * id : 01
     * groupId : 4b268ee08af44755991d6242a1903007
     * siteId : null
     * departId : null
     * userId : 01acd4631e2e498c9ff90d1ed480a710
     * addTime : null
     * memberRole : null
     * memberName : 专家测试医院11111
     * jobtitle : null
     * siteName : 专家测试医院11111
     * mobile : 18918812140
     * description : null
     * photo : null
     */

    private String id;
    private String groupId;
    private String siteId;
    private String departId;
    private String userId;
    private String addTime;
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

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
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
