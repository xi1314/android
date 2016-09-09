package com.remair.heixiu.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.R;

/**
 * 项目名称：Android
 * 类描述：类似输入支付密码的输入框
 * 创建人：JXH
 * 创建时间：2016/8/11 12:20
 * 修改人：JXH
 * 修改时间：2016/8/11 12:20
 * 修改备注：
 */
public class EditTextView extends FrameLayout {
    @BindView(R.id.iv_circle1) ImageView mIvCircle1;
    @BindView(R.id.tv_num1) TextView mTvNum1;
    @BindView(R.id.iv_circle2) ImageView mIvCircle2;
    @BindView(R.id.tv_num2) TextView mTvNum2;
    @BindView(R.id.iv_circle3) ImageView mIvCircle3;
    @BindView(R.id.tv_num3) TextView mTvNum3;
    @BindView(R.id.iv_circle4) ImageView mIvCircle4;
    @BindView(R.id.tv_num4) TextView mTvNum4;
    @BindView(R.id.et_view) EditText mEtView;
    private StringBuilder builder;
    private View[] imageViews;


    public EditTextView(Context context) {
        super(context);
        initView(context);
    }


    public EditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    public EditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    void initView(final Context context) {
        builder = new StringBuilder();
        LayoutInflater.from(context).inflate(R.layout.edit_view, this);
        ButterKnife.bind(this);
        imageViews = new ImageView[] { mIvCircle1, mIvCircle2, mIvCircle3,
                mIvCircle4 };
        mEtView.addTextChangedListener(mTextWatcher);
        mEtView.setOnKeyListener(keyListener);
    }


    TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }


        @Override
        public void afterTextChanged(Editable s) {

            if (s.toString().length() == 0) {
                return;
            }

            if (builder.length() < 4) {
                builder.append(s.toString());
                setTextValue();
            }
            s.delete(0, s.length());
        }
    };

    OnKeyListener keyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_DEL &&
                    event.getAction() == KeyEvent.ACTION_UP) {
                delTextValue();
                return true;
            }
            return false;
        }
    };


    private void setTextValue() {

        String str = builder.toString();
        int len = str.length();

        if (len <= 4) {
            imageViews[len - 1].setVisibility(View.VISIBLE);
        }
        if (mListener != null) {
            mListener.onNumChanged(str);
        }
        if (len == 4) {
            if (mListener != null) {
                mListener.onNumCompleted(str);
            }
        }
    }


    private void delTextValue() {
        String str = builder.toString();
        int len = str.length();
        if (len == 0) {
            return;
        }
        if (len > 0 && len <= 4) {
            builder.delete(len - 1, len);
        }
        if (mListener != null) {
            mListener.onNumChanged(builder.toString());
        }
        imageViews[len - 1].setVisibility(View.INVISIBLE);
    }


    public interface SecurityEditCompleListener {
        void onNumCompleted(String num);

        void onNumChanged(String num);
    }

    public SecurityEditCompleListener mListener;


    public void setSecurityEditCompleListener(SecurityEditCompleListener mListener) {
        this.mListener = mListener;
    }


    public String getSecurityString() {
        return builder.toString();
    }
}
