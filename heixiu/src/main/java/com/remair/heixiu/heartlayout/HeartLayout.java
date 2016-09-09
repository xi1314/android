/*
 * Copyright (C) 2015 tyrantgit
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
package com.remair.heixiu.heartlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.remair.heixiu.R;
import java.lang.ref.WeakReference;
import java.util.Random;


/**
 * 飘心动画
 */
public class HeartLayout extends RelativeLayout implements View.OnClickListener {

    private AbstractPathAnimator mAnimator;
    private AttributeSet attrs = null;
    private int defStyleAttr = 0;
    private OnHearLayoutListener onHearLayoutListener;
    private static HeartHandler heartHandler;
    private static HeartThread heartThread;

    public void setOnHearLayoutListener(OnHearLayoutListener onHearLayoutListener) {
        this.onHearLayoutListener = onHearLayoutListener;
    }

    public interface OnHearLayoutListener {
        boolean onAddFavor();
    }

    public HeartLayout(Context context) {
        super(context);
        findViewById(context);
    }

    public HeartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        findViewById(context);
    }

    public HeartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
        findViewById(context);
    }

    private Bitmap bitmap;

    private void findViewById(Context context) {
        LayoutInflater.from(context).inflate(R.layout.ly_periscope, this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart0);
        dHeight = bitmap.getWidth();
        dWidth = bitmap.getHeight();
        textHight = sp2px(getContext(), 20) + dHeight / 2;

        pointx = dWidth;//随机上浮方向的x坐标

        bitmap.recycle();
    }

    private int mHeight;
    private int mWidth;
    private int textHight;
    private int dHeight;
    private int dWidth;
    private int initX;
    private int pointx;

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public class HeartHandler extends Handler {
        public final static int MSG_SHOW = 1;
        WeakReference<HeartLayout> wf;

        public HeartHandler(HeartLayout layout) {
            wf = new WeakReference<HeartLayout>(layout);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HeartLayout layout = wf.get();
            if (layout == null) return;
            switch (msg.what) {
                case MSG_SHOW:
                    addFavor();
                    break;
            }
        }
    }

    public class HeartThread implements Runnable {

        private long time = 0;
        private int allSize = 0;

        public void addTask(long time, int size) {
            this.time = time;
            allSize += size;
        }

        public void clean() {
            allSize = 0;
        }

        @Override
        public void run() {
            if (heartHandler == null) return;

            if (allSize > 0) {
                heartHandler.sendEmptyMessage(HeartHandler.MSG_SHOW);
                allSize--;
            }
            postDelayed(this, time);
        }
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HeartLayout, defStyleAttr, 0);

        if (pointx <= initX && pointx >= 0) {
            pointx -= 30;
        } else if (pointx >= -initX && pointx <= 0) {
            pointx += 30;
        } else pointx = initX;


        mAnimator = new PathAnimator(AbstractPathAnimator.Config.fromTypeArray(a, initX, textHight, pointx, dWidth, dHeight));
        a.recycle();
    }
    private void init2(AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HeartLayout, defStyleAttr, 0);

        if (pointx <= initX && pointx >= 0) {
            pointx -= 30;
        } else if (pointx >= -initX && pointx <= 0) {
            pointx += 30;
        } else pointx = initX;


        mAnimator = new PathAnimator(AbstractPathAnimator.Config.fromTypeArray(a, initX, textHight, pointx, 150, 150));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
//        initX = (mWidth/4)*3 - dWidth / 3;
        initX = (mWidth/4)*3 - dWidth / 3;

    }

    public AbstractPathAnimator getAnimator() {
        return mAnimator;
    }

    public void setAnimator(AbstractPathAnimator animator) {
        clearAnimation();
        mAnimator = animator;
    }

    public void clearAnimation() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
        }
        removeAllViews();
    }

//    private static int[] drawableIds = new int[]{R.drawable.mouse1, R.drawable.mouse2, R.drawable.mouse3, R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat_horn1, R.drawable.cat_horn2, R.drawable.cat_horn3,
//            R.drawable.fishbone1,R.drawable.fishbone2,R.drawable.fishbone3
//    };
    private static int[] drawableIds = new int[]{R.drawable.siceraria, R.drawable.siceraria1, R.drawable.balloon, R.drawable.balloon1, R.drawable.star, R.drawable.star1, R.drawable.circle_hx_main, R.drawable.circle1, R.drawable.star_zheng,
            R.drawable.star_zheng1,R.drawable.lelf_balloon,R.drawable.lelf_balloon1
    };
    private int dra=R.drawable.bat;

    private Random random = new Random();

    public void addFavor() {
        HeartView heartView = new HeartView(getContext());
        heartView.setDrawable(drawableIds[random.nextInt(11)]);
        init(attrs, defStyleAttr);
        mAnimator.start(heartView, this);
    }
    public void addFavorbig() {
        HeartView heartView = new HeartView(getContext());
        heartView.setDrawable(dra);
        init2(attrs, defStyleAttr);
        mAnimator.start(heartView, this);
//        Animation scaleAnimation = new ScaleAnimation(0.8f, 2.0f, 0.8f, 2.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        //设置动画时间
//        scaleAnimation.setDuration(5000);
//        scaleAnimation.setRepeatCount(0);
//        scaleAnimation.setFillAfter(false);
//        heartView.startAnimation(scaleAnimation);
    }

    private long nowTime, lastTime;
    final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};

    public static int sizeOfInt(int x) {
        for (int i = 0; ; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }

    public void addFavor(int size) {
        switch (sizeOfInt(size)) {
            case 1:
                size = size % 10;
                break;
            default:
                size = size % 100;
        }
        if (size == 0) return;
        nowTime = System.currentTimeMillis();
        long time = nowTime - lastTime;
        if (lastTime == 0)
            time = 2 * 1000;

        time = time / (size + 15);
        if (heartThread == null) {
            heartThread = new HeartThread();
        }
        if (heartHandler == null) {
            heartHandler = new HeartHandler(this);
            heartHandler.post(heartThread);
        }
        heartThread.addTask(time, size);
        lastTime = nowTime;
    }

    public void addHeart(int color) {
        HeartView heartView = new HeartView(getContext());
        heartView.setColor(color);
        init(attrs, defStyleAttr);
        mAnimator.start(heartView, this);
    }

    public void addHeart(int color, int heartResId, int heartBorderResId) {
        HeartView heartView = new HeartView(getContext());
        heartView.setColorAndDrawables(color, heartResId, heartBorderResId);
        init(attrs, defStyleAttr);
        mAnimator.start(heartView, this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img) {
            if (onHearLayoutListener != null) {
                boolean isAdd = onHearLayoutListener.onAddFavor();
                if (isAdd) addFavor();
            }
        }
    }

    public void clean() {
        if (heartThread != null) {
            heartThread.clean();
        }
    }

    public void release() {
        if (heartHandler != null) {
            heartHandler.removeCallbacks(heartThread);
            heartThread = null;
            heartHandler = null;
        }
    }
}
