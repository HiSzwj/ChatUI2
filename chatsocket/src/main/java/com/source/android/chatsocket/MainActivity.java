package com.source.android.chatsocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.source.android.chatsocket.messages.ServiceEvent;
import com.source.android.chatsocket.service.MainService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity {
    Intent intent;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // EventBus.getDefault().post(new MessageEvent("MainActivity"));
            }
        });

    }
    public void init(){
        intent=new Intent();
        intent.setClass(this, MainService.class);
        startService(intent);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(ServiceEvent serviceEvent){
       // Log.d("zzw",serviceEvent.getMessage());
    }
}
