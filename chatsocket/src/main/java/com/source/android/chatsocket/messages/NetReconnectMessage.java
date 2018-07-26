package com.source.android.chatsocket.messages;

/**
 * Created by zzw on 2018/4/18.
 */

public class NetReconnectMessage {

    public NetReconnectMessage(int netStatus) {
        this.netStatus = netStatus;
    }

    public int netStatus;//0表示连接断开 1表示连接成功 2开始重连

    public int getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(int netStatus) {
        this.netStatus = netStatus;
    }
}
