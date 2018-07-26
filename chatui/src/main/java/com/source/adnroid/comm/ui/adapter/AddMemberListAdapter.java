package com.source.adnroid.comm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsc.chat.commenbase.BaseConst;
import com.bumptech.glide.Glide;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.entity.AddMemberInfo;
import com.source.adnroid.comm.ui.interfaces.OnItemClickListener;


import java.util.ArrayList;

public class AddMemberListAdapter extends Adapter<ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_FINISH = 2;
    private Context context;
    private ArrayList<AddMemberInfo> data;

    public AddMemberListAdapter(Context context, ArrayList<AddMemberInfo> data) {
        this.context = context;
        this.data = data;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            if (data.size() > 0 & position == data.get(0).total) {
                Log.i("test","TYPE_FINISH");
                return TYPE_FINISH;
            } else {
                Log.i("test","TYPE_FOOTER");
                return TYPE_FOOTER;
            }
        } else {
            Log.i("test","TYPE_ITEM");
            return TYPE_ITEM;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_group_add_member, parent,
                    false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_add_member_foot, parent,
                    false);
            return new FootViewHolder(view);
        } else {
            //TYPE_FINISH
            View view = new View(context);
            return new FootViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            if (((ItemViewHolder) holder).head != null) {
                Glide.with(context).load(BaseConst.CHAT_PIC_URL + data.get(position).photo).into(((ItemViewHolder) holder).head);
            }
            if (((ItemViewHolder) holder).name != null) {
                ((ItemViewHolder) holder).name.setText(data.get(position).realname);
            }
            if (((ItemViewHolder) holder).phone != null) {
                ((ItemViewHolder) holder).phone.setText(data.get(position).mobile);
            }
            if (((ItemViewHolder) holder).hospital != null) {
                ((ItemViewHolder) holder).hospital.setText(data.get(position).sitename + " " + data.get(position).jobtitle);
            }
            ((ItemViewHolder) holder).hospitalAddMemTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick("add",""+position);
                }
            });

        }
    }

    static class ItemViewHolder extends ViewHolder {

        ImageView head;
        TextView name;
        TextView phone;
        TextView hospital;
        TextView hospitalAddMemTv;

        public ItemViewHolder(View view) {
            super(view);
            head = view.findViewById(R.id.UserHeadImageView);
            name =  view.findViewById(R.id.NameTextView);
            phone =  view.findViewById(R.id.PhoneTextView);
            hospital =  view.findViewById(R.id.HospitalTextView);
            hospitalAddMemTv=view.findViewById(R.id.HospitalAddMemTextView);
        }
    }

    static class FootViewHolder extends ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
}