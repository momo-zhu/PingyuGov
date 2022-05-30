package org.kymjs.chat;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by Tech07 on 2016/2/26.
 */
public interface OnChatItemClickListener {
    void onTextClick(int position);

    void onImageClick(int position);

    void onFaceClick(int position);

    void onFileClick(int position);

    void onAudioClick(ImageView view, int position);
}
