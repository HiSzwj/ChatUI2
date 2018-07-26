package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by zzw on 2018/4/10.
 */

public class PatientEntity implements Serializable {

    /**
     * resultCode : 200
     * resultMsg :
     * data : {"clinicId":null,"dateDone":null,"purpose":null,"history":null,"treatment":null,"diagnosis":null,"sndSiteName":null,"doctorName":null,"patientName":null,"patientGender":null,"age":0,"folk":null,"marriage":null}
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
         * clinicId : null
         * dateDone : null
         * purpose : null
         * history : null
         * treatment : null
         * diagnosis : null
         * sndSiteName : null
         * doctorName : null
         * patientName : null
         * patientGender : null
         * age : 0
         * folk : null
         * marriage : null
         */

        private String clinicId;
        private String dateDone;
        private String purpose;
        private String history;
        private String treatment;
        private String diagnosis;
        private String sndSiteName;
        private String doctorName;
        private String patientName;
        private String patientGender;
        private int age;
        private String folk;
        private String marriage;

        public String getClinicId() {
            return clinicId;
        }

        public void setClinicId(String clinicId) {
            this.clinicId = clinicId;
        }

        public String getDateDone() {
            return dateDone;
        }

        public void setDateDone(String dateDone) {
            this.dateDone = dateDone;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getHistory() {
            return history;
        }

        public void setHistory(String history) {
            this.history = history;
        }

        public String getTreatment() {
            return treatment;
        }

        public void setTreatment(String treatment) {
            this.treatment = treatment;
        }

        public String getDiagnosis() {
            return diagnosis;
        }

        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }

        public String getSndSiteName() {
            return sndSiteName;
        }

        public void setSndSiteName(String sndSiteName) {
            this.sndSiteName = sndSiteName;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getPatientGender() {
            if (patientGender.equals("0") || patientGender.equals("男")) {
                return "男";
            } else if (patientGender.equals("1") || patientGender.equals("女")) {
                return  "女";
            } else {
                return "未知";
            }

        }

        public void setPatientGender(String patientGender) {
            this.patientGender = patientGender;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getFolk() {
            return folk;
        }

        public void setFolk(String folk) {
            this.folk = folk;
        }

        public String getMarriage() {
            return marriage;
        }

        public void setMarriage(String marriage) {
            this.marriage = marriage;
        }
    }
}
