package com.source.android.chatsocket.chatdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.alibaba.fastjson.JSONObject;
import com.source.android.chatsocket.entity.MsgViewEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatDBManager {
    private  String  TAG="ChatDBManager";
    private ChatDBHelper helper;
    private SQLiteDatabase db;

    public ChatDBManager(Context context) {
        helper = new ChatDBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void add(String id, String groupId, String userId, String message, String messageType, long addTime, long valideTime, int status) {
        db.beginTransaction();    //开始事务
        try {
            db.execSQL("INSERT INTO chat_message VALUES(null,?,?,?,?,?,?,?,?)",
                    new Object[]{id, groupId, userId,
                            message, messageType, LongToDate(addTime),
                            LongToDate(valideTime), status});
            db.setTransactionSuccessful();    //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void updateMessage(String id, int status) {
        String sql1 = "update chat_message set message_status = " + status + " where id = '" + id+ "'";
        db.execSQL(sql1);//执行修改
    }

    public int deleteMessage(String id) {
        int result;
        db.beginTransaction();    //开始事务
        try {
            result = db.delete("chat_message", "id = ?", new String[]{id});
            db.setTransactionSuccessful();    //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
        return result;
    }

    //查询所有消息 测试用
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM chat_message", null);
        return c;
    }

    //根据消息ID查询指定消息
    public MsgViewEntity queryMsgById(String id) {
        MsgViewEntity msgViewEntity = null;
        Cursor cursor = db.rawQuery("SELECT * FROM chat_message where id = '" + id + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                int msgIndex = cursor.getColumnIndex("message");
                String msg = cursor.getString(msgIndex);
                Log.i(TAG,"queryMsgById msg==>"+msg);
                int statusIndex = cursor.getColumnIndex("message_status");
                int status = cursor.getInt(statusIndex);
                msgViewEntity = JSONObject.parseObject(msg, MsgViewEntity.class);
                msgViewEntity.setMsgStatus(String.valueOf(status));
            } while (cursor.moveToNext());
        }
        return msgViewEntity;
    }

    //根据组groupId查询消息
    public List<MsgViewEntity> queryMsgByGroupId(String userId, String groupId) {
        List<MsgViewEntity> list = new ArrayList<MsgViewEntity>();
        Cursor cursor = db.rawQuery("SELECT * FROM chat_message where group_id = '" + groupId + "' and user_id='" + userId + "'", null);
        Log.i(TAG,"queryMsgByGroupId count==>"+cursor.getCount()+"position"+cursor.getPosition());
        cursor.moveToFirst();
        Log.i(TAG,"queryMsgByGroupId position==>"+cursor.getPosition());
        if (cursor.getCount() > 0) {
            do {
                int msgIndex = cursor.getColumnIndex("message");
                String msg = cursor.getString(msgIndex);
                Log.i(TAG,"queryMsgByGroupId msg==>"+msg);
                int statusIndex = cursor.getColumnIndex("message_status");
                int status = cursor.getInt(statusIndex);
                MsgViewEntity msgViewEntity = JSONObject.parseObject(msg, MsgViewEntity.class);
                msgViewEntity.setMsgStatus(String.valueOf(status));
                list.add(msgViewEntity);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void closeDB() {
        db.close();
    }

    private Date StringToDate(String dateStr) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.e("ChatDBManager", e.getMessage());
        }
        return date;
    }

    public Date LongToDate(long str) {
        return new Date(str);
    }
}
