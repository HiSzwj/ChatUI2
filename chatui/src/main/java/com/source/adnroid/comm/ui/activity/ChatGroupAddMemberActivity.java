package com.source.adnroid.comm.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.adapter.AddMemberListAdapter;
import com.source.adnroid.comm.ui.chatview.LoadingView;
import com.source.adnroid.comm.ui.entity.AddMemberHospital;
import com.source.adnroid.comm.ui.entity.AddMemberProvince;
import com.source.adnroid.comm.ui.entity.AddMemberInfo;
import com.source.adnroid.comm.ui.entity.CommenResponse;
import com.source.adnroid.comm.ui.interfaces.OnItemClickListener;
import com.source.adnroid.comm.ui.net.HttpReuqests;
import com.source.android.chatsocket.entity.SocketConst;
import com.source.android.chatsocket.messages.AddMemberEvent;
import com.source.android.chatsocket.messages.ChatGroupPeopleChangeCallBackMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatGroupAddMemberActivity extends ChatBaseActivity implements OnItemClickListener {
    private String TAG = "ChatGroupAddMemberActivity";
    private ImageView mBackImageView;

    private Spinner mProvinceSpinner;
    private Spinner mSiteSpinner;
    private LoadingView loadView;
    private SwipeToLoadLayout mMemberSwipeToLoadLayout;
    private RecyclerView mMemberRecyclerView;
    private LinearLayoutManager mMemberLayoutManager;
    private boolean isLoading;
    private int mMemberTotal = 0;
    private int mMemberStart = 0;
    private AddMemberListAdapter mMemberListAdapter;
    private Handler mMemberHandler = new Handler();
    MyHandler handler = new MyHandler(this);
    public ArrayList<AddMemberInfo> mMemberItem = new ArrayList<AddMemberInfo>();


    private List<AddMemberProvince> mProvinceData;
    private List<String> mProvinceList = new ArrayList<String>();

    private List<AddMemberHospital> mHospitalData;
    private List<String> mHospitalList = new ArrayList<String>();

    /* String token = "3983f653137b45f5b641c6f120bd33d0";*/
    String mGroupid;
    String mSiteid = "";

    public ChatGroupAddMemberActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_add_member);
        EventBus.getDefault().register(this);
        initIntent();
        init();
        getProvinceData();
    }

    private void initIntent() {
        mGroupid = roomId;
        Log.i(TAG, "mGroupid==>" + mGroupid + "  token==>" + token);
    }

    private void init() {
        loadView = findViewById(R.id.mload);
        mBackImageView = findViewById(R.id.BackImageView);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mProvinceSpinner = (Spinner) findViewById(R.id.ProvinceSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_add_member_spinner, mProvinceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvinceSpinner.setAdapter(adapter);
        mProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.i("test", "selected:" + mProvinceData.get(pos).getItemcode());
                String code = mProvinceData.get(pos).getItemcode();
                if (!TextUtils.isEmpty(code)) {
                    getHospitalList(mProvinceData.get(pos).getItemcode());
                } else {
                    mHospitalList.clear();
                    mHospitalList.add("暂无医院");
                    ((ArrayAdapter) mSiteSpinner.getAdapter()).notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        mSiteSpinner = (Spinner) findViewById(R.id.SiteSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.item_add_member_spinner, mHospitalList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSiteSpinner.setAdapter(adapter2);
        mSiteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mMemberItem.clear();
                mMemberTotal = 0;
                mMemberStart = 0;
                mMemberListAdapter.notifyDataSetChanged();
                mMemberSwipeToLoadLayout.setRefreshing(false);
                mMemberListAdapter.notifyItemRemoved(mMemberListAdapter.getItemCount());
                if (mHospitalData.size() != 0) {
                    String siteid = mHospitalData.get(pos).getSiteid();
                    getMemberListData(siteid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        mMemberSwipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.add_member_Layout);
        mMemberRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        mMemberLayoutManager = new LinearLayoutManager(this);
        mMemberRecyclerView.setLayoutManager(mMemberLayoutManager);
        mMemberListAdapter = new AddMemberListAdapter(this, mMemberItem);
        mMemberRecyclerView.setAdapter(mMemberListAdapter);

        mMemberRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int Alliancetate) {
                super.onScrollStateChanged(recyclerView, Alliancetate);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = mMemberLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == mMemberListAdapter.getItemCount()) {
                    if (mMemberTotal > mMemberItem.size()) {
                        if (!isLoading) {
                            isLoading = true;
                            mMemberHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getMemberListData(mSiteid);
                                    Log.d("test", "load more completed");
                                    isLoading = false;
                                }
                            }, 300);
                        }
                    }
                }
            }
        });

        mMemberListAdapter.setOnItemClickListener(this);
    }

    private void getProvinceData() {
        loadView.setVisibility(View.VISIBLE);
        //mobile/mobile/snsgroupMember/province
        HttpReuqests.getInstance().getAddMemberProvince(token, new Callback<CommenResponse<List<AddMemberProvince>>>() {
            @Override
            public void onResponse(Call<CommenResponse<List<AddMemberProvince>>> call, Response<CommenResponse<List<AddMemberProvince>>> response) {
                CommenResponse<List<AddMemberProvince>> comm = response.body();
                int resultCode = comm.getResultCode();
                //System.out.println(comm.getResultCode()+comm.getData().get(0).getItemid());
                if (resultCode == 200) {
                    Log.i("test", "getProvince 200");
                    mProvinceData = comm.getData();
                    for (int i = 0; i < mProvinceData.size(); i++) {
                        mProvinceList.add(mProvinceData.get(i).getItemname());
                    }
                    ((ArrayAdapter) mProvinceSpinner.getAdapter()).notifyDataSetChanged();
                    mHospitalList.clear();
                    ((ArrayAdapter) mSiteSpinner.getAdapter()).notifyDataSetChanged();
                } else if (resultCode == 401) {

                } else {

                }
                loadView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<CommenResponse<List<AddMemberProvince>>> call, Throwable t) {
                loadView.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void getHospitalList(String id) {
        loadView.setVisibility(View.VISIBLE);
        //mobile/snsgroupMember/getSiteByPro
        Log.i("test", "getHospitalList for " + id);
        HttpReuqests.getInstance().getAddMemberHospital(token, id, new Callback<CommenResponse<List<AddMemberHospital>>>() {
            @Override
            public void onResponse(Call<CommenResponse<List<AddMemberHospital>>> call, Response<CommenResponse<List<AddMemberHospital>>> response) {
                CommenResponse<List<AddMemberHospital>> comm = response.body();
                int resultCode = comm.getResultCode();
                //System.out.println(comm.getResultCode()+comm.getData().get(0).getItemid());
                if (resultCode == 200) {
                    Log.i("test", "getHospitalList 200");
                    mHospitalData = comm.getData();
                    mHospitalList.clear();
                    if (mHospitalData.size() > 0) {
                        for (int i = 0; i < mHospitalData.size(); i++) {
                            Log.i("test", mHospitalData.get(i).getSitename());
                            mHospitalList.add(mHospitalData.get(i).getSitename());
                        }
                    } else {
                        mHospitalList.add("暂无医院");
                    }
                    ((ArrayAdapter) mSiteSpinner.getAdapter()).notifyDataSetChanged();
                } else if (resultCode == 401) {

                } else {
                }
                loadView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<CommenResponse<List<AddMemberHospital>>> call, Throwable t) {
                loadView.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void getMemberListData(String siteid) {
        loadView.setVisibility(View.VISIBLE);
        //mobile/snsgroupMember/getUserListBySiteId
        mSiteid = siteid;
        HttpReuqests.getInstance().getAddMemberList(token, mSiteid, mGroupid, mMemberStart, 5, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.e("test", result);
                    JSONObject j = new JSONObject(result);
                    if (j.has("resultCode")) {
                        String resultCode = j.getString("resultCode");
                        if (resultCode.equals("200")) {
                            if (j.has("data")) {
                                String data = j.getString("data");
                                JSONObject k = new JSONObject(data);
                                int total = 0;
                                if (k.has("total")) {
                                    total = k.getInt("total");
                                    mMemberTotal = total;
                                }
                                if (k.has("data")) {
                                    String array = k.getString("data");
                                    JSONArray AllianceArray = new JSONArray(array);
                                    for (int i = 0; i < AllianceArray.length(); i++) {
                                        JSONObject l = AllianceArray.getJSONObject(i);
                                        AddMemberInfo item = new AddMemberInfo();
                                        item.total = total;
                                        if (l.has("realname")) {
                                            item.realname = l.getString("realname");
                                        }
                                        if (l.has("photo")) {
                                            item.photo = l.getString("photo");
                                        }
                                        if (l.has("sitename")) {
                                            item.sitename = l.getString("sitename");
                                        }
                                        if (l.has("mobile")) {
                                            item.mobile = l.getString("mobile");
                                        }
                                        if (l.has("jobtitle")) {
                                            item.jobtitle = l.getString("jobtitle");
                                        }
                                        if (l.has("userid")) {
                                            item.userid = l.getString("userid");
                                        }
                                        mMemberItem.add(item);
                                    }
                                    mMemberStart = mMemberStart + AllianceArray.length();
                                    mMemberListAdapter.notifyDataSetChanged();
                                    mMemberSwipeToLoadLayout.setRefreshing(false);
                                    mMemberListAdapter.notifyItemRemoved(mMemberListAdapter.getItemCount());
                                }
                            }
                        } else if (resultCode.equals("401")) {

                        } else {

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadView.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onClick(String type, String position) {
        Log.i(TAG, "type==>" + type + " position==>" + position + " mMemberItemsize()==>" + mMemberItem.size() + " userid==>" + mMemberItem.get(Integer.valueOf(position)).userid);
        if (type.equals("add")) {
            EventBus.getDefault().post(new AddMemberEvent(SocketConst.CHAT_GROUP_ADD_PEOPLE, roomId, mMemberItem.get(Integer.valueOf(position)).userid));
        }
    }

    //人员变动提示
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(ChatGroupPeopleChangeCallBackMessage chatManager) {
        Log.d(TAG, "从socket中获取到的信息人员变动回掉==" + chatManager.getResult() + "chatManager userid==" + chatManager.getUserId() + "mMemberItem size" + mMemberItem.size());
        if (!chatManager.getType().equals("inviteJoinRoom")) {
            return;
        }
        if (chatManager.getResult() == 1) {
            Log.d(TAG, "添加成功");
            Message message = new Message();
            message.what = 1;
            message.obj = chatManager;
            handler.sendMessage(message);
        } else if (chatManager.getResult() == 0) {
            Log.d(TAG, "添加失败");
            handler.sendEmptyMessage(0);
        }
    }

    public void removePeopleChangePosition(ChatGroupPeopleChangeCallBackMessage chatManager) {
        for (int i = 0; i < mMemberItem.size(); i++) {
            Log.i(TAG, "人员==>" + mMemberItem.get(i).userid);
            if (chatManager.getUserId() == mMemberItem.get(i).userid) {
                Log.i(TAG, "人员移除==>" + i);
                mMemberListAdapter.notifyItemRemoved(i - 1);
                mMemberItem.remove(i);
                mMemberListAdapter.notifyDataSetChanged();
            }
        }
    }

    static class MyHandler extends Handler {
        private final WeakReference<ChatGroupAddMemberActivity> mActivity;

        public MyHandler(ChatGroupAddMemberActivity mactivity) {
            mActivity = new WeakReference<ChatGroupAddMemberActivity>(mactivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity == null) {
                return;
            }
            if (msg.what == 1) {
                Toast.makeText(mActivity.get(), "添加成功", Toast.LENGTH_SHORT).show();
                mActivity.get().removePeopleChangePosition((ChatGroupPeopleChangeCallBackMessage) msg.obj);
            } else if (msg.what == 0) {
                Toast.makeText(mActivity.get(), "添加失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onLongClick(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
