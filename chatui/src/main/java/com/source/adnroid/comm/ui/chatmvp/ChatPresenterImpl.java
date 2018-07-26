package com.source.adnroid.comm.ui.chatmvp;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.source.adnroid.comm.ui.chatmvp.base.IBaseView;
import com.source.adnroid.comm.ui.chatmvp.message.DBMessageEvent;
import com.source.adnroid.comm.ui.chatmvp.message.UserMessageEvent;
import com.source.adnroid.comm.ui.entity.ChatFileEntity;
import com.source.adnroid.comm.ui.entity.MsgTypeEnum;
import com.source.android.chatsocket.entity.MsgEntity;
import com.source.android.chatsocket.entity.MsgViewEntity;
import com.source.android.chatsocket.messages.ChatActivityStatusMessage;
import com.source.android.chatsocket.messages.ChatUpLoadFileCallBackMessage;
import com.source.android.chatsocket.messages.ChatUpLoadFileMessage;
import com.source.android.chatsocket.messages.HistoryMessageEvent;
import com.source.android.chatsocket.messages.MessageCallBack;
import com.source.android.chatsocket.messages.MessageEvent;
import com.source.android.chatsocket.messages.NetMessage;
import com.source.android.chatsocket.messages.NetReconnectMessage;
import com.source.android.chatsocket.messages.ServiceEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.UUID;

public class ChatPresenterImpl implements IChatConstract.IChatPresenter {
    private String TAG = this.getClass().getSimpleName();
    private IChatConstract.IChatView mView;
    private String roomId;
    private String token;
    private String userId;
    private Context context;

    public ChatPresenterImpl(String roomId, String token, String userId, Context context) {
        this.roomId = roomId;
        this.token = token;
        this.userId = userId;
        this.context = context;
    }

    @Override
    public void getData(int userId) {

    }

    @Override
    public void sendTextMessage(MsgEntity msgEntity) {
        Log.i(TAG, "msgEntity.getType==>" + msgEntity.getMessage().getType());
        switch (msgEntity.getMessage().getType()) {
            case "text":
                MessageEvent messageEvent = new MessageEvent(msgEntity, msgEntity.getFrom());
                EventBus.getDefault().post(messageEvent);
                break;
            case "picture":
                sendFile("picture", msgEntity);
                break;
            case "gallery":
                sendFile("gallery", msgEntity);
                break;
            case "image":
                MessageEvent imageEvent = new MessageEvent(msgEntity, msgEntity.getFrom());
                EventBus.getDefault().post(imageEvent);
                break;
        }
    }

    @Override
    public void getHistoryMessage(int begin, int limit, int type) {
        Log.i(TAG, "loadHistoryFinish==>start");
        ChatModule chatModule = new ChatModule();
        chatModule.getHistoryMessage(begin, limit, roomId, token, type);
    }

    @Override
    public void getMessageFromNativeDB() {
        ChatModule chatModule = new ChatModule();
        chatModule.getMessageFromNativeDB(roomId, userId, context);
    }

    @Override
    public void getUserMessage() {
        ChatModule chatModule = new ChatModule();
        chatModule.getGroupMembersMessage(token, roomId);
    }

    @Override
    public void deleteDBMessageByID(String id) {
        ChatModule chatModule = new ChatModule();
        int status = chatModule.deleteSuccessMsg(id, context);
        if (status == 1) {
            mView.deleteMsgSuccess(id);
        } else {
            mView.deleteMsgSuccess("failed");
        }

    }

    @Override
    public void findDBMessageByID(String id) {
        ChatModule chatModule = new ChatModule();
        MsgViewEntity msgViewEntity = chatModule.getMsgById(id, context);
        mView.findMsgSuccess(msgViewEntity);
        deleteDBMessageByID(id);
    }


    //上传文件
    public void sendFile(String type, MsgEntity msgEntity) {
        EventBus.getDefault().post(new ChatUpLoadFileMessage(type, msgEntity.getId(), msgEntity.getTo(), msgEntity.getMessage().getMsg(), token, "failer"));
    }



    @Override
    public void start() {
        mView.showText("sss...");
    }

