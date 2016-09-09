package com.remair.heixiu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.TanmuBean;
import com.remair.heixiu.danmu.AnimationHelper;
import com.remair.heixiu.emoji.StringUtil;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.UiUtil;
import com.remair.heixiu.utils.Utils;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import org.json.JSONObject;

/**
 * 项目名称：Android
 * 类描述：弹幕类
 * 创建人：wsk
 * 创建时间：16/8/25 下午2:19
 * 修改人：wsk
 * 修改时间：16/8/25 下午2:19
 * 修改备注：
 */
public class BarrageView extends RelativeLayout {
    private MyHandler handler;
    //弹幕内容
    public TanmuBean tanmuBean;
    //父组件的高度
    private int validHeightSpace;

    Thread mthread;
    int danmuFlag = 0;//弹幕标记位置0是第一行，1...
    CreateTanmuThread mCreateTanmuThread;
    private Context context;
    private RelativeLayout view_one;
    private RelativeLayout view_two;
    private RelativeLayout view_three;
    private RelativeLayout view_four;
    private Queue<String> danmakuQueue;
    private RelativeLayout mTanmuContainer;


    public BarrageView(Context context, Queue<String> danmakuQueue, RelativeLayout mTanmuContainer, RelativeLayout view_one, RelativeLayout view_two, RelativeLayout view_three, RelativeLayout view_four) {
        super(context);
        this.context = context;
        this.danmakuQueue = danmakuQueue;
        this.mTanmuContainer = mTanmuContainer;
        this.view_one = view_one;
        this.view_two = view_two;
        this.view_three = view_three;
        this.view_four = view_four;
        initview();
    }


    public BarrageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void initview() {
        initdanmu2();
    }


    public void sendTanmu(String s) {
        tanmuBean.items = new String[] { s };
        existMarginValues.clear();
        mCreateTanmuThread = new CreateTanmuThread();
        mthread = new Thread(mCreateTanmuThread);
        mthread.start();
    }


    private void initdanmu2() {
        tanmuBean = new TanmuBean();
        tanmuBean.items = new String[] { "测试", "弹幕hahaafaf",
                "总是出fdfdafdaf~~", };
        handler = new MyHandler(BarrageView.this);
        existMarginValues.clear();
        mCreateTanmuThread = new CreateTanmuThread();
    }


