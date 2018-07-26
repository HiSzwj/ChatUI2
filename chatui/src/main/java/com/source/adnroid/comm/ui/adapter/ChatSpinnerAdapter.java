package com.source.adnroid.comm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.entity.ChatTypeItem;


import java.util.List;

/**
 * Created by zzw on 2018/4/12.
 */

public class ChatSpinnerAdapter extends BaseAdapter{
    List<ChatTypeItem> list;
    private Context mContext;

    public ChatSpinnerAdapter(Context pContext, List<ChatTypeItem> list) {
        this.mContext = pContext;
        this.list = list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(R.layout.item_spinner_chat_type, null);
        if(convertView!=null)
        {
            TextView item=(TextView)convertView.findViewById(R.id.chat_spinner_item);
            item.setText(list.get(position).getName());
        }
        return convertView;
    }
}
