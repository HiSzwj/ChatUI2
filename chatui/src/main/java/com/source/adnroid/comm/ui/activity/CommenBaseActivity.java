package com.source.adnroid.comm.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CommenBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

}
