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
package org.kymjs.chat.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.kymjs.chat.OnOperationListener;
import org.kymjs.chat.R;
import org.kymjs.chat.SoftKeyboardStateHelper;
import org.kymjs.chat.adapter.FaceCategroyAdapter;
import org.kymjs.chat.audio.AudioRecordButton;
import org.kymjs.kjframe.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 控件主界面
 *
 * @author kymjs (http://www.kymjs.com/)
 */
public class KJChatKeyboard extends RelativeLayout implements
        SoftKeyboardStateHelper.SoftKeyboardStateListener {


    private InputMethodManager imm;
    private Activity acy;
    private AudioRecordButton mBtnRecord;

    public interface OnToolBoxListener {
        void onShowFace();
    }

    public static final int LAYOUT_TYPE_HIDE = 0;
    public static final int LAYOUT_TYPE_FACE = 1;
    public static final int LAYOUT_TYPE_MORE = 2;
    public static final int LAYOUT_TYPE_SOUND = 3;
    private int foucusColor = Color.parseColor("#3dc725");
    private int noneFoucusColor = Color.parseColor("#949492");
    /**
     * 最上层输入框
     */
    private EditText mEtMsg;
    private CheckBox mBtnFace;
    private CheckBox mBtnMore;
    private Button mBtnSend;
    private CheckBox mBtnSound;
    private RelativeLayout mInputLayout;
    private View mInputLine;

    /**
     * 表情
     */
    private ViewPager mPagerFaceCagetory;
    private RelativeLayout mRlFace;
    private PagerSlidingTabStrip mFaceTabs;

    private int layoutType = LAYOUT_TYPE_HIDE;
    private FaceCategroyAdapter adapter;  //点击表情按钮时的适配器

    private List<String> mFaceData;

    private OnOperationListener listener;
    private OnToolBoxListener mFaceListener;
    private SoftKeyboardStateHelper mKeyboardHelper;

    public KJChatKeyboard(Context context) {
        super(context);
        init(context);
    }

    public KJChatKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KJChatKeyboard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.acy = (Activity) context;
        View root = View.inflate(context, R.layout.chat_tool_box, null);
        this.addView(root);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
        this.initWidget();
    }

    private void initData() {
        imm = (InputMethodManager) acy
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        mFaceData = new ArrayList<>();
//        mKeyboardHelper = new SoftKeyboardStateHelper(((Activity) getContext())
//                .getWindow().getDecorView());
//        mKeyboardHelper.addSoftKeyboardStateListener(this);
    }

    private void initWidget() {
        mEtMsg = (EditText) findViewById(R.id.toolbox_et_message);
        mBtnSend = (Button) findViewById(R.id.toolbox_btn_send);
        mBtnFace = (CheckBox) findViewById(R.id.toolbox_btn_face);
        mBtnMore = (CheckBox) findViewById(R.id.toolbox_btn_more);
        mBtnSound = (CheckBox) findViewById(R.id.toolbox_btn_sound);
        mRlFace = (RelativeLayout) findViewById(R.id.toolbox_layout_face);
        mBtnRecord = (AudioRecordButton) findViewById(R.id.recordButton);
        mInputLayout = (RelativeLayout) findViewById(R.id.input_layout);
        mInputLine = findViewById(R.id.input_line);
        mPagerFaceCagetory = (ViewPager) findViewById(R.id.toolbox_pagers_face);
        mFaceTabs = (PagerSlidingTabStrip) findViewById(R.id.toolbox_tabs);
        adapter = new FaceCategroyAdapter(((FragmentActivity) getContext())
                .getSupportFragmentManager(), LAYOUT_TYPE_FACE);
        mBtnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String content = mEtMsg.getText().toString();
                    listener.send(content);
                    mEtMsg.setText(null);
                    mBtnSend.setVisibility(INVISIBLE);
                    mBtnMore.setVisibility(VISIBLE);
                }
            }
        });
        // 点击表情按钮
        mBtnFace.setOnClickListener(getFunctionBtnListener(LAYOUT_TYPE_FACE));
        // 点击表情按钮旁边的加号
        mBtnMore.setOnClickListener(getFunctionBtnListener(LAYOUT_TYPE_MORE));

        mBtnSound.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnRecord.getVisibility() == VISIBLE && layoutType == LAYOUT_TYPE_SOUND) {
                    showKeyboard();
                    mBtnSound.setChecked(false);
                    hideSoundLayout();
                } else {
                    hideLayout();
                    mBtnSound.setChecked(true);
                    hideKeyboard();
                    showSoundLayout();
                    layoutType = LAYOUT_TYPE_SOUND;
                }
            }
        });
        // 点击消息输入框
        mEtMsg.setOnTouchListener(new OnTouchListener() {
            int flag;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                flag++;
                if (flag == 2) {
                    hideLayout();
                    flag = 0;
                }
                return false;
            }
        });
        mEtMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isEmpty(s)) {
                    mBtnSend.setVisibility(INVISIBLE);
                    mBtnMore.setVisibility(VISIBLE);
                } else {
                    mBtnSend.setVisibility(VISIBLE);
                    mBtnMore.setVisibility(INVISIBLE);
                }
            }
        });
        mEtMsg.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) mInputLine.setBackgroundColor(foucusColor);
                else mInputLine.setBackgroundColor(noneFoucusColor);
            }
        });
    }


    /*************************
     * 内部方法 start
     ************************/

    private OnClickListener getFunctionBtnListener(final int which) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow() && which == layoutType) {
                    hideLayout();
                    showKeyboard();
                } else {
                    changeLayout(which);
                    showInputLayout();
                    mBtnFace.setChecked(layoutType == LAYOUT_TYPE_FACE);
                    mBtnMore.setChecked(layoutType == LAYOUT_TYPE_MORE);
                    mBtnSound.setChecked(layoutType == LAYOUT_TYPE_SOUND);
                }
            }
        };
    }

    private void changeLayout(int mode) {
        adapter = new FaceCategroyAdapter(((FragmentActivity) getContext())
                .getSupportFragmentManager(), mode);
        adapter.setOnOperationListener(listener);
        layoutType = mode;
        setFaceData(mFaceData);
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {
        hideLayout();
    }

    @Override
    public void onSoftKeyboardClosed() {
    }

    /***************************** 内部方法 end ******************************/

    /**************************
     * 可选调用的方法 start
     **************************/

    public void setFaceData(List<String> faceData) {
        mFaceData = faceData;
        adapter.refresh(faceData);
        mPagerFaceCagetory.setAdapter(adapter);
        mFaceTabs.setViewPager(mPagerFaceCagetory);
        if (layoutType == LAYOUT_TYPE_MORE) {
            mFaceTabs.setVisibility(GONE);
        } else {
            //加1是表示第一个分类为默认的emoji表情分类，这个分类是固定不可更改的
//            if (faceData.size() + 1 < 2) {
            mFaceTabs.setVisibility(GONE);
//            } else {
//                mFaceTabs.setVisibility(VISIBLE);
//            }
        }
    }

    public EditText getEditTextBox() {
        return mEtMsg;
    }

    public AudioRecordButton getmBtnRecord() {
        return mBtnRecord;
    }

    public void setAudioFinishRecorderListener(AudioRecordButton.AudioFinishRecorderListener listener) {
        mBtnRecord.setAudioFinishRecorderListener(listener);
    }

    public void showInputLayout() {
        hideKeyboard();
        hideSoundLayout();
        // 延迟一会，让键盘先隐藏再显示表情键盘，否则会有一瞬间表情键盘和软键盘同时显示
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mRlFace.setVisibility(View.VISIBLE);
                if (mFaceListener != null) {
                    mFaceListener.onShowFace();
                }
            }
        }, 50);
    }

    public void showSoundLayout() {
        mBtnRecord.setVisibility(VISIBLE);
        mInputLayout.setVisibility(GONE);
        mBtnSend.setVisibility(INVISIBLE);
        mBtnMore.setVisibility(VISIBLE);
    }

    public void hideSoundLayout() {
        mBtnRecord.setVisibility(GONE);
        mInputLayout.setVisibility(VISIBLE);
        if (!StringUtils.isEmpty(mEtMsg.getText())) {
            mBtnSend.setVisibility(VISIBLE);
            mBtnMore.setVisibility(INVISIBLE);
        } else {
            mBtnSend.setVisibility(INVISIBLE);
            mBtnMore.setVisibility(VISIBLE);
        }
    }

    public boolean isShow() {
        return mRlFace.getVisibility() == VISIBLE;
    }

    public void hideLayout() {
        mRlFace.setVisibility(View.GONE);
        mBtnFace.setChecked(false);
        mBtnMore.setChecked(false);
        if (layoutType != LAYOUT_TYPE_SOUND)
            mBtnSound.setChecked(false);
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        if (imm.isActive() && acy.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(acy.getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     */
    public void showKeyboard() {
        if (acy.getCurrentFocus() != null)
            imm.showSoftInputFromInputMethod(acy.getCurrentFocus()
                    .getWindowToken(), 0);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mEtMsg.requestFocus();
    }


    public OnOperationListener getOnOperationListener() {
        return listener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.listener = onOperationListener;
        adapter.setOnOperationListener(onOperationListener);
    }

    public void setOnToolBoxListener(OnToolBoxListener mFaceListener) {
        this.mFaceListener = mFaceListener;
    }

    /*********************** 可选调用的方法 end ******************************/
}
