package com.source.adnroid.comm.ui.entity;

/**
 * Created by zzw on 2018/5/2.
 */

public class ChatFileResultEntity {

    /**
     * businessId : aa137cb961a04a708404b9911185ab8a
     * url : /data/sns\2018\05\02\aa13\bea963029f0449d5838baa20f56de074.jpg
     */

    private String businessId;
    private String url;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ChatFileResultEntity{" +
                "businessId='" + businessId + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
