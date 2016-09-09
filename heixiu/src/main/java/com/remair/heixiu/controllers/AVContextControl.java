package com.remair.heixiu.controllers;

import android.content.Context;
import android.content.Intent;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.utils.Utils;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVError;
import com.tencent.openqq.IMSdkInt;
import studio.archangel.toolkitv2.util.Logger;

class AVContextControl {
    private static final String TAG = "AvContextControl";
    private boolean mIsInStartContext = false;
    private boolean mIsInStopContext = false;
    private Context mContext;
    public AVContext mAVContext = null;
    private String mSelfIdentifier = "";
    private String mPeerIdentifier = "";
    public AVContext.Config mConfig = null;
    private String mUserSig = "";
    /**
     * 启动SDK系统的回调函数
     */
    private AVContext.StartCallback mStartContextCompleteCallback = new AVContext.StartCallback() {
        public void OnComplete(int result) {
            mIsInStartContext = false;
            Logger.out(
                    "WL_DEBUG mStartContextCompleteCallback.OnComplete result = " +
                            result);
            if (result != AVError.AV_OK) {
                mAVContext = null;
            }
            mContext.sendBroadcast(new Intent(Utils.ACTION_START_CONTEXT_COMPLETE_NEW)
                    .putExtra(Utils.EXTRA_AV_ERROR_RESULT, result));
        }
    };

    /**
     * 关闭SDK系统的回调函数
     */
    private AVContext.StopCallback mStopContextCompleteCallback = new AVContext.StopCallback() {
        public void OnComplete() {
            logout();
        }
    };


    AVContextControl(Context context) {
        mContext = context;
    }


    /**
     * 启动SDK系统
     *
     * @param identifier 用户身份的唯一标识
     * @param usersig 用户身份的校验信息
     */
    int startContext(String identifier, String usersig, String tag) {
        int result = AVError.AV_OK;
        if (!hasAVContext()) {
            Logger.out("WL_DEBUG startContext identifier = " + identifier);
            Logger.out("WL_DEBUG startContext usersig = " + usersig);

            mConfig = new AVContext.Config();
            mConfig.sdkAppId = DemoConstants.APPID;
            mConfig.accountType = DemoConstants.ACCOUNTTYPE;
            mConfig.appIdAt3rd = Integer.toString(DemoConstants.APPID);
            mConfig.identifier = identifier;
            mUserSig = usersig;
            login();
        } else {
            return AVError.AV_ERR_FAILED;
        }
        return result;
    }


    /**
     * 启动AVSDK系统
     *
     * @return 0 代表成功
     */
    int startContext() {
        int result = AVError.AV_OK;
        if (!hasAVContext()) {
            onLogin(true, IMSdkInt.get().getTinyId(), 0);
        } else {
            return AVError.AV_ERR_FAILED;
        }
        return result;
    }


    /**
     * 关闭SDK系统
     */
    void stopContext() {
        if (hasAVContext()) {
            Logger.out("WL_DEBUG stopContext");
            //判断stop
            int stop = mAVContext.stop(mStopContextCompleteCallback);
            Logger.out("stopAVContext " + stop);
            mIsInStopContext = true;
        }
    }


    /**
     * 设置AVSDK参数
     */
    public void setAVConfig(int appid, String accountype, String identifier, String usersig) {
        mConfig = new AVContext.Config();
        mConfig.sdkAppId = appid;
        mConfig.accountType = accountype;
        mConfig.appIdAt3rd = Integer.toString(appid);
        mConfig.identifier = identifier;
        mUserSig = usersig;
    }


    boolean getIsInStartContext() {
        return mIsInStartContext;
    }


    boolean getIsInStopContext() {
        return mIsInStopContext;
    }


    boolean setIsInStopContext(boolean isInStopContext) {
        return this.mIsInStopContext = isInStopContext;
    }


    boolean hasAVContext() {
        return mAVContext != null;
    }


    AVContext getAVContext() {
        return mAVContext;
    }


    void setAvContext(AVContext avContext) {
        this.mAVContext = avContext;
    }


    public String getSelfIdentifier() {
        return mSelfIdentifier;
    }


    String getPeerIdentifier() {
        return mPeerIdentifier;
    }


    void setPeerIdentifier(String peerIdentifier) {
        mPeerIdentifier = peerIdentifier;
    }


    /**
     * 消息系统登录
     */
    private void login() {
        TIMUser userId = new TIMUser();
        userId.setAccountType(DemoConstants.ACCOUNTTYPE);
        userId.setAppIdAt3rd(mConfig.appIdAt3rd);
        userId.setIdentifier(mConfig.identifier);

        /**
         * 登陆所需信息
         * 1.sdkAppId ： 创建应用时页面上分配的 sdkappid
         * 2.uid ： 创建应用账号集成配置页面上分配的 accounttype
         * 3.app_id_at3rd ： 第三方开放平台账号 appid，如果是自有的账号，那么直接填 sdkappid 的字符串形式
         * 4.identifier ：用户标示符，也就是我们常说的用户 id
         * 5.user_sig ：使用 tls 后台 api tls_gen_signature_ex 或者工具生成的 user_sig
         */
        TIMManager.getInstance()
                  .login(mConfig.sdkAppId, userId, mUserSig, new TIMCallBack() {

                      @Override
                      public void onSuccess() {
                          Logger.out("init successfully. tiny id = " +
                                  IMSdkInt.get().getTinyId());
                          onLogin(true, IMSdkInt.get().getTinyId(), 0);
                      }


                      @Override
                      public void onError(int code, String desc) {
                          Logger.out(
                                  "init failed, imsdk error code  = " + code +
                                          ", desc = " + desc);
                          onLogin(false, 0, code);
                      }
                  });
    }


    private void onLogin(boolean result, long tinyId, int errorCode) {
        if (result) {
            mAVContext = AVContext.createInstance(mContext, mConfig);
            Logger.out("WL_DEBUG startContext mAVContext is null? " +
                    (mAVContext == null));
            mSelfIdentifier = mConfig.identifier;
            int ret = mAVContext.start(mStartContextCompleteCallback);
            Logger.out("onLogin+ret:" + ret);
            mIsInStartContext = true;
        } else {
            mStartContextCompleteCallback.OnComplete(errorCode);
        }
    }


    private void logout() {

        TIMManager.getInstance().logout();
        onLogout(true);
    }


    private void onLogout(boolean result) {
        Logger.out("WL_DEBUG mStopContextCompleteCallback.OnComplete");
        if (mAVContext != null) {
            mAVContext.destroy();
        }
        mAVContext = null;
        Logger.out("WL_DEBUG mStopContextCompleteCallback mAVContext is null");
        mIsInStopContext = false;
        mContext.sendBroadcast(new Intent(Utils.ACTION_CLOSE_CONTEXT_COMPLETE));
    }
}