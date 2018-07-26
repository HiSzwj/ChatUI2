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

import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.entity.ChatGroupAnn;
import com.source.adnroid.comm.ui.interfaces.OnItemClickListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AnnListAdapter extends Adapter<ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_FINISH = 2;
    private Context context;
    private ArrayList<ChatGroupAnn> data;

    public AnnListAdapter(Context context, ArrayList<ChatGroupAnn> data) {
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
                return TYPE_FINISH;
            } else {
                return TYPE_FOOTER;
            }
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_group_ann_item, parent,
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
            if (((ItemViewHolder) holder).time != null) {
                String date = transferLongToDate(Long.parseLong(data.get(position).addTime));
                ((ItemViewHolder) holder).time.setText(date);
            }
            if (((ItemViewHolder) holder).ann != null) {
                ((ItemViewHolder) holder).ann.setText(data.get(position).noticeText);
            }
            ((ItemViewHolder) holder).del.setVisibility(View.GONE);
            ((ItemViewHolder) holder).edit.setVisibility(View.GONE);
            int role = data.get(position).role;
            if (role == 0 || role == 2) {
                ((ItemViewHolder) holder).del.setVisibility(View.VISIBLE);
               // ((ItemViewHolder) holder).edit.setVisibility(View.VISIBLE);
            }
            ((ItemViewHolder) holder).del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick("del", "" + position);
                }
            });
            ((ItemViewHolder) holder).edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick("edit", "" + position);
                }
            });
        }
    }

    /**
     * 把long 转换成 日期 再转换成String类型
     */
    public String transferLongToDate(Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    static class ItemViewHolder extends ViewHolder {

        TextView time;
        TextView ann;
        ImageView del;
        ImageView edit;

        public ItemViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.TimeTextView);
            ann = view.findViewById(R.id.AnnTextView);
            del = view.findViewById(R.id.DelImageView);
            edit = view.findViewById(R.id.EditImageView);
        }
    }

    static class FootViewHolder extends ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

}