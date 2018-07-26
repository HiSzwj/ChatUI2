package com.source.adnroid.comm.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.entity.CommenResponse;
import com.source.adnroid.comm.ui.net.HttpReuqests;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatGroupAddAnnActivity extends ChatBaseActivity {
    private String TAG = "ChatGroupAddAnnActivity";
    private ImageView mBackImageView;

    private EditText mAnnTextEdit;
    private Button mSendButton;
    private RelativeLayout mLoadingLayout;

    private String mGroupid;
    private String mUid;
    private String mToken;

    public ChatGroupAddAnnActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_add_ann);
        initIntent();
        init();
    }

    private void initIntent() {
        mGroupid = roomId;
        mToken = token;
        mUid = userId;
        Log.i(TAG, "mGroupid==>" + mGroupid + "  token==>" + token + "  mUid==>" + mUid);
    }

    private void init() {
        mBackImageView = findViewById(R.id.BackImageView);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mLoadingLayout = findViewById(R.id.LoadingLayout);
        mAnnTextEdit = findViewById(R.id.AnnTextEdit);
        mSendButton = findViewById(R.id.SendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mAnnTextEdit.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    sendAnn(mToken, text, mUid, mGroupid);
                    mLoadingLayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "请输入内容", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void sendAnn(String token, String text, String uid, String gid) {
        // mobile/snsGroupNotice/doAdd
        HttpReuqests.getInstance().addAnn(token, text, uid, gid, new Callback<CommenResponse>() {
            @Override
            public void onResponse(Call<CommenResponse> call, Response<CommenResponse> response) {
                CommenResponse c = response.body();
                int resultCode = c.getResultCode();
                Log.i(TAG, resultCode + "");
                if (resultCode == 200) {
                    finish();
                } else if (resultCode == 401) {

                } else {

                }
                mLoadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommenResponse> call, Throwable t) {

            }
        });
    }
}
