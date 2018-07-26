package com.source.adnroid.comm.ui.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.adapter.ChatSpinnerAdapter;
import com.source.adnroid.comm.ui.chatview.LoadingView;
import com.source.adnroid.comm.ui.entity.ChatTypeEntity;
import com.source.adnroid.comm.ui.entity.ChatTypeItem;
import com.source.adnroid.comm.ui.entity.ChatTypeItemEntity;
import com.source.adnroid.comm.ui.entity.CommenResponse;
import com.source.adnroid.comm.ui.net.HttpReuqests;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewGroupActivity extends ChatBaseActivity {
    private String TAG = "CreateNewGroupActivity";
    Spinner chatGroupType;
    EditText chatGroupName;
    EditText chatGroupDescription;
    String chatGroupTypeCode;
    LoadingView loadingView;
    private ChatSpinnerAdapter adapter;
    private List<ChatTypeItem> list;
    MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);
        initView();
        initChatType();

    }

    private void initView() {
        loadingView = findViewById(R.id.mload);
        chatGroupType = findViewById(R.id.chat_group_type);
        chatGroupName = findViewById(R.id.chat_group_name);
        chatGroupDescription = findViewById(R.id.chat_group_description);
        list = new ArrayList<ChatTypeItem>();
        adapter = new ChatSpinnerAdapter(this, list);
        chatGroupType.setAdapter(adapter);
        chatGroupType.setOnItemSelectedListener(new OnSpinnerItemSelectedListener());
    }

    class OnSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, " select position type==>" + list.get(position).getCode());
            chatGroupTypeCode = list.get(position).getCode();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void ChatCreatGroupSave(View v) {
        loadingView.setVisibility(View.VISIBLE);
        String chatGroupNameTemp = chatGroupName.getText().toString();
        String chatGroupDescriptionTemp = chatGroupDescription.getText().toString();
        Log.i(TAG, "chatGroupTypeCode=>" + chatGroupTypeCode + " chatGroupNameTemp=>" + chatGroupNameTemp + " " +
                "chatGroupDescriptionTemp==>" + chatGroupDescriptionTemp + " userId=>" + userId);
        HttpReuqests.getInstance().doAddChat(token, chatGroupTypeCode, chatGroupNameTemp, chatGroupDescriptionTemp, userId, new Callback<CommenResponse>() {
            @Override
            public void onResponse(Call<CommenResponse> call, Response<CommenResponse> response) {
                CommenResponse commenResponse = response.body();
                Log.i(TAG, "ChatCreatGroupSave result==>" + commenResponse.getResultCode());
                if (commenResponse.getResultCode() == 200) {
                    handler.sendEmptyMessage(2);
                }
            }

            @Override
            public void onFailure(Call<CommenResponse> call, Throwable t) {

            }
        });
    }

    //加载聊天室类别
    private void initChatType() {
        loadingView.setVisibility(View.VISIBLE);
        HttpReuqests.getInstance().getTypeListInfo(token, new Callback<ChatTypeEntity>() {
            @Override
            public void onResponse(Call<ChatTypeEntity> call, Response<ChatTypeEntity> response) {
                Log.i(TAG, "getTypeListInfo success");
                List<ChatTypeItemEntity> templist = response.body().getData();
                for (ChatTypeItemEntity dataBean : templist) {
                    if (dataBean == null) {
                        return;
                    }
                    ChatTypeItem item = new ChatTypeItem();
                    item.setName(dataBean.getItemname());
                    item.setCode(dataBean.getItemid());
                    list.add(item);
                }
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(Call<ChatTypeEntity> call, Throwable t) {
                Log.i(TAG, "getTypeListInfo onFailure");
            }
        });
    }

    static class MyHandler extends Handler {
        private final WeakReference<CreateNewGroupActivity> mActivity;

        public MyHandler(CreateNewGroupActivity mactivity) {
            mActivity = new WeakReference<CreateNewGroupActivity>(mactivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity == null) {
                return;
            }
            if (msg.what == 1) {
                mActivity.get().loadingView.setVisibility(View.INVISIBLE);
                mActivity.get().adapter.notifyDataSetChanged();
            } else if (msg.what == 2) {
                mActivity.get().loadingView.setVisibility(View.INVISIBLE);
                mActivity.get().showUpdateDialog("创建成功");
            }
        }
    }

    @Override
    public void onBackClick(View v) {
        super.onBackClick(v);
    }

    private void showUpdateDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(text);
        builder.setMessage(text);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }
}
