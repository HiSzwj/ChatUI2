package com.source.adnroid.comm.ui.chatmvp;

import android.content.Context;

import com.source.adnroid.comm.ui.chatmvp.base.IBasePresenter;
import com.source.adnroid.comm.ui.chatmvp.base.IBaseView;
import com.source.adnroid.comm.ui.entity.ChatUserGroupDetailsMessage;
import com.source.android.chatsocket.entity.MsgEntity;
import com.source.android.chatsocket.entity.MsgViewEntity;
import com.source.android.chatsocket.messages.ChatUpLoadFileCallBackMessage;
import com.source.android.chatsocket.messages.MessageCallBack;
import com.source.android.chatsocket.messages.NetMessage;
import com.source.android.chatsocket.messages.NetReconnectMessage;

import java.util.List;
import java.util.Map;

public interface IChatConstract {
    public interface IChatPresenter extends IBasePresenter {
        //获取数据
        void getData(int userId);
        //发送消息
        void sendTextMessage(MsgEntity msgEntity);
        //加载历史数据
        void getHistoryMessage(int begin, int limit,int type);
        //加载本地数据void
        void getMessageFromNativeDB();
        //加载用户信息
        void getUserMessage();
        //删除指定数据
        void deleteDBMessageByID(String id);
        //查询指定数据
        void findDBMessageByID(String id);
    }

    public interface IChatView extends IBaseView {
        //显示加载中
        void showText(String s);
        //显示远端消息
        void showRemoteMessage(MsgEntity msgEntity);
        //消息回掉
        void messageCallBack(MessageCallBack messageCallBack);
        //本地数据加载完成
        void loadLocalDBFinish(List<MsgViewEntity> list);
        //历史数据加载完成
        void loadHistoryFinish(List<MsgEntity> list,int type);
        //用户信息加载完成
        void loadUserMessageFinish(Map<String, ChatUserGroupDetailsMessage> userMap);
        //成功根据ID查询数据
        void findMsgSuccess(MsgViewEntity msgEntity);
        //成功根据ID删除数据
        void deleteMsgSuccess(String position);
        //从socket中获取到的NetReconnectMessage信息
        void refereshNetReconnectStatus(NetReconnectMessage netReconnectMessage);
        //从socket中获取到的NetMessage信息
        void refereshNetStatus(NetMessage netMessage);
    }
}
