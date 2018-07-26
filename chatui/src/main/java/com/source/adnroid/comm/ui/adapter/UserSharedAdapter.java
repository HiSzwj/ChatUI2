package com.source.adnroid.comm.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.entity.DataBean;
import com.source.adnroid.comm.ui.entity.MsgTypeEnum;
import com.source.adnroid.comm.ui.interfaces.OnUserListItemClickListener;
import com.source.android.chatsocket.entity.MsgEntity;

import java.util.ArrayList;

public class UserSharedAdapter extends RecyclerView.Adapter<UserSharedAdapter.ViewHolder> {
    private String TAG = "UserListAdapter";
    private ArrayList<DataBean> mData;
    OnUserListItemClickListener onUserListItemClickListener;

    public void setOnItemClickListener(OnUserListItemClickListener onUserListItemClickListener) {
        this.onUserListItemClickListener = onUserListItemClickListener;
    }

    public UserSharedAdapter(ArrayList<DataBean> data) {
        this.mData = data;
    }

    @Override
    public UserSharedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usershared, parent, false);
        // 实例化viewholder
        UserSharedAdapter.ViewHolder viewHolder = new UserSharedAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final UserSharedAdapter.ViewHolder holder, final int position) {
        holder.groupName.setText(mData.get(position).getName());
        holder.linearLayout.setTag(mData.get(position).getId());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将点击位置的房间Id传递出去
                onUserListItemClickListener.onClick(mData.get(position).getId(), mData.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView groupName;

        public ViewHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_name);
            linearLayout = itemView.findViewById(R.id.group_content);

        }
    }
}
