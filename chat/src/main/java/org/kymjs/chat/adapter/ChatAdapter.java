/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kymjs.chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.kymjs.chat.BaseType;
import org.kymjs.chat.DisplayUtils;
import org.kymjs.chat.OnChatItemClickListener;
import org.kymjs.chat.R;
import org.kymjs.chat.UrlUtils;
import org.kymjs.chat.ViewFindUtils;
import org.kymjs.chat.bean.MessageChat;
import org.kymjs.kjframe.utils.DensityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author kymjs (http://www.kymjs.com/) on 6/8/15.
 */
public class ChatAdapter extends BaseAdapter {

    private final Context cxt;
    private final int mMaxItemWidth;
    private final int mMinItemWidth;
    private final int maxContentWidth;
    private List<MessageChat> datas = null;
    private OnChatItemClickListener listener;
    private SimpleDateFormat format;
    private String name;
    private int fileNameColor = Color.parseColor("#347dd1");
    private final String avatar;
    private final String toAvatar;
    private AvatarLongListener longlistener;
//    private ReadStatusListener readStatusListener;//监听读取状态

    public ChatAdapter(Context cxt, List<MessageChat> datas, String name,String avatar,String toAvatar, OnChatItemClickListener listener) {
        this.cxt = cxt;
        if (datas == null) {
            datas = new ArrayList<>(0);
        }
        this.name = name;
        this.datas = datas;
        this.listener = listener;
        this.avatar = avatar;
        this.toAvatar = toAvatar;
        format = new SimpleDateFormat("HH:mm:ss", Locale.CANADA);
        WindowManager wm = (WindowManager) cxt.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWidth = (int) (outMetrics.widthPixels * 0.65f);
        mMinItemWidth = (int) (outMetrics.widthPixels * 0.12f);
        maxContentWidth = DensityUtils.getScreenW(cxt) - DensityUtils.dip2px(cxt, 100);
    }

    public void refresh(List<MessageChat> datas) {
        if (datas == null) {
            datas = new ArrayList<>(0);
        }
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).isSend() ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final MessageChat data = datas.get(position);
        if (v == null) {
            if (data.isSend()) {
                v = View.inflate(cxt, R.layout.chat_item_list_right, null);
            } else {
                v = View.inflate(cxt, R.layout.chat_item_list_left, null);
            }
        }
        RelativeLayout layout_content = ViewFindUtils.hold(v, R.id.chat_item_layout_content);
        ImageView img_avatar = ViewFindUtils.hold(v, R.id.chat_item_avatar);
        ImageView img_chatimage = ViewFindUtils.hold(v, R.id.chat_item_content_image);
        ImageView img_sendfail = ViewFindUtils.hold(v, R.id.chat_item_fail);
        ProgressBar progress = ViewFindUtils.hold(v, R.id.chat_item_progress);
        TextView tv_chatcontent = ViewFindUtils.hold(v, R.id.chat_item_content_text);
        ImageView fileIcon = ViewFindUtils.hold(v, R.id.fileIcon);
        final ImageView chatsound = ViewFindUtils.hold(v, R.id.chat_item_audio);
        TextView tv_chatsound = ViewFindUtils.hold(v, R.id.chat_item_audio_length);
        TextView tv_date = ViewFindUtils.hold(v, R.id.chat_item_date);
        TextView tv_name = ViewFindUtils.hold(v, R.id.chat_item_name);
        if(data.isSend()){
            TextView hasRead = ViewFindUtils.hold(v, R.id.hasRead);
            if(!data.isHasRead() && data.getState() == MessageChat.MSG_STATE_SUCCESS){
                hasRead.setVisibility(View.VISIBLE);
            }else{
                hasRead.setVisibility(View.GONE);
            }
        }
        RelativeLayout.LayoutParams vp = (RelativeLayout.LayoutParams) layout_content.getLayoutParams();
        tv_chatsound.setMaxWidth(mMaxItemWidth);
        long preDate = 0;
        if (position > 0) preDate = datas.get(position - 1).getTime();
        if (data.getTime() - preDate > 300000) {
            tv_date.setText(format.format(new Date(data.getTime())));
            tv_date.setVisibility(View.VISIBLE);
        } else {
            tv_date.setVisibility(View.GONE);
        }
        int type = data.getType();
        if (type == BaseType.ChatMediaType.Other) {
            vp.width = maxContentWidth;
        }else {
            vp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        layout_content.setLayoutParams(vp);
        tv_chatcontent.setMaxWidth(maxContentWidth);
        switch (type) {
            case BaseType.ChatMediaType.Text: //文本
                img_chatimage.setVisibility(View.GONE);
                tv_chatcontent.setVisibility(View.VISIBLE);
                chatsound.setVisibility(View.GONE);
                tv_chatsound.setVisibility(View.GONE);
                fileIcon.setVisibility(View.GONE);
                tv_chatcontent.setTextIsSelectable(true);
                DisplayUtils.handleAll(tv_chatcontent, cxt, data.getContent());
                break;
            case BaseType.ChatMediaType.Other://其他
                img_chatimage.setVisibility(View.GONE);
                tv_chatcontent.setVisibility(View.VISIBLE);
                chatsound.setVisibility(View.GONE);
                tv_chatsound.setVisibility(View.GONE);
                fileIcon.setVisibility(View.VISIBLE);
                fileIcon.setClickable(false);
                fileIcon.setImageResource(DisplayUtils.getIcon(data.getMediaName()));
                SpannableStringBuilder s = new SpannableStringBuilder(data.getMediaName());
                s.setSpan(new ForegroundColorSpan(fileNameColor), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.append("\n\n").append(UrlUtils.getFileLength(data.getMediaLength()));
                tv_chatcontent.setText(s);
                tv_chatcontent.setClickable(false);
                tv_chatcontent.setTextIsSelectable(false);
                break;
            case BaseType.ChatMediaType.Image://图片
                tv_chatcontent.setVisibility(View.GONE);
                img_chatimage.setVisibility(View.VISIBLE);
                chatsound.setVisibility(View.GONE);
                tv_chatsound.setVisibility(View.GONE);
                fileIcon.setVisibility(View.GONE);
                Glide.with(cxt).load(data.getContent()).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.icon_load_failed).into(img_chatimage);
//                Glide.with(cxt).load(data.getContent()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img_chatimage);
                break;
            case BaseType.ChatMediaType.Voice://声音
                tv_chatcontent.setVisibility(View.GONE);
                img_chatimage.setVisibility(View.GONE);
                chatsound.setVisibility(View.VISIBLE);
                chatsound.setTag(data.isSend());
                tv_chatsound.setVisibility(View.VISIBLE);
                fileIcon.setVisibility(View.GONE);
                ViewGroup.LayoutParams lp = chatsound.getLayoutParams();
                lp.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f) * (data.getMediaLength() > 60 ? 60 : data.getMediaLength()));
                chatsound.setLayoutParams(lp);
                tv_chatsound.setText(Math.round(data.getMediaLength()) + "\"");
                break;
        }

        //如果是表情或图片，则不显示气泡，如果是图片则显示气泡
        if (type == BaseType.ChatMediaType.Image)
            layout_content.setBackgroundResource(android.R.color.transparent);
        else {
            if (data.isSend()) {
                layout_content.setBackgroundResource(R.drawable.chat_to_bg_selector);
            } else {
                layout_content.setBackgroundResource(R.drawable.chat_from_bg_selector);
            }
        }

        //显示头像
        if (data.isSend()) {
            tv_name.setVisibility(View.INVISIBLE);
            Glide.with(cxt).load(avatar).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_head).into(img_avatar);
        } else {
            if (!data.getToUserName().equals(name)) {//其他人的消息
                tv_name.setText(data.getToUserName());
                tv_name.setVisibility(View.VISIBLE);
            } else tv_name.setVisibility(View.INVISIBLE);
            Glide.with(cxt).load(toAvatar).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_head).into(img_avatar);
            if(longlistener != null){
                img_avatar.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longlistener.avatarLongClicked(data.getToUserName());
                        return true;
                    }
                });
            }

        }
