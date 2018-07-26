package com.source.adnroid.comm.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.chatservice.ChatUpLoadService;
import com.source.adnroid.comm.ui.entity.Const;


public class ChatActivity extends ChatBaseActivity {
    private String TAG = "ChatActivity";
    private com.source.adnroid.comm.ui.chatmvp.ChatFragment chatFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    ImageView chatBackImage;
    String roomId = "";
    String roomName = "";
    Intent intent;
    private int CHAT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        startUpLoadFileService();
        getRoomId();
        if (roomId != "" && roomId != null) {
            initChatFragment();
        }
    }

    private void getRoomId() {
        roomId = getIntent().getStringExtra(Const.ROOM_ID);
        roomName = getIntent().getStringExtra(Const.ROOM_NAME);
        Log.i(TAG, "roomId ==>" + roomId + " " + "userId ==>" + userId + "token==>" + token + " roomName==>" + roomName);
        titleName = findViewById(R.id.chat_title_name);
        titleName.setText(roomName);
        chatBackImage = findViewById(R.id.chat_back_image);
        chatBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick(v);
            }
        });
        ImageView chatInfoImage = findViewById(R.id.chat_info_image);
        chatInfoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, ChatGroupDetailsActivity.class);
                intent.putExtra(Const.ROOM_ID, roomId);
                intent.putExtra(Const.TOKEN_KEY, token);
                intent.putExtra(Const.USER_ID, userId);
                startActivityForResult(intent, CHAT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "resultCode==>" + resultCode);
        if (resultCode == 2) {
            finish();
        }
    }

    /**
     * 拿到事务管理器并开启事务
     */
    private void initChatFragment() {
        chatFragment = new com.source.adnroid.comm.ui.chatmvp.ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Const.ROOM_ID, roomId);
        bundle.putString(Const.USER_ID, userId);
        bundle.putString(Const.TOKEN_KEY, token);
        chatFragment.setArguments(bundle);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.chat_content, chatFragment);
        transaction.commit();
    }

    private void startUpLoadFileService() {
        intent = new Intent(this, ChatUpLoadService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        stopService(intent);
        super.onDestroy();
    }

    public void onBackClick(View v) {
        onBackPressed();
    }

}
