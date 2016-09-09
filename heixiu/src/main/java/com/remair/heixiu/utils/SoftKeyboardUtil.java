package com.remair.heixiu.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import java.lang.ref.WeakReference;
import studio.archangel.toolkitv2.util.Logger;

/**
 * Created by JXHIUUI on 2016/3/28.
 */
public class SoftKeyboardUtil {
    public static void observeSoftKeyboard(Activity activity, final OnSoftKeyboardChangeListener listener) {
        WeakReference<Activity> weakReference = new WeakReference<Activity>(activity);
        //        final WeakReference<OnSoftKeyboardChangeListener> weakReferenceListener = new WeakReference<>(listener);
        if (weakReference.get() == null) {
            return;
        }
        final View decorView = weakReference.get().getWindow().getDecorView();
        decorView.getViewTreeObserver()
                 .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                     int previousKeyboardHeight = -1;


                     @Override
                     public void onGlobalLayout() {
                         Rect rect = new Rect();
                         decorView.getWindowVisibleDisplayFrame(rect);
                         int displayHeight = rect.bottom - rect.top;
                         int height = decorView.getHeight();
                         int keyboardHeight = height - displayHeight;
                         if (previousKeyboardHeight != keyboardHeight) {
                             boolean hide = (double) displayHeight / height >
                                     0.8;
                             //                    if (weakReferenceListener.get()!=null) {
                             listener.onSoftKeyBoardChange(keyboardHeight, !hide);
                             //                    }
                         }
                         previousKeyboardHeight = keyboardHeight;
                         Logger.out("onGlobalLayout" + keyboardHeight);
                     }
                 });
    }


    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
    }
}