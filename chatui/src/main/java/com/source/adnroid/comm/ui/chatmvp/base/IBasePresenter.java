package com.source.adnroid.comm.ui.chatmvp.base;

public interface IBasePresenter<V extends IBaseView> {
    void start();
    void attachView(V view);
    void detacheView();
    boolean isViewAttached();

}
