package studio.archangel.toolkitv2.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by Michael on 2015/8/10.
 */
public class CountDownHandler extends Handler {
    static final int countdown_start = 0;
    static final int countdown_count = 1;
    long countdown_total = 5000;
    long countdown_interval = 1000;
    Object target;
    boolean counting = false;
    long countdown_start_time;
    CountDownListener listener;
    int count = 5, interval = 1;
    int last_count = count;

    public CountDownHandler(Object target, int count, CountDownListener listener) {
        super();
        setTarget(target);
        setCountDownListener(listener);
        setCount(count);
    }

    public CountDownHandler(int count, CountDownListener listener) {
        this(null, count, listener);
    }

    @Override
    public void handleMessage(Message msg) {
        if (target == null) {
            return;
        }
        switch (msg.what) {
            case countdown_start: {
                counting = true;
                countdown_start_time = SystemClock.uptimeMillis();
                if (listener != null) {
                    listener.onStart(target, count);
                }
                new CountDownThread().start();
                break;
            }
            case countdown_count: {
                if (!counting) {
                    break;
                }
                int count = (int) Math.ceil((countdown_total - (SystemClock.uptimeMillis() - countdown_start_time)) * 1.0 / countdown_interval);
                if (last_count <= count) {
                    break;
                }
                last_count = count;
                if (count <= 0) {
                    counting = false;
                    if (listener != null) {
                        listener.onEnd(target);
                    }
                } else {
                    if (listener != null) {
                        listener.onCountDown(target, count);
                    }
                }
                break;
            }
        }

    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public CountDownListener getCountDownListener() {
        return listener;
    }

    public void setCountDownListener(CountDownListener listener) {
        this.listener = listener;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        last_count = count;
        countdown_total = 1000 * count;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
        countdown_interval = 1000 * interval;
    }

    public void start() {
        sendEmptyMessage(countdown_start);
    }

    /**
     * 除非你清楚你在做什么...否则不要使用此方法
     */
    public void countDown() {
        sendEmptyMessage(countdown_count);
    }

    public interface CountDownListener {
        void onStart(Object target, int total);

        void onCountDown(Object target, int count);

        void onEnd(Object target);
    }

    class CountDownThread extends Thread {

        @Override
        public void run() {
            while (counting) {
                try {
                    Thread.sleep((long) (countdown_interval / 10.0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CountDownHandler.this.sendEmptyMessage(countdown_count);
            }
        }
    }
}
