package com.remair.heixiu;

import android.os.Bundle;
import com.umeng.analytics.MobclickAgent;
import studio.archangel.toolkitv2.activities.AngelActivityV4;

/**
 * Created by Michael
 * base activity for this project
 */
public class HXActivityV4 extends AngelActivityV4 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HXApp.getInstance().addActivity(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //        AVAnalytics.onResume(this);
        //        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //        AVAnalytics.onPause(this);
        //        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        HXApp.getInstance().finishActivity(this);
    }


    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(0, R.anim.fade_out);
    }


    @Override
    public int getDefaultStatusBarColor() {
        return getResources().getColor(R.color.hx_main);
        //        return R.drawable.shape_main_background;
    }
}
