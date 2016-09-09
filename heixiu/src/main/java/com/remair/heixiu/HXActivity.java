package com.remair.heixiu;

import android.os.Bundle;
import com.remair.heixiu.view.LoadingDialog;
import com.umeng.analytics.MobclickAgent;
import studio.archangel.toolkitv2.activities.AngelActivity;


/**
 * Created by Michael
 * base activity for this project
 */
public class HXActivity extends AngelActivity {
    public LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HXApp.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        AVAnalytics.onResume(this);
        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        AVAnalytics.onPause(this);
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (dialog != null) {
            dialog.todismiss();
        }
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
        return getResources().getColor(R.color.trans);
    }

}
