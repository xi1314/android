package studio.archangel.toolkitv2.util;

/**
 * Created by Michael on 2015/6/27.
 */

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class AngelCountDownTimer {

    private static final int message = 1;
    private final long future_ms;
    private final long interval;
    private long next;
    private long future_stop_time;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            synchronized (AngelCountDownTimer.this) {
                final long millisLeft = future_stop_time - SystemClock.uptimeMillis();

                if (millisLeft <= 0) {
                    onFinish();
                } else {
                    onTick(millisLeft);

                    long currentTime = SystemClock.uptimeMillis();
                    do {
                        next += interval;
                    } while (currentTime > next);

                    if (next < future_stop_time)
                        sendMessageAtTime(obtainMessage(message), next);
                    else
                        sendMessageAtTime(obtainMessage(message), future_stop_time);
                }
            }
        }
    };

    public AngelCountDownTimer(long millisInFuture, long countDownInterval) {
        future_ms = millisInFuture;
        interval = countDownInterval;
    }

    public final void cancel() {
        mHandler.removeMessages(message);
    }

    public synchronized final AngelCountDownTimer start() {
        if (future_ms <= 0) {
            onFinish();
            return this;
        }

        next = SystemClock.uptimeMillis();
        future_stop_time = next + future_ms;

        next += interval;
        mHandler.sendMessageAtTime(mHandler.obtainMessage(message), next);
        return this;
    }

    public abstract void onTick(long millisUntilFinished);

    public abstract void onFinish();
}