package com.remair.heixiu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.remair.heixiu.R;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/8/29 10:35
 * 修改人：JXH
 * 修改时间：2016/8/29 10:35
 * 修改备注：
 */
public class BlankHintView extends FrameLayout {

    private TextView mViewById;
    private TextView mViewById1;


    public BlankHintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    public BlankHintView(Context context) {
        this(context, null);
    }


    public BlankHintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    private void initView(Context context) {
        View.inflate(context, R.layout.item_blank_hint, this);
        mViewById = (TextView) findViewById(R.id.tv_blank1);
        mViewById1 = (TextView) findViewById(R.id.tv_blank2);
    }


    public void setHint1Text(String text) {
        if (null != mViewById && null != text) {
            mViewById.setText(text);
        }
    }


    public void setHint2Text(String text) {
        if (null != mViewById1 && null != text) {
            mViewById1.setText(text);
        }
    }
}
