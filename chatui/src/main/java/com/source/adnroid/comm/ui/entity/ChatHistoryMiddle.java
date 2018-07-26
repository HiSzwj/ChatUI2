package com.source.adnroid.comm.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzw on 2018/4/12.
 */

public class ChatHistoryMiddle implements Serializable {

    private Object order;
    private Object sortBy;
    private int begin;
    private int limit;
    private int total;
    private Object param;
    private Object parambody;
    private List<ChatHistoryInside> data;

    public Object getOrder() {
        return order;
    }

    public void setOrder(Object order) {
        this.order = order;
    }

    public Object getSortBy() {
        return sortBy;
    }

    public void setSortBy(Object sortBy) {
        this.sortBy = sortBy;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public Object getParambody() {
        return parambody;
    }

    public void setParambody(Object parambody) {
        this.parambody = parambody;
    }

    public List<ChatHistoryInside> getData() {
        return data;
    }

    public void setData(List<ChatHistoryInside> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ChatHistoryMiddle{" +
                "order=" + order +
                ", sortBy=" + sortBy +
                ", begin=" + begin +
                ", limit=" + limit +
                ", total=" + total +
                ", param=" + param +
                ", parambody=" + parambody +
                ", data=" + data+
                '}';
    }
}
