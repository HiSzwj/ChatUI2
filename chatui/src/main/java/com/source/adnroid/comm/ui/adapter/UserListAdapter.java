package com.source.adnroid.comm.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.entity.DataBean;
import com.source.adnroid.comm.ui.entity.MsgTypeEnum;
import com.source.adnroid.comm.ui.interfaces.OnUserListItemClickListener;
import com.source.android.chatsocket.entity.MsgEntity;


import java.util.ArrayList;

/**
 * Created by zzw on 2018/4/3.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{
    private String  TAG="UserListAdapter";
    private ArrayList<DataBean> mData;
    OnUserListItemClickListener onUserListItemClickListener;
    public void setOnItemClickListener(OnUserListItemClickListener onUserListItemClickListener ){
        this.onUserListItemClickListener=onUserListItemClickListener;
    }
    public UserListAdapter(ArrayList<DataBean> data) {
        this.mData = data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userlist, parent, false);
        // 实例化viewholder
        UserListAdapter.ViewHolder viewHolder = new UserListAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.groupName.setText(mData.get(position).getName());
        holder.groupOwnerName.setText(mData.get(position).getRealName());
        holder.groupUserNum.setText(mData.get(position).getCount());
        holder.nowNewCcontent.setText(mData.get(position).getNotice());
        MsgEntity msgEntity=null;
        try {
            Log.i(TAG,"Data====>"+mData.get(position).getMessage());
            msgEntity= JSONObject.parseObject(mData.get(position).getMessage(), MsgEntity.class);
        }catch (Exception e){
            holder.nowSayContent.setText("无法解析的消息");
        }
        if (msgEntity!=null){
            if (msgEntity.getMessage().getType().equals(MsgTypeEnum.TEXT_MSG.getType())){
                holder.nowSayContent.setText(msgEntity.getMessage().getMsg());
            }else if(msgEntity.getMessage().getType().equals(MsgTypeEnum.PATIENT_MSG.getType())){
                holder.nowSayContent.setText("病例分享");
                holder.patientImg.setVisibility(View.VISIBLE);
            }else if(msgEntity.getMessage().getType().equals(MsgTypeEnum.IMAGE_MSG.getType())){
                holder.nowSayContent.setText("图片");
            }else if(msgEntity.getMessage().getType().equals(MsgTypeEnum.VIDEO_MSG.getType())){
                holder.nowSayContent.setText("视频");
            }
        }

        holder.linearLayout.setTag(mData.get(position).getId());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将点击位置的房间Id传递出去
                onUserListItemClickListener.onClick(mData.get(position).getId(),mData.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView groupName;
        TextView groupOwnerName;
        TextView groupUserNum;
        TextView nowNewCcontent;
        TextView nowSayContent;
        ImageView patientImg;
        public ViewHolder(View itemView) {
            super(itemView);
            groupName =itemView.findViewById(R.id.group_name);
            groupOwnerName=itemView.findViewById(R.id.group_owner_name);
            groupUserNum =itemView.findViewById(R.id.group_user_num);
            nowNewCcontent =itemView.findViewById(R.id.now_news_content);
            nowSayContent=itemView.findViewById(R.id.now_say_content);
            linearLayout=itemView.findViewById(R.id.group_content);
            patientImg=itemView.findViewById(R.id.patient_img);

        }
    }
}
