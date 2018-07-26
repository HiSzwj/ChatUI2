package com.source.adnroid.comm.ui.activity;



import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.source.adnroid.comm.ui.R;

import com.source.android.chatsocket.chatdb.ChatDBManager;

public class TestActivity extends AppCompatActivity {
    ChatDBManager chatDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
    }
    private void init(){
        chatDBManager=new ChatDBManager(this);
       // chatDBManager.add();
        Cursor cursor= chatDBManager.queryTheCursor();
        Log.i("TestActivity",cursor.getCount()+"count");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()){
                int index = cursor.getColumnIndex("id");
                String name = cursor.getString(index);
                Log.i("TestActivity","name==>"+name);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatDBManager.closeDB();
    }
}
