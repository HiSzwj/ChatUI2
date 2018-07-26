package com.source.adnroid.comm.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsc.chat.commenbase.BaseConst;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.entity.ChatUseDetailMessage;
import com.source.adnroid.comm.ui.entity.CommenResponse;
import com.source.adnroid.comm.ui.entity.Const;
import com.source.adnroid.comm.ui.net.HttpReuqests;


import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsActivity extends ChatBaseActivity {
    String TAG = "UserDetailsActivity";
    ImageView userIamge;
    TextView userName;
    TextView userJob;
    TextView userDescription;
    ChatUseDetailMessage chatUseDetailMessage;
    MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        initView();
        getUserMessage();
    }

    private void initView() {
        userIamge = findViewById(R.id.chat_user_image);
        userName = findViewById(R.id.chat_user_name);
        userJob = findViewById(R.id.chat_user_job);
        userDescription = findViewById(R.id.chat_user_description);
        titleName = findViewById(R.id.chat_title_name);
        titleName.setText("信息");
    }

    private void setData() {
        String picUrl = chatUseDetailMessage.getPhoto();
        RequestOptions options = new RequestOptions();
        options.circleCrop();
        options.placeholder(R.mipmap.ic_launcher);
        options.error(R.mipmap.ic_launcher);
        Glide.with(UserDetailsActivity.this).load(BaseConst.CHAT_PIC_URL + picUrl).apply(options).into(userIamge);

        userName.setText(chatUseDetailMessage.getUsername());
        userJob.setText(chatUseDetailMessage.getJobtitle());
        userDescription.setText(chatUseDetailMessage.getDescription());
    }

    private void getUserMessage() {
        HttpReuqests.getInstance().findMemberById(token, userId, new Callback<CommenResponse<ChatUseDetailMessage>>() {
            @Override
            public void onResponse(Call<CommenResponse<ChatUseDetailMessage>> call, Response<CommenResponse<ChatUseDetailMessage>> response) {
                CommenResponse commenResponse = response.body();
                Log.i(TAG, "findMemberById==>" + commenResponse.getResultCode());
                if (commenResponse.getResultCode() == 200) {
                    chatUseDetailMessage = (ChatUseDetailMessage) commenResponse.getData();
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(Call<CommenResponse<ChatUseDetailMessage>> call, Throwable t) {

            }
        });
    }

    static class MyHandler extends Handler {
        private final WeakReference<UserDetailsActivity> mActivity;

        public MyHandler(UserDetailsActivity mactivity) {
            mActivity = new WeakReference<UserDetailsActivity>(mactivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity == null) {
                return;
            }
            if (msg.what == 1) {
                mActivity.get().setData();
            }
        }
    }

    public void onBackClick(View v) {
        onBackPressed();
    }

}
