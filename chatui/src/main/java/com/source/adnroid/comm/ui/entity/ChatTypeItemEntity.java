package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;

/**
 * Created by zzw on 2018/4/27.
 */

public class ChatTypeItemEntity implements Serializable {



    private String itemid;
    private String itemcode;
    private String itemname;
    private String itemdescription;
    private String itemtypename;
    private int rank;
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

    public String getItemdescription() {
        return itemdescription;
    }

    public void setItemdescription(String itemdescription) {
        this.itemdescription = itemdescription;
    }

    public String getItemtypename() {
        return itemtypename;
    }

    public void setItemtypename(String itemtypename) {
        this.itemtypename = itemtypename;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getItemtypeid() {
        return itemtypeid;
    }

    public void setItemtypeid(String itemtypeid) {
        this.itemtypeid = itemtypeid;
    }

    @Override
    public String toString() {
        return "ChatTypeItemEntity{" +
                "itemid='" + itemid + '\'' +
                ", itemcode='" + itemcode + '\'' +
                ", itemname='" + itemname + '\'' +
                ", itemdescription='" + itemdescription + '\'' +
                ", itemtypename='" + itemtypename + '\'' +
                ", rank=" + rank +
                ", itemtypeid='" + itemtypeid + '\'' +
                '}';
    }
}
