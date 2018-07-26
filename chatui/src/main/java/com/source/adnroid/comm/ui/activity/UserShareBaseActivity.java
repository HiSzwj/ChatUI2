package com.source.adnroid.comm.ui.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.chatview.MarqueTextView;
import com.source.adnroid.comm.ui.entity.ChatTypeItem;
import com.source.adnroid.comm.ui.entity.DataBean;

import com.source.android.chatsocket.entity.SocketConst;
import com.source.android.chatsocket.messages.NetReconnectMessage;
import com.source.android.chatsocket.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

//TODO 抽出分享类 超类
public abstract class UserShareBaseActivity extends ChatBaseActivity {
    public  String TAG="UserShareBaseActivity";
    SwipeToLoadLayout swipeToLoadLayout;//刷新 加载 控件
    RecyclerView recyclerView;//聊天信息控件
    LinearLayoutManager linearLayoutManager;
    ArrayList<DataBean> list = new ArrayList<DataBean>();
    //顶部选择按钮
    public TableRow mNewsTableRow;
    public View mTabView[];
    public int selectedType;
    public int mTabSize;
    public View mTabSelectedView[];
    public int mTabAddNum = 0;
    public ArrayList<ChatTypeItem> mTabItem = new ArrayList<ChatTypeItem>();
    TextView chatNetStatue;//socket状态
    TextView chatNetReconnect;//重连

    public  void initBaseView(){
        chatNetStatue=findViewById(R.id.chat_net_statue);
        chatNetReconnect=findViewById(R.id.chat_net_reconnect);
        chatNetReconnect.setOnClickListener(new onViewClickListener());
        titleName=findViewById(R.id.chat_title_name);
        mNewsTableRow=findViewById(R.id.NewsTableRow);
        swipeToLoadLayout = findViewById(R.id.swipeToLoadLayout);
        recyclerView = findViewById(R.id.swipe_target);
        //为recyclerView设置布局管理器
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.devider_shape));
        recyclerView.addItemDecoration(divider);
    }
    //加载历史状态
    public void getSPMessage(){
        int registerStatus= (int) SPUtils.get(this, SocketConst.Socket_Register_STATUS,0);
        changeNetReconnectStatus(registerStatus);
        int socketStatus= (int) SPUtils.get(this, SocketConst.Socket_STATUS,0);
        changeNetStatus(socketStatus);
    }
    class onViewClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.chat_net_reconnect){
                Log.i(TAG,"点击重连");
                EventBus.getDefault().post(new NetReconnectMessage(2));
            }
        }
    }
    //改变netSocket显示状态
    public void changeNetStatus(int netStatue){
        if(netStatue==1){
            if(chatNetStatue.getVisibility()==chatNetStatue.VISIBLE){
                chatNetStatue.setVisibility(View.GONE);
            }
        }else {
            if(chatNetStatue.getVisibility()==chatNetStatue.GONE){
                chatNetStatue.setVisibility(View.VISIBLE);
            }
            changeNetReconnectStatus(1);
        }
    }
    //改变重连按钮状态
    public void changeNetReconnectStatus(int netReconnectStatue){
        if(netReconnectStatue==1){
            if(chatNetReconnect.getVisibility()==chatNetReconnect.VISIBLE){
                chatNetReconnect.setVisibility(View.GONE);
            }
        }else if(netReconnectStatue==0){
            if(chatNetReconnect.getVisibility()==chatNetReconnect.GONE){
                chatNetReconnect.setVisibility(View.VISIBLE);
            }
        }
    }
    public abstract void initAdapterAndListener();

    public void setTabData() {
        mNewsTableRow.removeAllViews();
        mTabView = new View[mTabSize];
        mTabSelectedView = new View[mTabSize];
        for (int i = 0; i < mTabSize; i++) {
            TabView item = new TabView();
            if (mTabSize == 1) {
                item.mTabSelectedView.setVisibility(View.VISIBLE);
            } else {
                if (i == 0) {
                    item.mTabSelectedView.setVisibility(View.VISIBLE);
                } else if (i == mTabSize - 1) {
                    item.mTabSelectedView.setVisibility(View.INVISIBLE);
                } else {
                    item.mTabSelectedView.setVisibility(View.INVISIBLE);
                }
            }
            item.mTabText.setText(mTabItem.get(i).getName());
            mTabView[i] = item.mTabView;
            mTabSelectedView[i] = item.mTabSelectedView;
            mNewsTableRow.addView(mTabView[i]);
        }

        mTabAddNum = 0;
        for (int i = 0; i < mTabSize; i++) {
            mTabView[i].setTag(mTabAddNum);
            mTabAddNum++;
            mTabView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = (Integer) v.getTag();
                    selectedType=n;
                    //设置重新加载
                    selectedNewsViewPager(n);
                    OnTabItemClick();
                }
            });

        }
    }
    public  abstract void OnTabItemClick();
    public void selectedNewsViewPager(int n) {
        View a = mTabSelectedView[n];
        if (mTabSize == 1) {
            a.setVisibility(View.VISIBLE);
        } else {
            if (n == 0) {
                initializeNewsTab();
                a.setVisibility(View.VISIBLE);
            } else if (n == mTabSize - 1) {
                initializeNewsTab();
                a.setVisibility(View.VISIBLE);
            } else {
                initializeNewsTab();
                a.setVisibility(View.VISIBLE);
            }
        }
    }
    public void initializeNewsTab() {
        if (mTabSize > 1) {
            for (int i = 0; i < mTabSize; i++) {
                if (i == 0) {
                    mTabSelectedView[i].setVisibility(View.INVISIBLE);
                } else if (i == mTabSize - 1) {
                    mTabSelectedView[i].setVisibility(View.INVISIBLE);
                } else {
                    mTabSelectedView[i].setVisibility(View.INVISIBLE);
                }
            }
        }
    }
    public class TabView {
        public View mTabView;
        public MarqueTextView mTabText;
        public View mTabSelectedView;

        public TabView() {
            mTabView = LayoutInflater.from(UserShareBaseActivity.this).inflate(
                    R.layout.chat_news_tab_item, null);
            viewfinder();
        }

        public void viewfinder() {
            mTabText =  mTabView.findViewById(R.id.chatTabTextView);

            mTabSelectedView = mTabView.findViewById(R.id.chatTabSelectedView);
        }
    }
    //设置标题栏的方法
    public void setTitleName(String text){
        titleName.setText(text);
    }

}
