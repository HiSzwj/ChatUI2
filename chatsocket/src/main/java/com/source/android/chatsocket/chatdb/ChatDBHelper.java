package com.source.android.chatsocket.chatdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zzw on 2018/5/4.
 */

public class ChatDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "com_chinabsc_chat_chat_comm.db";
    public static final String TABLE_NAME = "chat_message";

    public ChatDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(message_order INTEGER PRIMARY KEY  NOT NULL," +
                "id INTEGER (36) NOT NULL ," +
                "group_id varchar(36) DEFAULT NULL ," +
                "user_id varchar(36)  DEFAULT NULL  ," +
                "message text  DEFAULT NULL ," +
                "message_type int(11)  DEFAULT NULL ," +
                "add_time datetime(0)  DEFAULT NULL ," +
                "valid_time datetime(0)  DEFAULT NULL ," +
                "message_status  INTEGER (5) DEFAULT NULL)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
