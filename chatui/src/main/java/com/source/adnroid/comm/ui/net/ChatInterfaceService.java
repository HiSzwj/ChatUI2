package com.source.adnroid.comm.ui.net;

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
import com.source.adnroid.comm.ui.entity.PatientEntity;
import com.source.adnroid.comm.ui.entity.RoomEntity;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zzw on 2018/4/8.
 */

public interface ChatInterfaceService {
    //上传文件
    //上传文件的interface
    @Multipart
    @POST("mobile/attachment/upload")
    Observable<ResponseBody> uploadFile(@Header("Authorization") String header, @Part MultipartBody.Part file, @Part("type") String type);

    //创建聊天室
    @FormUrlEncoded
    @POST("mobile/snsgroup/doAdd")
    Call<CommenResponse> doAddChat(@Header("Authorization") String header, @Field("groupType") String groupType, @Field("name") String name, @Field("description") String description, @Field("createUserId") String createUserId);

    //根据类型查询聊天室列表（查所有）
    @GET("mobile/snsgroup/getSnsGroupListByType")
    Call<RoomEntity> getSnsGroupListByType(@Header("Authorization") String header);

    //根据类型查询聊天室列表（查类别）
    @GET("mobile/snsgroup/getSnsGroupListByType")
    Call<RoomEntity> getSnsGroupListByType(@Header("Authorization") String header, @Query("groupType") String groupType, @Query("userId") String userId);

    //获取病例信息
    @POST("mobile/clinic/{TelemedicineInfoId}")
    Call<PatientEntity> getTelemedicineInfo(@Header("Authorization") String header, @Path("TelemedicineInfoId") String TelemedicineInfoId);

    //获取所有聊天室类别列表
    @GET("mobile/snsgroup/getTypeList")
    Call<ChatTypeEntity> getTypeListInfo(@Header("Authorization") String header);

    //获取已加入聊天室类别列表
    @GET("mobile/snsgroup/getSnsGroupTypeByUserId")
    Call<ChatTypeEntity> getSnsGroupTypeByUserId(@Header("Authorization") String header, @Query("userId") String userId);

    //获取历史消息
    @GET("mobile/discuss/getSnsDiscussListByPage")
    Call<ChatHistoryMessage> getSnsDiscussListByPage(@Header("Authorization") String header, @Query("begin") int begin, @Query("limit") int limit, @Query("groupId") String groupId);

    //获取群属性
    @GET("mobile/snsgroup/findEntityById")
    Call<ChatGroupAttributes> getGroupAttributesByPage1(@Header("Authorization") String header, @Query("id") String id);

    //获取群详情成员
    @GET("mobile/snsgroupMember/getSnsMemberByGroupId")
    Call<CommenResponse<List<ChatGroupMember>>> getGroupMemberById(@Header("Authorization") String header, @Query("groupId") String id);

    //移除群成员
    @FormUrlEncoded
    @POST("mobile/snsgroupMember/delete")
    Call<CommenResponse> delMember(@Header("Authorization") String header, @Field("id") String id);

    //设置管理员
    @FormUrlEncoded
    @POST("mobile/snsgroupMember/setGroupManager")
    Call<CommenResponse> setManager(@Header("Authorization") String header, @Field("userId") String id, @Field("groupId") String gid);

    //获取群公告
    @GET("mobile/snsGroupNotice/getListByGroupId")
    Call<ResponseBody> getAnnList(@Header("Authorization") String header, @Query("groupId") String groupid, @Query("begin") int begin, @Query("limit") int limit);

    //删除群公告
    @FormUrlEncoded
    @POST("mobile/snsGroupNotice/delete")
    Call<CommenResponse> delAnn(@Header("Authorization") String header, @Field("id") String id);

    //添加群公告
    @FormUrlEncoded
    @POST("mobile/snsGroupNotice/doAdd")
    Call<CommenResponse> addAnn(@Header("Authorization") String header, @Field("noticeText") String text, @Field("addUserId") String uid, @Field("groupId") String gid);

    //获取群管理员
    @GET("mobile/snsgroupMember/getGroupManagerList")
    Call<CommenResponse<List<ChatGroupManager>>> getGroupManagerById(@Header("Authorization") String header, @Query("groupId") String id);

    //获取添加成员时所需省份
    @GET("mobile/snsgroupMember/province")
    Call<CommenResponse<List<AddMemberProvince>>> getAddMemberProvince(@Header("Authorization") String header);

    //获取添加成员时省份下属医院
    @GET("mobile/snsgroupMember/getSiteByPro")
    Call<CommenResponse<List<AddMemberHospital>>> getAddMemberHospital(@Header("Authorization") String header, @Query("provSelect") String id);

    //获取添加成员时医院下属人员
    @GET("mobile/snsgroupMember/getUserListBySiteId")
    Call<ResponseBody> getAddMemberList(@Header("Authorization") String header, @Query("siteid") String siteid, @Query("groupid") String groupid, @Query("begin") int begin, @Query("limit") int limit);

    //根据用户id获取用户详情
    @GET("mobile/snsgroupMember/findMemberById")
    Call<CommenResponse<ChatUseDetailMessage>> findMemberById(@Header("Authorization") String header, @Query("id") String id);

    //获取讨论组groupid获取用户组详情
    @GET("mobile/snsgroupMember/getSnsMemberByGroupId")
    Call<CommenResponse<List<ChatUserGroupDetailsMessage>>> getSnsMemberByGroupId(@Header("Authorization") String header, @Query("groupId") String groupId);

    //讨论组删除
    @FormUrlEncoded
    @POST("mobile/snsgroup/doDelete")
    Call<CommenResponse> deleteChatGroupById(@Header("Authorization") String header, @Field("id") String id);
}