    @Override
    public void attachView(IBaseView view) {
        Log.i(TAG, "attachView");
        this.mView = (IChatConstract.IChatView) view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detacheView() {
        Log.i(TAG, "detacheView");
        this.mView = null;
        EventBus.getDefault().unregister(this);

    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    //接受发送消息回掉状态
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(MessageCallBack messageCallBack) {
        Log.d(TAG, "从socket获取到的messageCallBack信息=>" + messageCallBack.getMsg().toString() + "status==>" + messageCallBack.getStatus());
        mView.messageCallBack(messageCallBack);
    }

/*    //接受上传文件回掉状态
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(ChatUpLoadFileCallBackMessage chatUpLoadFileCallBackMessage) {
        Log.d(TAG, "从service获取到的文件回传信息=>" + "type==" + chatUpLoadFileCallBackMessage.getType() + "  id==" + chatUpLoadFileCallBackMessage.getId() + "status==>" + chatUpLoadFileCallBackMessage.getStatus());
        switch (chatUpLoadFileCallBackMessage.getType()) {
            case "picture":
                mView.upLoadFileCallBack(chatUpLoadFileCallBackMessage);//发送大盘UI界面更新UI
                ChatFileEntity pictureEntity = ConverToChatFileEntity(chatUpLoadFileCallBackMessage.getUrl(), chatUpLoadFileCallBackMessage.getUrl());
                String msg = JSON.toJSONString(pictureEntity);
                MsgEntity pictureMsgEntity = MsgEntity.parse(userId, msg, roomId, MsgTypeEnum.TO_ROOM.getType(), MsgTypeEnum.IMAGE_MSG.getType());
                pictureMsgEntity.setId(chatUpLoadFileCallBackMessage.getId());
                sendTextMessage(pictureMsgEntity);//发送到MainService发送socket消息
                break;
            case "gallery":
                mView.upLoadFileCallBack(chatUpLoadFileCallBackMessage);//发送大盘UI界面更新UI
                ChatFileEntity galleryFileEntity = ConverToChatFileEntity(chatUpLoadFileCallBackMessage.getUrl(), chatUpLoadFileCallBackMessage.getUrl());
                String galleryMsg = JSON.toJSONString(galleryFileEntity);
                MsgEntity galleryEntity = MsgEntity.parse(userId, galleryMsg, roomId, MsgTypeEnum.TO_ROOM.getType(), MsgTypeEnum.IMAGE_MSG.getType());
                galleryEntity.setId(chatUpLoadFileCallBackMessage.getId());
                sendTextMessage(galleryEntity);//发送到MainService发送socket消息
                break;
            case "video":

                break;
        }

    }*/

    //加载历史消息
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(HistoryMessageEvent historyMessageEvent) {
        Log.d(TAG, "加载历史数据回传信息=>" + "rooID==" + historyMessageEvent.getRoomId());
        if (isViewAttached()) {
            mView.loadHistoryFinish(historyMessageEvent.getList(), historyMessageEvent.getType());
        }
    }

    //获取数据库数据信息
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(DBMessageEvent dbMessageEvent) {
        Log.d(TAG, "加载本地数据回传信息=>" + "rooID==" + dbMessageEvent.getRoomId() + "msg====>" + dbMessageEvent.toString());
        if (isViewAttached()) {
            mView.loadLocalDBFinish(dbMessageEvent.getList());
        }
    }

    //获取讨论组成员信息
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(UserMessageEvent userMessageEvent) {
        Log.d(TAG, "加载讨论组成员信息回传信息=>" + "rooID==" + userMessageEvent.getRoomID());
        if (isViewAttached()) {
            mView.loadUserMessageFinish(userMessageEvent.getUserMap());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    //接受从socket发送的聊天消息
    public void onMoonEvent(ServiceEvent messageEvent) {
        Log.d(TAG, "从socket中获取到的ServiceEvent信息==>" + messageEvent.getMsgEntity().toString());
        //roomId判断是否发往本房间信息
        mView.showRemoteMessage(messageEvent.getMsgEntity());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(NetReconnectMessage netReconnectMessage) {
        Log.d(TAG, "从socket中获取到的NetReconnectMessage信息=>" + netReconnectMessage.getNetStatus());
        mView.refereshNetReconnectStatus(netReconnectMessage);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(NetMessage netMessage) {
        Log.d(TAG, "从socket中获取到的NetMessage信息=>" + netMessage.getNetStatus());
        mView.refereshNetStatus(netMessage);
    }
}
