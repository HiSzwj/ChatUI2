package com.source.android.chatsocket.net;



import com.source.android.chatsocket.entity.CommenResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Header;

import retrofit2.http.Query;

/**
 * Created by zzw on 2018/4/8.
 */

public interface ChatInterfaceService {
    //获取聊天室
    @GET("mobile/snsgroup/getSnsGroupListByType")
    Call<ResponseBody> getRooms(@Header("Authorization") String header, @Query("userId") String userId);
    //获取聊天室
    @GET("mobile/getInfo/getNodeJsServe")
    Call<CommenResponse> getNodeJsServer(@Header("Authorization") String header, @Query("config") String config);

}
