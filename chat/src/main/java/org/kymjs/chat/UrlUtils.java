package org.kymjs.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Browser;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 各种链接跳转样式
 *
 * @author kymjs (http://www.kymjs.com/) on 8/10/15.
 */
public class UrlUtils {


    /**
     * 让TextView自动解析URL并高亮设置点击链接(链接不支持中文)
     * Note:深深的体会到，写一个正则不容易啊，Android居然还不支持POSIX字符
     * <p/>
     * Created by kymjs(www.kymjs.com) on 8/5/15.
     *
     * @param tv      TextView
     * @param content 要高亮的内容
     * @return 已经解析之后的TextView
     */
    public static TextView handleText(TextView tv, String content) {
        SpannableStringBuilder sp = new SpannableStringBuilder(content);
        //又碰上一个坑，在Android中的\p{Alnum}和Java中的\p{Alnum}不是同一个值，非得要我换成[a-zA-Z0-9]才行
        Pattern pattern = Pattern.compile("(http|https|ftp|svn)://([a-zA-Z0-9]+[/?.?])" +
                "+[a-zA-Z0-9]*\\??([a-zA-Z0-9]*=[a-zA-Z0-9]*&?)*");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String url = matcher.group();
            sp.setSpan(new URLSpan(url), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(getClickableSpan(url), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(sp);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        return tv;
    }

    /**
     * 处理html数据的高亮与响应
     *
     * @param tv
     * @param content
     * @return
     */
    public static TextView handleHtmlText(TextView tv, String content) {
        SpannableStringBuilder sp = new SpannableStringBuilder(Html.fromHtml(content));
        URLSpan[] urlSpans = sp.getSpans(0, sp.length(), URLSpan.class);
        for (final URLSpan span : urlSpans) {
            int start = sp.getSpanStart(span);
            int end = sp.getSpanEnd(span);
            sp.setSpan(getClickableSpan(span.getURL()), start, end, Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(sp);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        return tv;
    }

    /**
     * 设置链接跳转与高亮样式
     *
     * @param url
     * @return
     */
    public static ClickableSpan getClickableSpan(final String url) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Uri uri = Uri.parse(url);
                Context context = widget.getContext();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
    }

    public static String getFileLength(long length) {
        if (length < 1024) return length + "B";
        if (length < 1024 * 1024) return String.format("%.1fKB", length / 1024f);
        if (length < 1024 * 1024 * 1024) return String.format("%.1fMB", length / (1024 * 1024f));
        return String.format("%.1fGB", length / (1024 * 1024 * 1024f));
    }
}
