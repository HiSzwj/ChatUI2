package com.bsc.rtc.lib.chatui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.source.adnroid.comm.ui.activity.UserListActivity;
import com.source.adnroid.comm.ui.chatutils.ChatSerciceUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!ChatSerciceUtils.isServiceRunning(MainActivity.this, "com.source.android.chatsocket.service.MainService")) {
           // initChatSocket();//尝试启动服务
        }
        Intent intent1 = new Intent();
        intent1.setClass(MainActivity.this, UserListActivity.class);
 /*       intent1.putExtra("userId", SPUtils.get(MainActivity.this, PublicUrl.USER_ID_KEY, "").toString());
        intent1.putExtra(PublicUrl.TOKEN_KEY, getTokenFromLocal());*/
        startActivity(intent1);
    }

}