//        if (listener != null) {
//            tv_chatcontent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onTextClick(position);
//                }
//            });
//            img_chatimage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    switch (data.getType()) {
//                        case MessageChat.MSG_TYPE_IMAGE:
//                            listener.onImageClick(position);
//                            break;
//                        case MessageChat.MSG_TYPE_FACE:
//                            listener.onFaceClick(position);
//                            break;
//                    }
//                }
//            });
//    }
        if (listener != null) {
            layout_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (data.getType()) {
                        case BaseType.ChatMediaType.Image:
                            listener.onImageClick(position);
                            break;
//                        case MessageChat.MSG_TYPE_FACE:
//                            listener.onFaceClick(position);
//                            break;
                        case BaseType.ChatMediaType.Other:
                            listener.onFileClick(position);
                            break;
                        case BaseType.ChatMediaType.Voice:
                            listener.onAudioClick(chatsound, position);
                            break;
                    }
                }
            });
        }

        //消息发送的状态
        switch (data.getState()) {
            case MessageChat.MSG_STATE_FAIL:
                progress.setVisibility(View.GONE);
                img_sendfail.setVisibility(View.VISIBLE);
                break;
            case MessageChat.MSG_STATE_SUCCESS:
                progress.setVisibility(View.GONE);
                img_sendfail.setVisibility(View.GONE);
                break;
            case MessageChat.MSG_STATE_SENDING:
                progress.setVisibility(View.VISIBLE);
                img_sendfail.setVisibility(View.GONE);
                break;
        }

        return v;
    }

    public void finishUpload(int position) {
        View v = getView(position, null, null);
    }

    public void setUploadProgress(int progress, int position) {
        View v = getView(position, null, null);

    }
    public interface AvatarLongListener{

        public void avatarLongClicked(String nickName);
    }
    public void setAvatarLongListener(AvatarLongListener longlistener){
        this.longlistener =longlistener;
    }

   /* public interface ReadStatusListener{
        public void readStatusListener(MessageChat currentChat);
    }
    public void setReadStatusListener(ReadStatusListener readStatusListener){
        this.readStatusListener = readStatusListener;
    }*/
}
