/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.remair.heixiu.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.HXApp;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.text.Notifier;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, DemoConstants.WX_APPID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        boolean b = api.handleIntent(getIntent(), this);
        Toast.makeText(getApplicationContext(), b + "", Toast.LENGTH_SHORT)
             .show();
        if (!api.handleIntent(getIntent(), this)) {
            finish();
        }
    }


    @Override
    public void onReq(BaseReq req) {
        int type = req.getType();
    }


    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0: {
                    Notifier.showShortMsg(getApplication(), "支付成功");
                    setResult(HXApp.result_ok);
                    break;
                }
                case -1: {
                    Notifier.showShortMsg(getApplication(), "支付失败");
                    setResult(HXApp.result_fail);
                    Logger.out(resp.errStr);
                    break;
                }
                case -2: {
                    //用户取消
                    Notifier.showShortMsg(getApplication(), "取消支付");
                    break;
                }
            }
        }

        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}