package com.remair.heixiu.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by JXHIUUI on 2016/6/17.
 */
public class MemberLinearLayoutManager extends LinearLayoutManager {
    public MemberLinearLayoutManager(Context context) {
        super(context);
    }


    public MemberLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }


    public MemberLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
