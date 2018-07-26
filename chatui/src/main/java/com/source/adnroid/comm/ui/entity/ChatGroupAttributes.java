package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by Feng on 2018/4/16.
 */

public class ChatGroupAttributes implements Serializable {

    /**
     * resultCode : 200
     * resultMsg :
     * data : {"id":"4b268ee08af44755991d6242a1903007","groupType":"心电系统","name":"讨论组1","description":"亲住也破也破做最切","createTime":1523848962000,"createUserId":"c71a7d39eeea4eea95fdbd768cfabe8d","groupHeadId":null,"count":0,"realName":"华佗再世","siteName":"专家医院","notice":null,"message":null,"jobtitle":"主任医师","mobile":"13522022535","skill":"习近平指出，中以建交25年来，双边关系总体保持平稳健康发展。两国高层互访频繁，务实合作稳步推进，人文交流日益密切。习近平指出，中以建交25年来，双边关系总体保持平稳健康发展。两国高层互访频繁，务实合作稳步推进，人文交流日益密切。习近平指出，中以建交25年来，双边关系总体保持平稳健康发展。两国高层互访频繁，务实合作稳步推进，人文交流日益密切。","departName":"骨科"}
     */

    private int resultCode;
    private String resultMsg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 4b268ee08af44755991d6242a1903007
         * groupType : 心电系统
         * name : 讨论组1
         * description : 亲住也破也破做最切
         * createTime : 1523848962000
         * createUserId : c71a7d39eeea4eea95fdbd768cfabe8d
         * groupHeadId : null
         * count : 0
         * realName : 华佗再世
         * siteName : 专家医院
         * notice : null
         * message : null
         * jobtitle : 主任医师
         * mobile : 13522022535
         * skill : 习近平指出，中以建交25年来，双边关系总体保持平稳健康发展。两国高层互访频繁，务实合作稳步推进，人文交流日益密切。习近平指出，中以建交25年来，双边关系总体保持平稳健康发展。两国高层互访频繁，务实合作稳步推进，人文交流日益密切。习近平指出，中以建交25年来，双边关系总体保持平稳健康发展。两国高层互访频繁，务实合作稳步推进，人文交流日益密切。
         * departName : 骨科
         */

        private String id;
        private String groupType;
        private String name;
        private String photo;
        private String description;
        private long createTime;
        private String createUserId;
        private Object groupHeadId;
        private int count;
        private String realName;
        private String siteName;
        private Object notice;
        private Object message;
        private String jobtitle;
        private String mobile;
        private String skill;
        private String departName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
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

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
        }

        public Object getGroupHeadId() {
            return groupHeadId;
        }

        public void setGroupHeadId(Object groupHeadId) {
            this.groupHeadId = groupHeadId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getRealName() {
            return realName;
        }

        public String getPhoto() {
            return photo;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public Object getNotice() {
            return notice;
        }

        public void setNotice(Object notice) {
            this.notice = notice;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }

        public String getJobtitle() {
            return jobtitle;
        }

        public void setJobtitle(String jobtitle) {
            this.jobtitle = jobtitle;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public String getDepartName() {
            return departName;
        }

        public void setDepartName(String departName) {
            this.departName = departName;
        }
    }
}
