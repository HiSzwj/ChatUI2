package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by Feng on 2018/4/17.
 */

public class AddMemberProvince implements Serializable {

    /**
     * itemid : 674a10dd746d4583aaad75bcb6cbb262
     * itemcode : 110000
     * itemname : 北京市
     * itemdescription : null
     * itemtypename : 省份
     * rank : null
     * itemtypeid : fd787b087b3d44f2a88bdfbfe28cfcbb
     */

    private String itemid;
    private String itemcode;
    private String itemname;
    private Object itemdescription;
    private String itemtypename;
    private Object rank;
    private String itemtypeid;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public Object getItemdescription() {
        return itemdescription;
    }

    public void setItemdescription(Object itemdescription) {
        this.itemdescription = itemdescription;
    }

    public String getItemtypename() {
        return itemtypename;
    }

    public void setItemtypename(String itemtypename) {
        this.itemtypename = itemtypename;
    }

    public Object getRank() {
        return rank;
    }

    public void setRank(Object rank) {
        this.rank = rank;
    }

    public String getItemtypeid() {
        return itemtypeid;
    }

    public void setItemtypeid(String itemtypeid) {
        this.itemtypeid = itemtypeid;
    }
}
