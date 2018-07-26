package com.source.adnroid.comm.ui.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.source.adnroid.comm.ui.entity.Const;

public class ChatBaseActivity extends AppCompatActivity {

    public String userId;
    public String token;
    public TextView titleName;
    public String roomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
     /*  *//*set it to be full screen*//*
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        super.onCreate(savedInstanceState);
              /*set it to be no title*/
        getSupportActionBar().hide();
        userId=getIntent().getStringExtra(Const.USER_ID);
        token=getIntent().getStringExtra(Const.TOKEN_KEY);
        roomId=getIntent().getStringExtra(Const.ROOM_ID);
        Log.i("ChatBaseActivity","userId==>"+userId+" token==>"+token+" roomId==>"+roomId);

    }
    public void onBackClick(View v){
        onBackPressed();
    }

}
