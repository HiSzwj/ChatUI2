package com.source.adnroid.comm.ui.chatmvp.base;

public interface IBaseView<P extends IBasePresenter> {
    void setPresenter(P presenter);
}
