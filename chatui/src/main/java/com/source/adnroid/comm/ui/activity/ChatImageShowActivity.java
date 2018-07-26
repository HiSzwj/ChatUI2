package com.source.adnroid.comm.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.chatview.TouchImageView;
public class ChatImageShowActivity extends ChatBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        String url = getIntent().getStringExtra("url");
        TouchImageView touchImageView = findViewById(R.id.chat_image);
        RequestOptions imageUpLoadSuceess = new RequestOptions();//图片成功消息配置
        imageUpLoadSuceess.placeholder(R.mipmap.chat_loading);
        imageUpLoadSuceess.error(R.mipmap.chat_load_failed);
        Glide.with(this).load(url).apply(imageUpLoadSuceess).into(touchImageView);
        touchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
}
