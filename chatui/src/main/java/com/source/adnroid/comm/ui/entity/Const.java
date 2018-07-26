package com.source.adnroid.comm.ui.entity;

import com.source.android.chatsocket.entity.SocketConst;

/**
 * Created by zzw on 2018/4/10.
 */

public class Const {
    public static final String USER_ID_KEY = "USER_ID_KEY";
    public static final String USER_ID = "userId";//用户id
    public static final String ROOM_ID = "roomId";//用户id
    public static String TELEMEDICINE_INFO_ID = "TELEMEDICINE_INFO_ID";//会诊单Id
    public static final String TOKEN_KEY = "TOKEN_KEY";
    public static final String ROOM_NAME = "ROOM_NAME";
/*    //地址
    public static String CHAT_URL = SocketConst.Socket_URL;
    //图片地址
    public static String CHAT_PIC_URL = SocketConst.CHAT_PIC_URL;*/
    //跳转地址
    public static final String PATIENT_URL = "chat.ChatTemedicineInfoActivity";//病例
    public static final String Expert_URL = "chat.expertActivity";//专家详情
    public static final String Socket_STATUS = SocketConst.Socket_STATUS;//socket状态
    public static final String Socket_Register_STATUS = SocketConst.Socket_Register_STATUS;//注册状态
}
