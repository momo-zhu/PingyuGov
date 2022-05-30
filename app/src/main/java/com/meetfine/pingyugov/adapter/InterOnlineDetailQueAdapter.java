package com.meetfine.pingyugov.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import java.util.List;

/**
 * Created by Andriod05 on 2016/12/21.
 */
public class InterOnlineDetailQueAdapter extends ArrayAdapter<JSONObject> {
    private LayoutInflater inflater;
    private Context context;
    private StringBuilder sbNum = new StringBuilder();
    private StringBuilder sbQus = new StringBuilder();

    public InterOnlineDetailQueAdapter(Context context, List<JSONObject> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_inter_online_question,parent,false);
        }
        TextView questionNumber = ViewFindUtils.hold(convertView, R.id.questionNumber);//问题编号
        TextView question = ViewFindUtils.hold(convertView, R.id.question);//问题内容
        TextView reply = ViewFindUtils.hold(convertView, R.id.reply);//回复内容

        sbNum.delete(0, sbNum.length());
        sbQus.delete(0, sbQus.length());
        sbNum.append("问题").append(position + 1);
        questionNumber.setText(sbNum.toString());
        String replyName = item.getString("name");
        sbQus.append("[").append(replyName).append("]").append(item.getString("content"));
        SpannableStringBuilder ssbQus = new SpannableStringBuilder(sbQus.toString());
        int end = replyName == null ? 2 : replyName.length()+2;
        ssbQus.setSpan(new ForegroundColorSpan(Color.parseColor("#0099cc")),
                0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        question.setText(ssbQus);
        reply.setText(item.getString("reply"));

        return convertView;
    }
}
