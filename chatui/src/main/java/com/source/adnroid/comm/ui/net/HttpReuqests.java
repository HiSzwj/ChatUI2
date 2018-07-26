package com.source.adnroid.comm.ui.net;


import com.bsc.chat.commenbase.BaseConst;
import com.source.adnroid.comm.ui.entity.AddMemberHospital;
import com.source.adnroid.comm.ui.entity.AddMemberProvince;
import com.source.adnroid.comm.ui.entity.ChatGroupAttributes;
import com.source.adnroid.comm.ui.entity.ChatGroupManager;
import com.source.adnroid.comm.ui.entity.ChatGroupMember;
import com.source.adnroid.comm.ui.entity.ChatHistoryMessage;
import com.source.adnroid.comm.ui.entity.ChatTypeEntity;
import com.source.adnroid.comm.ui.entity.ChatUseDetailMessage;
import com.source.adnroid.comm.ui.entity.ChatUserGroupDetailsMessage;
import com.source.adnroid.comm.ui.entity.CommenResponse;
import com.source.adnroid.comm.ui.entity.Const;
import com.source.adnroid.comm.ui.entity.PatientEntity;
import com.source.adnroid.comm.ui.entity.RoomEntity;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by zzw on 2018/4/8.
 */

public class HttpReuqests {
    private String TAG = "HttpReuqests";

    /**
     * 单上传文件的封装
     *
     * @param file               需要上传的文件
     * @param fileUploadObserver 上传回调
     */
    public void upLoadFilePicAndVideo(String header, File file, String type, FileUploadObserver<ResponseBody> fileUploadObserver) {
        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(file, fileUploadObserver);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), uploadFileRequestBody);
        service.uploadFile(header, part, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver);
    }

    public void getSnsGroupListByType(String header, Callback<RoomEntity> callback) {
        Call<RoomEntity> call = service.getSnsGroupListByType(header);
        call.enqueue(callback);
    }

    public void getSnsGroupListByType(String header, String type, String userId, Callback<RoomEntity> callback) {
        Call<RoomEntity> call = service.getSnsGroupListByType(header, type, userId);
        call.enqueue(callback);
    }

    public void getTelemedicineInfo(String header, String TelemedicineInfoId, Callback<PatientEntity> callback) {
        Call<PatientEntity> call = service.getTelemedicineInfo(header, TelemedicineInfoId);
        call.enqueue(callback);
    }

    public void getTypeListInfo(String header, Callback<ChatTypeEntity> callback) {
        Call<ChatTypeEntity> call = service.getTypeListInfo(header);
        call.enqueue(callback);
    }
    //讨论组类别
    public void getSnsGroupTypeByUserId(String header, String userId, Callback<ChatTypeEntity> callback) {
        Call<ChatTypeEntity> call = service.getSnsGroupTypeByUserId(header, userId);
        call.enqueue(callback);
    }

    public void doAddChat(String header, String groupType, String name, String description, String createUserId, Callback<CommenResponse> callback) {
        Call<CommenResponse> call = service.doAddChat(header, groupType, name, description, createUserId);
        call.enqueue(callback);
    }

    public void getSnsDiscussListByPage(String header, int begin, int limit, String groupId, Callback<ChatHistoryMessage> callback) {
        Call<ChatHistoryMessage> call = service.getSnsDiscussListByPage(header, begin, limit, groupId);
        call.enqueue(callback);
    }

    public void getGroupAttributes(String header, String id, Callback<ChatGroupAttributes> callback) {
        Call<ChatGroupAttributes> call = service.getGroupAttributesByPage1(header, id);
        call.enqueue(callback);
    }

    public void getGroupMember(String header, String id, Callback<CommenResponse<List<ChatGroupMember>>> callback) {
        Call<CommenResponse<List<ChatGroupMember>>> call = service.getGroupMemberById(header, id);
        call.enqueue(callback);
    }

    public void getGroupManager(String header, String id, Callback<CommenResponse<List<ChatGroupManager>>> callback) {
        Call<CommenResponse<List<ChatGroupManager>>> call = service.getGroupManagerById(header, id);
        call.enqueue(callback);
    }

    public void getAddMemberProvince(String header, Callback<CommenResponse<List<AddMemberProvince>>> callback) {
        Call<CommenResponse<List<AddMemberProvince>>> call = service.getAddMemberProvince(header);
        call.enqueue(callback);
    }

    public void getAddMemberHospital(String header, String id, Callback<CommenResponse<List<AddMemberHospital>>> callback) {
        Call<CommenResponse<List<AddMemberHospital>>> call = service.getAddMemberHospital(header, id);
        call.enqueue(callback);
    }

    public void getAddMemberList(String token, String siteid, String groupid, int begin, int limit, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = service.getAddMemberList(token, siteid, groupid, begin, limit);
        call.enqueue(callback);
    }

    public void delMember(String token, String uid, Callback<CommenResponse> callback) {
        Call<CommenResponse> call = service.delMember(token, uid);
        call.enqueue(callback);
    }

    public void setManager(String token, String uid, String gid, Callback<CommenResponse> callback) {
        Call<CommenResponse> call = service.setManager(token, uid, gid);
        call.enqueue(callback);
    }

    public void getAnnList(String token, String groupid, int begin, int limit, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = service.getAnnList(token, groupid, begin, limit);
        call.enqueue(callback);
    }

    public void delAnn(String token, String id, Callback<CommenResponse> callback) {
        Call<CommenResponse> call = service.delAnn(token, id);
        call.enqueue(callback);
    }

    public void addAnn(String token, String text, String uid, String gid, Callback<CommenResponse> callback) {
        Call<CommenResponse> call = service.addAnn(token, text, uid, gid);
        call.enqueue(callback);
    }

    public void findMemberById(String header, String id, Callback<CommenResponse<ChatUseDetailMessage>> callback) {
        Call<CommenResponse<ChatUseDetailMessage>> call = service.findMemberById(header, id);
        call.enqueue(callback);
    }

    public void getSnsMemberByGroupId(String header, String groupId, Callback<CommenResponse<List<ChatUserGroupDetailsMessage>>> callback) {
        Call<CommenResponse<List<ChatUserGroupDetailsMessage>>> call = service.getSnsMemberByGroupId(header, groupId);
        call.enqueue(callback);
    }

    public void deleteChatGroupById(String header, String id, Callback<CommenResponse> callback) {
        Call<CommenResponse> call = service.deleteChatGroupById(header, id);
        call.enqueue(callback);
    }

    /**
     * 初始化retrofit
     */
    Retrofit retrofit;
    ChatInterfaceService service;
    private static HttpReuqests mInstance;

    public HttpReuqests() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseConst.Socket_URL)
                .client(OkHttpManager.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        service = retrofit.create(ChatInterfaceService.class);

    }

    public static HttpReuqests getInstance() {
        if (mInstance == null) {
            synchronized (HttpReuqests.class) {
                if (mInstance == null) {
                    mInstance = new HttpReuqests();
                }
            }
        }
        return mInstance;
    }

    public static void resetRequest() {
        mInstance = null;
    }

    private <T> T create(Class<T> clz) {
        return retrofit.create(clz);
    }


}
