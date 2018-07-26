package com.source.adnroid.comm.ui.chatservice;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.source.adnroid.comm.ui.entity.ChatFileEntity;
import com.source.adnroid.comm.ui.net.FileUploadObserver;
import com.source.adnroid.comm.ui.net.HttpReuqests;
import com.source.android.chatsocket.messages.ChatUpLoadFileCallBackMessage;
import com.source.android.chatsocket.messages.ChatUpLoadFileMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;

public class ChatUpLoadService extends Service {
    private String TAG = "ChatUpLoadService";

    public ChatUpLoadService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand==>");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(ChatUpLoadFileMessage chatUpLoadFileMessage) {
        Log.i(TAG, "ChatUpLoadFileMessage==>type==>"+chatUpLoadFileMessage.getType()+"path==>" + chatUpLoadFileMessage.getPath());
        ChatFileEntity chatFileEntity = JSONObject.parseObject(chatUpLoadFileMessage.getPath(), ChatFileEntity.class);
        switch (chatUpLoadFileMessage.getType()) {
            case "picture":

                File file = new File(chatFileEntity.getUrl());
                upLoadFiles("picture", chatUpLoadFileMessage.getId(), file, chatUpLoadFileMessage.getToken(), chatUpLoadFileMessage.getRoomId());
                break;
            case "gallery":
                Uri newUri = Uri.parse(chatFileEntity.getUrl());
                // Log.i(TAG, "newUri==>" + newUri.getPath());
                File galleryfile = new File(newUri.getPath());
                upLoadFiles("gallery", chatUpLoadFileMessage.getId(), galleryfile, chatUpLoadFileMessage.getToken(), chatUpLoadFileMessage.getRoomId());
                break;
        }
    }

    //发送上传回掉信息
    private void sendStatus(String type, String id, String url, String thumUrl, String status, String roomId) {
        ChatUpLoadFileCallBackMessage msg = new ChatUpLoadFileCallBackMessage(type, id, url, thumUrl, status, roomId);
        EventBus.getDefault().post(msg);
    }

    //上传文件
    public void upLoadFiles(final String type, final String id, File file, String token, final String roomId) {
        HttpReuqests.getInstance().upLoadFilePicAndVideo(token, file, "", new FileUploadObserver<ResponseBody>() {
            @Override
            public void onUpLoadSuccess(ResponseBody response) {
                try {
                    String temp = response.string();
                    Log.i(TAG, "temp==>" + temp);
                    JSONObject jsonObject = JSONObject.parseObject(temp);
                    String url = jsonObject.getJSONObject("data").getString("url");
                    Log.i(TAG, "url==>" + url);
                    if (!TextUtils.isEmpty(url)) {
                        sendStatus(type, id, url, url, "1", roomId);

                    } else {
                        sendStatus(type, id, url, url, "0", roomId);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "upLoadFiles err==>" + e.getMessage());
                    sendStatus(type, id, "", "", "0", roomId);
                }
            }

            @Override
            public void onUpLoadFail(Throwable e) {
                //TODO 加入失败处理
                Log.i(TAG, "onUpLoadFail==>" + e.getMessage());
            }

            @Override
            public void onProgress(int progress) {
                Log.i(TAG, "upLoadFiles==>" + progress);
            }
        });
    }
}
