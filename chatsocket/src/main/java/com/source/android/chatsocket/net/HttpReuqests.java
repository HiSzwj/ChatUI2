package com.source.android.chatsocket.net;


import com.bsc.chat.commenbase.BaseConst;
import com.source.android.chatsocket.entity.CommenResponse;
import com.source.android.chatsocket.entity.SocketConst;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zzw on 2018/4/8.
 */

public class HttpReuqests {
    private String TAG = "SocketHttpReuqests";

    public void getRooms(String header, String userId, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = service.getRooms(header, userId);
        call.enqueue(callback);
    }

    public void getNodeJsServer(String header, String config, Callback<CommenResponse> callback) {
        Call<CommenResponse> call = service.getNodeJsServer(header, config);
        call.enqueue(callback);
    }

    /**
     * 初始化retrofit
     */
    Retrofit retrofit;
    ChatInterfaceService service;
    private static HttpReuqests httpReuqests;

    public HttpReuqests() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseConst.Socket_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ChatInterfaceService.class);

    }

    public static void resetRequest() {
        httpReuqests = null;
    }

    public static HttpReuqests getInstance() {
        if (httpReuqests == null) {
            synchronized (HttpReuqests.class) {
                if (httpReuqests == null) {
                    httpReuqests = new HttpReuqests();
                }
            }
        }
        return httpReuqests;
    }


}
