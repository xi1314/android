package com.remair.heixiu.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.badoo.mobile.util.WeakHandler;
import com.remair.heixiu.R;

/**
 * Created by JXHIUUI on 2016/8/2.
 */
public class FlowerAnimationView {
    FlakeView flakeView;
    Context context;
    ViewGroup parent;
    WeakHandler handler;
    StopListener listener;
    boolean stop = true;
    private ImageView imageView;

    public FlowerAnimationView(Context context, ViewGroup parent, WeakHandler handler) {
        this.context = context;
        this.parent = parent;
        this.handler = handler;
    }

    Runnable runnableRain = new Runnable() {

        @Override
        public void run() {
            flakeView.addFlakes(6);
            handler.postDelayed(runnableRain, 1300);
            if (stop) {
                if (flakeView.getNumFlakes() > 110) {
                    handler.removeCallbacks(runnableRain);
                }
            }
        }
    };
    Runnable runnableStop = new Runnable() {
        @Override
        public void run() {
            stop();
        }
    };

    public void start() {
        flakeView = new FlakeView(context);
        imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.diamond_bg);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        parent.addView(imageView, layoutParams);
        parent.addView(flakeView);
        handler.postDelayed(runnableRain, 0);
        handler.postDelayed(runnableStop, 15 * 1000);
    }

    void stop() {
        if (flakeView != null) {
//            flakeView.stop();
            handler.removeCallbacks(runnableRain);
            stop = false;
            parent.removeView(imageView);
            parent.removeView(flakeView);
            if (listener != null) {
                listener.onStop();
            }
        }
    }

    public void setOnStopListener(StopListener listener) {
        this.listener = listener;
    }

    public interface StopListener {
        void onStop();
    }
}
