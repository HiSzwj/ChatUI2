package com.source.adnroid.comm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.source.adnroid.comm.ui.R;
import com.source.adnroid.comm.ui.chatview.CircularProgressView;
import com.source.adnroid.comm.ui.entity.ChatFileEntity;
import com.source.adnroid.comm.ui.entity.MessagePatientEntity;
import com.source.adnroid.comm.ui.entity.MsgTypeEnum;
import com.source.adnroid.comm.ui.interfaces.OnItemClickListener;

import com.source.android.chatsocket.commen.BaseConst;
import com.source.android.chatsocket.entity.MsgViewEntity;

import java.util.ArrayList;

/**
 * Created by zzw on 2018/4/2.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "ChatMessageAdapter";

    private int LEFT_TEXT_MESSAGE = 1;//左侧文本消息
    private int RIGHT_TEXT_MESSAGE = 2;//右侧文本消息
    private int LEFT_PATIENT_MESSAGE = 3;//左侧病例消息
    private int RIGHT_PATIENT_MESSAGE = 4;//右侧病例消息
    private int RIGHT_IMAGE_MESSAGE = 5;//右侧图片消息
    private int LEFT_IMAGE_MESSAGE = 6;//左侧图片消息
    private int ERROR_MESSAGE = 7;//无法解析消息
    private int CHAT_MEMBER_CHANGE_MESSAGE = 8;//房间人员变动消息
    private String IMAGE_CLICK = "imageClick";//头像点击事件
    private Context context;
    private ArrayList<MsgViewEntity> mData;
    private String userId;//记录本地用户id区分左右显示
    OnItemClickListener onItemClickListener;
    RequestOptions options = new RequestOptions();//头像配置
    RequestOptions imageUpLoadingOptions = new RequestOptions();//图片上传中消息配置
    RequestOptions imageUpLoadSuceess = new RequestOptions();//图片成功消息配置
    RequestOptions imageUpLoadFailed = new RequestOptions();//图片失败消息配置

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ChatMessageAdapter(Context context, ArrayList<MsgViewEntity> data, String userId) {
        this.mData = data;
        this.userId = userId;
        this.context = context;
        options.circleCrop();
        options.placeholder(R.mipmap.chat_default_image_user);
        options.error(R.mipmap.chat_default_image_user);
        imageUpLoadingOptions.placeholder(R.mipmap.chat_up_loading);
        imageUpLoadingOptions.error(R.mipmap.chat_up_loading);
        imageUpLoadSuceess.placeholder(R.mipmap.chat_loading);
        imageUpLoadSuceess.error(R.mipmap.chat_load_failed);
        imageUpLoadFailed.placeholder(R.mipmap.chat_up_loading_failed);
        imageUpLoadFailed.error(R.mipmap.chat_up_loading_failed);
    }

    //更新数据
    public void updateData(ArrayList<MsgViewEntity> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int Flag = 0;
        Log.i(TAG, "userId=>" + userId + " mData.get(position).getFromUser()" + mData.get(position).getFrom());
        if (mData.get(position).getFrom().equals(userId)) {//右侧显示
            if (mData.get(position).getMessage().getType().equals(MsgTypeEnum.TEXT_MSG.getType())) {//消息类型判断
                Flag = RIGHT_TEXT_MESSAGE;//右侧普通文本
            } else if (mData.get(position).getMessage().getType().equals(MsgTypeEnum.PATIENT_MSG.getType())) {
                Flag = RIGHT_PATIENT_MESSAGE;//右侧病例消息
            } else if (mData.get(position).getMessage().getType().equals(MsgTypeEnum.INVITE_JOIN_ROOM.getType()) || mData.get(position).getMessage().getType().equals(MsgTypeEnum.REMOVE_FROM_ROOM.getType())) {
                Flag = CHAT_MEMBER_CHANGE_MESSAGE;//人员变动消息
            } else if (mData.get(position).getMessage().getType().equals(MsgTypeEnum.IMAGE_MSG.getType())) {
                Flag = RIGHT_IMAGE_MESSAGE;//右侧图片消息
            }
        } else {//左侧显示
            if (mData.get(position).getMessage().getType().equals(MsgTypeEnum.TEXT_MSG.getType())) {//消息类型判断
                Flag = LEFT_TEXT_MESSAGE;//左侧普通文本
            } else if (mData.get(position).getMessage().getType().equals(MsgTypeEnum.PATIENT_MSG.getType())) {//左侧病例消息
                Flag = LEFT_PATIENT_MESSAGE;//左侧病例消息
            } else if (mData.get(position).getMessage().getType().equals(MsgTypeEnum.INVITE_JOIN_ROOM.getType()) || mData.get(position).getMessage().getType().equals(MsgTypeEnum.REMOVE_FROM_ROOM.getType())) {
                Flag = CHAT_MEMBER_CHANGE_MESSAGE;//人员变动消息
            } else if (mData.get(position).getMessage().getType().equals(MsgTypeEnum.IMAGE_MSG.getType())) {
                Flag = LEFT_IMAGE_MESSAGE;//左侧图片消息
            }
        }
        return Flag;//右侧普通文本
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RIGHT_TEXT_MESSAGE) {//右侧消息布局
            return new RightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_messsage_text_right, parent, false));
        } else if (viewType == LEFT_TEXT_MESSAGE) {//左侧消息布局
            return new LeftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_text_left, parent, false));
        } else if (viewType == RIGHT_PATIENT_MESSAGE) {//右侧病例布局
            return new RightPatientViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_patient_right, parent, false));
        } else if (viewType == LEFT_PATIENT_MESSAGE) {//左侧病例布局
            return new LeftPatientViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_patient_left, parent, false));
        } else if (viewType == CHAT_MEMBER_CHANGE_MESSAGE) {//人员变动
            return new MemberChangeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_messsage_memeber_change, parent, false));
        } else if (viewType == RIGHT_IMAGE_MESSAGE) {//右侧图片消息
            return new RightImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_image_right, parent, false));
        } else if (viewType == LEFT_IMAGE_MESSAGE) {//左侧图片
            return new LeftImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_image_left, parent, false));
        }
        return new ErrorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_error, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof LeftViewHolder) {//绑定左侧文本消息
            ((LeftViewHolder) holder).chatFromMsg.setText(mData.get(position).getMessage().getMsg());
            if (!TextUtils.isEmpty(mData.get(position).getUserName())) {
                ((LeftViewHolder) holder).chatFromName.setText(mData.get(position).getUserName());
            } else {
                ((LeftViewHolder) holder).chatFromName.setText(mData.get(position).getFrom());
            }
            Glide.with(context).load(BaseConst.CHAT_PIC_URL + mData.get(position).getUserPhoto()).apply(options).into(((LeftViewHolder) holder).fromImg);
            ((LeftViewHolder) holder).fromImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(IMAGE_CLICK, mData.get(position).getFrom());
                }
            });
        } else if (holder instanceof RightViewHolder) {////绑定右侧文本消息
            ((RightViewHolder) holder).chatTomMsg.setText(mData.get(position).getMessage().getMsg());
            if (!TextUtils.isEmpty(mData.get(position).getUserName())) {
                ((RightViewHolder) holder).chatToName.setText(mData.get(position).getUserName());
            } else {
                ((RightViewHolder) holder).chatToName.setText(mData.get(position).getFrom());
            }
            if (mData.get(position).getMsgStatus().equals("0")) {//上传中
                ((RightViewHolder) holder).chatMsgStatus.setVisibility(View.VISIBLE);
                ((RightViewHolder) holder).chatDelete.setVisibility(View.INVISIBLE);
            } else if (mData.get(position).getMsgStatus().equals("1")) {//成功
                ((RightViewHolder) holder).chatMsgStatus.setVisibility(View.INVISIBLE);
                ((RightViewHolder) holder).chatDelete.setVisibility(View.INVISIBLE);
            } else if (mData.get(position).getMsgStatus().equals("2")) {//失败
                ((RightViewHolder) holder).chatMsgStatus.setVisibility(View.INVISIBLE);
                ((RightViewHolder) holder).chatDelete.setVisibility(View.VISIBLE);
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onClick("longClick", mData.get(holder.getLayoutPosition()).getId());
                        return false;
                    }
                });
            } else {
                Log.i(TAG, "右侧文本 doNothing");
            }

            Glide.with(context).load(BaseConst.CHAT_PIC_URL + mData.get(position).getUserPhoto()).apply(options).into(((RightViewHolder) holder).toImg);
            ((RightViewHolder) holder).toImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(IMAGE_CLICK, mData.get(position).getFrom());
                }
            });
        } else if (holder instanceof RightPatientViewHolder) {//绑定右侧病例消息
            final MessagePatientEntity messagePatientEntity = JSONObject.parseObject(mData.get(position).getMessage().getMsg(), MessagePatientEntity.class);
            if (!TextUtils.isEmpty(mData.get(position).getUserName())) {
                ((RightPatientViewHolder) holder).chatToName.setText(mData.get(position).getUserName());
            } else {
                ((RightPatientViewHolder) holder).chatToName.setText(mData.get(position).getFrom());
            }
            ((RightPatientViewHolder) holder).chatPatientsg.setText(messagePatientEntity.getUserMessage() + " " + messagePatientEntity.getUserSex());
            ((RightPatientViewHolder) holder).chatPatientDiagnosissg.setText(messagePatientEntity.getUserPatientMessage());
            ((RightPatientViewHolder) holder).PatientToContent.setTag(messagePatientEntity.getPatientId());
            ((RightPatientViewHolder) holder).PatientToContent.setOnClickListener(new View.OnClickListener() {
                                                                                      @Override
                                                                                      public void onClick(View v) {
                                                                                          onItemClickListener.onClick(MsgTypeEnum.PATIENT_MSG.getType(), messagePatientEntity.getPatientId());
                                                                                      }
                                                                                  }
            );
            Glide.with(context).load(BaseConst.CHAT_PIC_URL + mData.get(position).getUserPhoto()).apply(options).into(((RightPatientViewHolder) holder).toImg);
            ((RightPatientViewHolder) holder).toImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(IMAGE_CLICK, mData.get(position).getFrom());
                }
            });

        } else if (holder instanceof LeftPatientViewHolder) {//绑定左侧病例消息
            final MessagePatientEntity messagePatientEntity = JSONObject.parseObject(mData.get(position).getMessage().getMsg(), MessagePatientEntity.class);
            if (!TextUtils.isEmpty(mData.get(position).getUserName())) {
                ((LeftPatientViewHolder) holder).chatFromName.setText(mData.get(position).getUserName());
            } else {
                ((LeftPatientViewHolder) holder).chatFromName.setText(mData.get(position).getFrom());
            }
            ((LeftPatientViewHolder) holder).chatPatientsg.setText(messagePatientEntity.getUserMessage() + " " + messagePatientEntity.getUserSex());
            ((LeftPatientViewHolder) holder).chatPatientDiagnosissg.setText(messagePatientEntity.getUserPatientMessage());
            ((LeftPatientViewHolder) holder).PatientFromContent.setTag(messagePatientEntity.getPatientId());
            ((LeftPatientViewHolder) holder).PatientFromContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(MsgTypeEnum.PATIENT_MSG.getType(), messagePatientEntity.getPatientId());
                }
            });
            Glide.with(context).load(BaseConst.CHAT_PIC_URL + mData.get(position).getUserPhoto()).apply(options).into(((LeftPatientViewHolder) holder).fromImg);
            ((LeftPatientViewHolder) holder).fromImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(IMAGE_CLICK, mData.get(position).getFrom());
                }
            });
        } else if (holder instanceof ErrorViewHolder) {//错误信息 不显示
            // ((ErrorViewHolder) holder).errorTextView.setText(mData.get(position).getMessage().getMsg());
        } else if (holder instanceof MemberChangeHolder) {//人员变动
            if (mData.get(position).getMessage().getType().equals(MsgTypeEnum.INVITE_JOIN_ROOM.getType())) {
                //((MemberChangeHolder) holder).memberChageTv.setText(mData.get(position).getUserName()+"邀请 "+mData.get(position).getFrom()+" 加入房间");
                ((MemberChangeHolder) holder).memberChageTv.setText("人员变动");
            } else {
                ((MemberChangeHolder) holder).memberChageTv.setText("人员变动");
                //((MemberChangeHolder) holder).memberChageTv.setText(mData.get(position).getUserName()+" 将 "+mData.get(position).getFrom()+" 移除房间");
            }
        } else if (holder instanceof RightImageViewHolder) {//右侧图片
            ChatFileEntity chatFileEntity = null;
            try {
                chatFileEntity = JSONObject.parseObject(mData.get(position).getMessage().getMsg(), ChatFileEntity.class);
                Log.i(TAG, "chatFileEntity iname==>" + chatFileEntity.getUrl());
            } catch (Exception e) {
                Log.e(TAG, "chatFileEntity failed==>" + e.getMessage());
            }
            String url = "";
            if (chatFileEntity != null) {
                url = chatFileEntity.getUrl();
                url = convertUrl(url);
                Log.i(TAG, "chatFileEntity reclaced url==>" +BaseConst.CHAT_PIC_URL+ url);
            }
            if (!TextUtils.isEmpty(mData.get(position).getUserName())) {
                ((RightImageViewHolder) holder).chatToName.setText(mData.get(position).getUserName());
            }
            Glide.with(context).load(BaseConst.CHAT_PIC_URL + mData.get(position).getUserPhoto()).apply(options).into(((RightImageViewHolder) holder).toImg);

            //Log.i(TAG,"picurl==>"+Const.CHAT_PIC_URL + mData.get(position).getMessage().getMsg());
            if (mData.get(position).getMsgStatus().equals("0")) {
                Glide.with(context).load(BaseConst.CHAT_PIC_URL + url).apply(imageUpLoadingOptions).into(((RightImageViewHolder) holder).sourceImage);
                ((RightImageViewHolder) holder).chatMsgStatus.setVisibility(View.VISIBLE);
                ((RightImageViewHolder) holder).chatDelete.setVisibility(View.INVISIBLE);
                ((RightImageViewHolder) holder).sourceImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onClick("longClick", mData.get(holder.getLayoutPosition()).getId());
                        return false;
                    }
                });
            } else if (mData.get(position).getMsgStatus().equals("1")) {
                Glide.with(context).load(BaseConst.CHAT_PIC_URL + url).apply(imageUpLoadSuceess).into(((RightImageViewHolder) holder).sourceImage);
                ((RightImageViewHolder) holder).chatMsgStatus.setVisibility(View.INVISIBLE);
                ((RightImageViewHolder) holder).chatDelete.setVisibility(View.INVISIBLE);
            } else if (mData.get(position).getMsgStatus().equals("2")) {
                Glide.with(context).load(BaseConst.CHAT_PIC_URL + url).apply(imageUpLoadFailed).into(((RightImageViewHolder) holder).sourceImage);
                ((RightImageViewHolder) holder).chatMsgStatus.setVisibility(View.INVISIBLE);
                ((RightImageViewHolder) holder).chatDelete.setVisibility(View.VISIBLE);
                ((RightImageViewHolder) holder).sourceImage.setBackgroundResource(R.mipmap.chat_load_failed);
                ((RightImageViewHolder) holder).sourceImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onClick("longClick", mData.get(holder.getLayoutPosition()).getId());
                        return false;
                    }
                });

            } else {
                Log.i(TAG, "右侧图片 doNothing");
            }
            ((RightImageViewHolder) holder).sourceImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ChatFileEntity chatFileEntity = JSONObject.parseObject(mData.get(position).getMessage().getMsg(), ChatFileEntity.class);
                        String murl = "";
                        if (chatFileEntity != null) {
                            murl = chatFileEntity.getUrl();
                        }
                        onItemClickListener.onClick(MsgTypeEnum.IMAGE_MSG.getType(), BaseConst.CHAT_PIC_URL + murl);
                    } catch (Exception e) {
                        Log.e(TAG, "ChatFileEntity parse err");
                    }

                }
            });
            ((RightImageViewHolder) holder).toImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(IMAGE_CLICK, mData.get(position).getFrom());
                }
            });
        } else if (holder instanceof LeftImageViewHolder) {//左侧图片
            ChatFileEntity chatFileEntity = JSONObject.parseObject(mData.get(position).getMessage().getMsg(), ChatFileEntity.class);
            String url = "";
            if (chatFileEntity != null) {
                url = chatFileEntity.getUrl();
                url = convertUrl(url);
                Log.i(TAG, "chatFileEntity reclaced url==>" + url);
            }
            if (!TextUtils.isEmpty(mData.get(position).getUserName())) {
                ((LeftImageViewHolder) holder).chatFromName.setText(mData.get(position).getUserName());
            }
            Glide.with(context).load(BaseConst.CHAT_PIC_URL + mData.get(position).getUserPhoto()).apply(options).into(((LeftImageViewHolder) holder).fromImg);
            Glide.with(context).load(BaseConst.CHAT_PIC_URL + url).apply(imageUpLoadSuceess).into(((LeftImageViewHolder) holder).sourceImage);
            ((LeftImageViewHolder) holder).sourceImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ChatFileEntity chatFileEntity = JSONObject.parseObject(mData.get(position).getMessage().getMsg(), ChatFileEntity.class);
                        String murl = "";
                        if (chatFileEntity != null) {
                            murl = chatFileEntity.getUrl();
                        }
                        onItemClickListener.onClick(MsgTypeEnum.IMAGE_MSG.getType(), BaseConst.CHAT_PIC_URL + murl);
                    } catch (Exception e) {
                        Log.e(TAG, "ChatFileEntity parse err");
                    }
                }
            });
            ((LeftImageViewHolder) holder).fromImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(IMAGE_CLICK, mData.get(position).getFrom());
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    //左侧文本布局
    public class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView chatFromMsg;
        TextView chatFromName;
        ImageView fromImg;

        public LeftViewHolder(View itemView) {
            super(itemView);
            chatFromMsg = itemView.findViewById(R.id.chat_from_msg);
            chatFromName = itemView.findViewById(R.id.chat_from_name);
            fromImg = itemView.findViewById(R.id.from_img);

        }
    }

    //右侧文本布局
    public class RightViewHolder extends RecyclerView.ViewHolder {
        TextView chatTomMsg;
        TextView chatToName;
        ImageView toImg;
        CircularProgressView chatMsgStatus;
        TextView chatDelete;

        public RightViewHolder(View itemView) {
            super(itemView);
            chatTomMsg = itemView.findViewById(R.id.chat_to_msg);
            chatToName = itemView.findViewById(R.id.chat_to_name);
            toImg = itemView.findViewById(R.id.to_img);
            chatMsgStatus = itemView.findViewById(R.id.chat_msg_status);
            chatDelete = itemView.findViewById(R.id.chat_delete);
        }
    }

    //左侧病例布局
    public class LeftPatientViewHolder extends RecyclerView.ViewHolder {
        ImageView fromImg;
        TextView chatFromName;
        TextView chatPatientsg;
        TextView chatPatientDiagnosissg;//诊断消息
        LinearLayout PatientFromContent;

        public LeftPatientViewHolder(View itemView) {
            super(itemView);
            fromImg = itemView.findViewById(R.id.from_img);
            chatFromName = itemView.findViewById(R.id.chat_from_name);
            chatPatientsg = itemView.findViewById(R.id.chat_patient_msg);
            chatPatientDiagnosissg = itemView.findViewById(R.id.chat_patient_Diagnosis_msg);
            PatientFromContent = itemView.findViewById(R.id.patient_from_content);
        }
    }

    //右侧病例布局
    public class RightPatientViewHolder extends RecyclerView.ViewHolder {
        TextView chatToName;
        ImageView toImg;
        TextView chatPatientsg;
        TextView chatPatientDiagnosissg;//诊断消息
        LinearLayout PatientToContent;
        TextView chatDelete;

        public RightPatientViewHolder(View itemView) {
            super(itemView);
            toImg = itemView.findViewById(R.id.to_img);
            chatToName = itemView.findViewById(R.id.chat_to_name);
            chatPatientsg = itemView.findViewById(R.id.chat_patient_msg);
            chatPatientDiagnosissg = itemView.findViewById(R.id.chat_patient_Diagnosis_msg);
            PatientToContent = itemView.findViewById(R.id.patient_to_content);
            chatDelete = itemView.findViewById(R.id.chat_delete);
        }
    }

    //右侧图片布局
    public class RightImageViewHolder extends RecyclerView.ViewHolder {
        TextView chatToName;
        ImageView toImg;
        ImageView sourceImage;
        CircularProgressView chatMsgStatus;
        TextView chatDelete;

        public RightImageViewHolder(View itemView) {
            super(itemView);
            chatToName = itemView.findViewById(R.id.chat_to_name);
            toImg = itemView.findViewById(R.id.to_img);
            sourceImage = itemView.findViewById(R.id.chat_right_img);
            chatMsgStatus = itemView.findViewById(R.id.chat_msg_status);
            chatDelete = itemView.findViewById(R.id.chat_delete);
        }
    }

    //左侧图片布局
    public class LeftImageViewHolder extends RecyclerView.ViewHolder {
        TextView chatFromName;
        ImageView fromImg;
        ImageView sourceImage;

        public LeftImageViewHolder(View itemView) {
            super(itemView);
            chatFromName = itemView.findViewById(R.id.chat_from_name);
            fromImg = itemView.findViewById(R.id.from_img);
            sourceImage = itemView.findViewById(R.id.chat_left_img);

        }
    }

    //错误消息布局
    public class ErrorViewHolder extends RecyclerView.ViewHolder {
        TextView errorTextView;

        public ErrorViewHolder(View itemView) {
            super(itemView);
            errorTextView = itemView.findViewById(R.id.chat_error_msg);
        }
    }

    //人员变动消息布局
    public class MemberChangeHolder extends RecyclerView.ViewHolder {
        TextView memberChageTv;

        public MemberChangeHolder(View itemView) {
            super(itemView);
            memberChageTv = itemView.findViewById(R.id.chat_member_change);
        }
    }

    private String convertUrl(String url) {
        int index = url.lastIndexOf(".");
        String s1 = url.substring(0, index);
        String s2 = url.substring(index, url.length());
        return s1 + "_s" + s2;
    }

}