    //自动添加弹幕
    private class CreateTanmuThread implements Runnable {
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            int N = tanmuBean.items.length;
            for (int i = 0; i < N; i++) {
                handler.obtainMessage(1, i, 0).sendToTarget();
                SystemClock.sleep(2000);
            }
        }
    }

    //在主线城中添加组
    private class MyHandler extends Handler {
        private WeakReference<BarrageView> ref;


        MyHandler(BarrageView ac) {
            ref = new WeakReference<>(ac);
        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                BarrageView ac = ref.get();
                if (ac != null && ac.tanmuBean != null) {
                    int index = msg.arg1;
                    String content = ac.tanmuBean.items[index];
                    float textSize = (float) (ac.tanmuBean.minTextSize * (1.2 +
                                                                                  Math.random() *
                                                                                          ac.tanmuBean.range));
                    int i = (int) (Math.random() * 100 % 6);
                    int textColor = Color.WHITE;
                    showTanmu(content, textSize, textColor, UiUtil
                            .getDisplayWidth(context));
                }
            }
        }
    }


    private void showTanmu(String content, float textSize, int textColor, int width) {
        final View view = View
                .inflate(context, R.layout.item_live_chat_danmu, null);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view
                .findViewById(R.id.item_live_chat_avatar);
        TextView item_live_chat_name = (TextView) view
                .findViewById(R.id.item_live_chat_name);
        TextView item_live_chat_des = (TextView) view
                .findViewById(R.id.item_live_chat_des);
        try {
            JSONObject jsonObject = new JSONObject(content);
            String username = jsonObject.getJSONObject("user")
                                        .optString("user_name");
            String user_avatar = jsonObject.getJSONObject("user")
                                           .optString("user_avatar");
            String des = jsonObject.getJSONObject("command").optString("des");
            int type = jsonObject.getJSONObject("command").optInt("type");
            SpannableString spanString = StringUtil
                    .stringToSpannableString(des, context);
            if (type == 1) {
                simpleDraweeView.setImageResource(R.drawable.win_prize);
            } else {
                int px = Utils.getPX(30);
                if (type == 0) {
                    HXImageLoader
                            .loadImage(simpleDraweeView, user_avatar, px, px);
                } else {
                    HXImageLoader
                            .loadImage(simpleDraweeView, user_avatar, px, px);
                }
            }
            item_live_chat_name.setText(username);
            item_live_chat_des.setText(spanString);
            item_live_chat_des.setTextColor(textColor);
            int leftMargin = mTanmuContainer.getRight() -
                    mTanmuContainer.getLeft() -
                    mTanmuContainer.getPaddingLeft();
            //计算本条弹幕的topMargin(随机值，但是与屏幕中已有的不重复)
            int verticalMargin = getRandomTopMargin();
            //            view.setTag(verticalMargin);
            //            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            //            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            //            params.topMargin = verticalMargin;
            //            view.setLayoutParams(params);
            // view.setGravity(Gravity.CENTER_HORIZONTAL);
            if (danmuFlag == 0) {
                danmuFlag = 1;
                Animation anim = AnimationHelper
                        .createTranslateAnim(leftMargin, -width, width);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }


                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (danmakuQueue.size() == 0) {
                            danmuFlag = 0;
                        }
                    }


                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                view.startAnimation(anim);
                view_one.addView(view);
            } else if (danmuFlag == 1) {
                danmuFlag = 2;
                Animation anim = AnimationHelper
                        .createTranslateAnim(leftMargin, -width, width);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }


                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (danmakuQueue.size() == 0) {
                            danmuFlag = 0;
                        }
                    }


                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                view.startAnimation(anim);
                view_two.addView(view);
            } else if (danmuFlag == 2) {
                danmuFlag = 3;
                Animation anim = AnimationHelper
                        .createTranslateAnim(leftMargin, -width, width);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }


                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //移除该组�?                containerVG.removeView(textView);
                        //移除占位
                        //                        int verticalMargin = (int) view_one.getTag();
                        //                        existMarginValues.remove(verticalMargin);
                        //                        danmakuQueue.poll();
                        if (danmakuQueue.size() == 0) {
                            danmuFlag = 0;
                        }
                    }


                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                view.startAnimation(anim);
                view_three.addView(view);
            } else if (danmuFlag == 3) {
                danmuFlag = 0;
                Animation anim = AnimationHelper
                        .createTranslateAnim(leftMargin, -width, width);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }


                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //移除该组�?                containerVG.removeView(textView);
                        //移除占位
                        //                        int verticalMargin = (int) view_one.getTag();
                        //                        existMarginValues.remove(verticalMargin);
                        //                        danmakuQueue.poll();
                        if (danmakuQueue.size() == 0) {
                            danmuFlag = 0;
                        }
                    }


                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                view.startAnimation(anim);
                view_four.addView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //记录当前仍在显示状态的弹幕的位置（避免重复）
    private Set<Integer> existMarginValues = new HashSet<>();
    private int linesCount;


    private int getRandomTopMargin() {
        //计算用于弹幕显示的空间高
        if (validHeightSpace == 0) {
            validHeightSpace = mTanmuContainer.getBottom() -
                    mTanmuContainer.getTop() -
                    mTanmuContainer.getPaddingTop() -
                    mTanmuContainer.getPaddingBottom();
        }
        //计算可用的行
        //
        if (linesCount == 0) {
            linesCount = validHeightSpace /
                    Utils.getPX(tanmuBean.minTextSize * (1 + tanmuBean.range));
            if (linesCount == 0) {
                throw new RuntimeException("Not enough space to show text.");
            }
        }
        while (true) {
            int randomIndex = (int) (Math.random() * linesCount);
            int marginValue = randomIndex * (validHeightSpace / linesCount);
            if (!existMarginValues.contains(marginValue)) {
                existMarginValues.add(marginValue);
                return marginValue;
            }
        }
    }
}
