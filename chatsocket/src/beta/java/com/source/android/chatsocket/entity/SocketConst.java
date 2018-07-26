package com.source.android.chatsocket.entity;


public class SocketConst {
    //KEY
    public static final String USER_ID_KEY = "USER_ID_KEY";
    public static final String TOKEN_KEY = "TOKEN_KEY";
    public static final String Socket_STATUS = "netStatus";//socket状态
    public static final String Socket_Register_STATUS = "socketRegisterStatus";//注册状态
    //public static final String Socket_ADDRESS_KEY="socketaddresskey";//socket地址缓存key
    public static final String CHAT_GROUP_ADD_PEOPLE = "inviteJoinRoom";
    public static final String CHAT_GROUP_REMOVE_PEOPLE = "removeUserFromRoom";
/*    //地址
    public static String CHAT_DEAULT_BASE = "http://192.168.0.159:8080";
    public static String CHAT_SOCKET_URL = "";
    public static String Socket_URL = CHAT_DEAULT_BASE + "/BSCTelmed/";//retrofit地址
    public static String CHAT_PIC_URL = CHAT_DEAULT_BASE + "/BSCTelmed";//图片地址
    public static String CHAT_COMMUNICATE_ADDRESS = CHAT_DEAULT_BASE;//视频聊天室地址
    public static String DEAULT_URL = CHAT_DEAULT_BASE + "/BSCTelmed";//项目地址*/

    //TODO 加入地址缓存机制
 /*   private static String getAddress(){
      if (CHAT_SOCKET_URL_FROM_NET!=null&&!TextUtils.isEmpty(CHAT_SOCKET_URL_FROM_NET)){
            return CHAT_SOCKET_URL_FROM_NET;
        }
    }
*/

}
