package com.remair.heixiu;

import android.content.Context;
import android.widget.Toast;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.TIMUserStatusListener;
import org.simple.eventbus.EventBus;

/**
 * 登录的数据处理类
 */
public class LoginHelper {
    private Context mContext;
    private Context maContext;
    private static final String TAG = LoginHelper.class.getSimpleName();
    private LoginView mLoginView;
    private LogoutView mLogoutView;
    private int RoomId = -1;


    public LoginHelper(Context context) {
        mContext = context;
    }


    public LoginHelper(Context context, LoginView loginView) {
        maContext = context;
        mContext = context.getApplicationContext();
        mLoginView = loginView;
        SharedPreferenceUtil.setContext(context);
    }


    public LoginHelper(Context context, LogoutView logoutView) {
        mContext = context;
        mLogoutView = logoutView;
    }


    /**
     * 登录imsdk
     *
     * @param identify 用户id
     * @param userSig 用户签名
     */
    public void imLogin(String identify, String userSig) {
        final UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            if (mLoginView != null) {
                mLoginView.loginFail();
            }
            return;
        }
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(DemoConstants.ACCOUNTTYPE));
        user.setAppIdAt3rd(String.valueOf(DemoConstants.APPID));
        user.setIdentifier(identify);

        //设置用户状态变更监听器，在回调中进行相应的处理
        TIMManager.getInstance()
                  .setUserStatusListener(new TIMUserStatusListener() {
                      @Override
                      public void onForceOffline() {
                          //被踢下线主线成
                          EventBus.getDefault()
                                  .post("被踢下线", EventConstants.UPDATE_EXIT_LOGING);
                      }


                      public void onUserSigExpired() {
                          //票据过期，需要换票后重新登录
                      }
                  });

        //发起登录请求
        TIMManager.getInstance()
                  .login(DemoConstants.APPID, user, userSig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
                          new TIMCallBack() {
                              @Override
                              public void onError(int i, String s) {
                                  if (i == 70001) {
                                      Toast.makeText(mContext, "账号过期，请重新登录", Toast.LENGTH_LONG)
                                           .show();
                                  }
                                  if (mLoginView != null) {
                                      mLoginView.loginFail();
                                  }
                              }


                              @Override
                              public void onSuccess() {

                                  TIMFriendshipManager.getInstance()
                                                      .setNickName(userInfo.nickname, new TIMCallBack() {
                                                          @Override
                                                          public void onError(int i, String s) {

                                                          }


                                                          @Override
                                                          public void onSuccess() {

                                                          }
                                                      });
                                  startAVSDK();
                              }
                          });
    }


    /**
     * 退出imsdk
     * <p/>
     * 退出成功会调用退出AVSDK
     */
    public void imLogout() {
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (mLoginView != null) {
                    mLogoutView.logoutFail();
                }
            }


            @Override
            public void onSuccess() {
                stopAVSDK();
            }
        });
    }


    /**
     * 初始化AVSDK
     */
    private void startAVSDK() {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        HXApp.getInstance().getQavsdkControl().setAvConfig(DemoConstants.APPID,
                "" + DemoConstants.ACCOUNTTYPE,
                userInfo.user_id + "", userInfo.tlsSig);
        HXApp.getInstance().getQavsdkControl().startContext();
        if (mLoginView != null) {
            mLoginView.loginSucc();
        }
    }


    /**
     * 反初始化AVADK
     */
    public void stopAVSDK() {
        HXApp.getInstance().getQavsdkControl().stopContext();
        if (mLogoutView != null) {
            mLogoutView.logoutSucc();
        }
    }


    public void onDestory() {
        mLoginView = null;
        mLogoutView = null;
        mContext = null;
    }
}
