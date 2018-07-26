package com.source.adnroid.comm.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bsc.chat.commenbase.BaseConst;
import com.bumptech.glide.Glide;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.activity.ChatGroupDetailsActivity;
import com.source.adnroid.comm.ui.entity.ChatGroupMember;
import com.source.adnroid.comm.ui.entity.Const;
import com.source.adnroid.comm.ui.interfaces.OnItemClickListener;


import java.util.List;

/**
 * Created by Feng on 2018/4/24.
 */

//成员列表适配器
public class MemberListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ChatGroupMember> mMemberData;
    private int mMyRole = 1;
    private Context mContext;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MemberListAdapter(Context context, List<ChatGroupMember> memberData) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMemberData = memberData;
    }

    public void setMyRole(int role) {
        mMyRole = role;
    }

    @Override
    public int getCount() {
        //return mArticleListItem.size();
        if (mMemberData != null) {
            return mMemberData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_chat_group_member, null);
            viewHolder.memberLayout = convertView.findViewById(R.id.memberLayout);
            viewHolder.head = convertView.findViewById(R.id.UserHeadImageView);
            viewHolder.name = convertView.findViewById(R.id.NameTextView);
            viewHolder.phone = convertView.findViewById(R.id.PhoneTextView);
            viewHolder.hospital = convertView.findViewById(R.id.HospitalTextView);
            viewHolder.del = convertView.findViewById(R.id.DelTextView);
            viewHolder.manager = convertView.findViewById(R.id.ManagerTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.del.setVisibility(View.GONE);
        viewHolder.manager.setVisibility(View.GONE);
        int role = mMemberData.get(position).getMemberRole();
        if (mMyRole == 0) {
            //当前权限为群主
            if (role == 0) {
                //item权限为群主
                //隐藏删除
                viewHolder.del.setVisibility(View.GONE);
                //隐藏管理
                viewHolder.manager.setVisibility(View.GONE);
            } else if (role == 2) {
                //item权限为管理员
                //显示删除
                viewHolder.del.setVisibility(View.VISIBLE);
                //显示管理
                viewHolder.manager.setVisibility(View.VISIBLE);
                viewHolder.manager.setText("取消管理");
            } else {
                //item权限为成员
                //显示删除
                viewHolder.del.setVisibility(View.VISIBLE);
                //显示管理
                viewHolder.manager.setVisibility(View.VISIBLE);
                viewHolder.manager.setText("设置管理");
            }
        } else if (mMyRole == 2) {
            //当前权限为管理员
            if (role == 0) {
                //item权限为群主
                //隐藏删除
                viewHolder.del.setVisibility(View.GONE);
                //隐藏管理
                viewHolder.manager.setVisibility(View.GONE);
            } else if (role == 2) {
                //item权限为管理员
                //隐藏删除
                viewHolder.del.setVisibility(View.GONE);
                //隐藏管理
                viewHolder.manager.setVisibility(View.GONE);
            } else {
                //item权限为成员
                //显示删除
                viewHolder.del.setVisibility(View.VISIBLE);
                //隐藏管理
                viewHolder.manager.setVisibility(View.GONE);
            }
        } else {
            //当前权限为成员
        }
        Glide.with(mContext).load(BaseConst.CHAT_PIC_URL + mMemberData.get(position).getPhoto()).into(viewHolder.head);
        viewHolder.name.setText(mMemberData.get(position).getMemberName());
        viewHolder.phone.setText(mMemberData.get(position).getMobile());
        viewHolder.hospital.setText(mMemberData.get(position).getSiteName() + " " + mMemberData.get(position).getJobtitle());

        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick("delmember", "" + position);
            }
        });
        viewHolder.manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick("manager", "" + position);
            }
        });
        viewHolder.memberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick("memberitem", "" + position);
            }
        });
        return convertView;
    }

    //成员列表ViewHolder
    private static class ViewHolder {
        RelativeLayout memberLayout;
        ImageView head;
        TextView name;
        TextView phone;
        TextView hospital;
        TextView del;
        TextView manager;
    }
}