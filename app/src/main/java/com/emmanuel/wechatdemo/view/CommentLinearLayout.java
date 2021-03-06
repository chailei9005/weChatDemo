package com.emmanuel.wechatdemo.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emmanuel.wechatdemo.App;
import com.emmanuel.wechatdemo.R;
import com.emmanuel.wechatdemo.bean.Comment;
import com.emmanuel.wechatdemo.bean.User;
import com.emmanuel.wechatdemo.util.Constants;
import com.emmanuel.wechatdemo.util.ToastUtil;

import java.util.List;

/**
 * Created by user on 2016/9/8.
 */
public class CommentLinearLayout extends LinearLayout {

    private static final String TAG = "CommentLinearLayout";

    private LayoutInflater layoutInflater;
    private List<Comment> commentList;
    private int nameColor;
    private int clickItemBg = 0xb5b5b5;
    private Handler myHandler;
    private int posSS;

    public CommentLinearLayout(Context context) {
        super(context);
        nameColor = ContextCompat.getColor(context, R.color.text_color_blue_light);
    }

    public CommentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        nameColor = ContextCompat.getColor(context, R.color.text_color_blue_light);
    }

    public CommentLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        nameColor = ContextCompat.getColor(context, R.color.text_color_blue_light);
    }

    private View createView(final int position) {
        if(layoutInflater == null)
            layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_comment, this, false);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        tvMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myHandler != null){
                    User fromUser = commentList.get(position).fromUser;
                    if(fromUser.name.equals(App.getInstance().getUser().name))
                        return;
                    Message message = new Message();
                    message.what = Constants.HANDLER_FLAG_SHOW_EDITTEXT;
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.BUNDLE_KEY_SHUOSHUO_POS, posSS);
                    bundle.putSerializable(Constants.BUNDLE_KEY_TO_USER, fromUser);
                    message.setData(bundle);
                    myHandler.sendMessage(message);
                }
            }
        });
        String fromName = commentList.get(position).fromUser.name;
        String toName = null;
        if(commentList.get(position).toUser != null)
            toName = commentList.get(position).toUser.name;
        String msg = commentList.get(position).content;
        tvMsg.setText(getStringComment(fromName, toName, msg));
        return view;
    }

    private SpannableStringBuilder getStringComment(String fromName, String toName, String msg) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(fromName);
        stringBuilder.setSpan(new SpanStringClickListener(), 0, fromName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(toName != null) {
            stringBuilder.append("回复");
            stringBuilder.append(toName);
            int index = 2 + fromName.length();
            stringBuilder.setSpan(new SpanStringClickListener(), index, index + toName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        stringBuilder.append(": ");
        stringBuilder.append(msg);
        return stringBuilder;
    }

    public void setCommentList(List<Comment> list, int posSS) {
        this.commentList = list;
        this.posSS = posSS;
        notifyDataSetChanged();
    }

    public void setHandler(Handler myHandler){
        this.myHandler = myHandler;
    }

    //更新评论
    public void notifyDataSetChanged() {
        if(commentList == null || commentList.size() == 0){
            return;
        }
        removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < commentList.size(); i++) {
            View view = createView(i);
            addView(view, i, layoutParams);
        }
    }

    class SpanStringClickListener extends ClickableSpan{

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "dddd", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);

            ds.setColor(nameColor);
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }
    }
}
