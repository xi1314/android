package com.remair.heixiu.utils;

import android.view.View;
import com.jakewharton.rxbinding.view.RxView;
import java.util.concurrent.TimeUnit;
import rx.functions.Action1;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/10 17:39
 * 修改人：LiuJun
 * 修改时间：16/8/10 17:39
 * 修改备注：
 */
public class RxViewUtil {

    public static void viewBindClick(final View view, final View.OnClickListener onClickListener) {
        if (view == null || onClickListener == null) {
            return;
        }
        viewBindClick(view, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (onClickListener != null) {
                    onClickListener.onClick(view);
                }
            }
        });
    }
   public static void viewBindClick(final View view, final View
            .OnLongClickListener onClickListener) {
        if (view == null || onClickListener == null) {
            return;
        }
        viewBindClick(view, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (onClickListener != null) {
                    onClickListener.onLongClick(view);
                }
            }
        });
    }
    public static void viewBindClick(final View view, Action1<Void> action1) {
        if (view == null || action1 == null) {
            return;
        }
        RxView.clicks(view).throttleFirst(1, TimeUnit.SECONDS)
              .subscribe(action1);
    }


}
