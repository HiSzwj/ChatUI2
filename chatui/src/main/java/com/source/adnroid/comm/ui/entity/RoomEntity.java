package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzw on 2018/4/8.
 */

public class RoomEntity implements Serializable {

    /**
     * resultCode : 200
     * resultMsg :
     * data : [{"id":"11","groupType":null,"name":"讨论组","description":"描述","createTime":null,"createUserId":null,"groupHeadId":null},{"id":"12","groupType":null,"name":"讨论组12","description":"描述描述描述描述描述描述","createTime":null,"createUserId":null,"groupHeadId":null},{"id":"514b78baaeeb4d688886924e4039e1c2","groupType":"心电系统","name":"心电讨论群","description":null,"createTime":1523169884000,"createUserId":null,"groupHeadId":null}]
     */

    private int resultCode;
    private String resultMsg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "resultCode="+resultCode+"resultMsg="+resultMsg;
    }
}
