package com.remair.heixiu.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import studio.archangel.toolkitv2.util.image.ImageProvider;

/**
 * Created by Michael
 * entrance of APP
 */
public class LaunchActivity extends HXBaseActivity {
    @Override
    protected void initUI() {

    }


    @Override
    protected void initData() {
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        if (click != null) { // 判断是否来自信鸽的打开方式
            HXApp.customcontent = click.getCustomContent();
        }
        Intent it = new Intent(this, SplashActivity.class);
        it.putExtras(getIntent());

        String folder_name = "instruction";
        AssetManager am = getAssets();
        String[] list;
        try {
            list = am.list(folder_name);
            for (String name : list) {
                String uri = "asset:///" + folder_name + "/" + name;
                ImageProvider.preload(uri);
                //                ImageProvider.loadImage(uri);/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(it);
        finish();
    }
}
