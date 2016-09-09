package com.remair.heixiu.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.BuildConfig;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.EventConstants;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.LoginHelper;
import com.remair.heixiu.LogoutView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.bean.AuthStatusBean;
import com.remair.heixiu.bean.ConfigBean;
import com.remair.heixiu.bean.LoginCommonBean;
import com.remair.heixiu.bean.NewPerMesInfo;
import com.remair.heixiu.bean.RoomNumBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.controllers.QavsdkControl;
import com.remair.heixiu.fragment.FindFragment;
import com.remair.heixiu.fragment.HomeFragment;
import com.remair.heixiu.fragment.MineFragment;
import com.remair.heixiu.fragment.PrivateLetterFragment;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.service.LocationService;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.BadgeView;
import com.remair.heixiu.view.LoadingDialog;
import com.remair.heixiu.view.ShowDialog;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.av.sdk.AVError;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import rx.Subscription;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelDialog;
import studio.archangel.toolkitv2.views.AngelLoadingDialog;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * Created by Michael
 * home page
 */
public class HomeActivity extends HXBaseActivity implements View.OnClickListener, LogoutView {
    @BindView(R.id.main_radio) RadioGroup main_radio;
    @BindView(R.id.home_page) RadioButton home_page;
    @BindView(R.id.rb_player) RadioButton rb_player;
    @BindView(R.id.rb_main) RadioButton rb_main;
    @BindView(R.id.rb_findfragment) RadioButton rb_findfragment;
    @BindView(R.id.rb_privatechat) RadioButton rb_privatechat;
    @BindView(R.id.bt) Button bt;
    boolean loading_info = false;
    private UserInfo mUserInfo;
    private long exitTime = 0;
    public AngelDialog angelDialog;
    public static int grade = 1;//点击的房间主播等级
    AlertDialog dialogtwo;
    ShowDialog showDialog;
    private int auth_control;
    private Context mContext;
    private QavsdkControl mQavsdkControl;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private MineFragment mineFragment;
    private FindFragment findfragment;
    private PrivateLetterFragment privateletterfragment;
    public int flag = 0;
    private BadgeView badge;
    private ShowDialog updataDialog;
    private LoadingDialog loaddialog;
    private SharedPreferences sp;
    private AngelLoadingDialog dialog;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.out("jxh:action:" + action);
            if (action.equals(Utils.ACTION_START_CONTEXT_COMPLETE_NEW)) {
                Xgavactivity();
            }
            if (action.equals(Utils.ACTION_CLOSE_CONTEXT_COMPLETE)) {
                String loginUser = TIMManager.getInstance().getLoginUser();
                Logger.out("jxh:loginUser:" + loginUser);
                HXApp.getInstance().isStartContext = false;
                startContext(mUserInfo.tlsSig,
                        mUserInfo.user_id + "", "HomeActivity");
            }
            if (action.equals("heixiu.offlinemessage")) {
                long offlinemessage = intent.getLongExtra("oflinemessage", 0);
                if (offlinemessage > 0) {
                    badge.setText(offlinemessage + "");
                    bt.setVisibility(View.VISIBLE);
                    if (badge.isShown()) {
                        badge.increment(1);
                    } else {
                        badge.show();
                    }
                    badge.setText(offlinemessage + "");
                } else {
                    bt.setVisibility(View.INVISIBLE);
                    badge.hide();
                }
            }
        }
    };


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
    }


    private LoginHelper loginHelper;


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void initUI() {
        mUserInfo = HXApp.getInstance().getUserInfo();
        if (mUserInfo == null) {
            loginHelper.imLogout();
            startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            finish();
            return;
        }
        sp = getSharedPreferences(DemoConstants.LOCAL_DATA, MODE_PRIVATE);
        mQavsdkControl = HXApp.getInstance().getQavsdkControl();
        mContext = getApplicationContext();
        loginHelper = new LoginHelper(mContext, this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.ACTION_CLOSE_CONTEXT_COMPLETE);
        intentFilter.addAction(Utils.ACTION_CREATE_ROOM_NUM_COMPLETE);
        intentFilter.addAction(Utils.ACTION_START_CONTEXT_COMPLETE_NEW);
        intentFilter.addAction("heixiu.offlinemessage");
        registerReceiver(broadcastReceiver, intentFilter);
        setContentView(R.layout.act_home_cofy);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        EventBus.getDefault().register(this);
        final Context context = getApplicationContext();
        // 开启logcat输出，方便debug，发布时请关闭
        // XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        // 其它常用的API：
        // 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
        // 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
        // 反注册（不再接收消息）：unregisterPush(context)
        // 设置标签：setTag(context, tagName)
        // 删除标签：deleteTag(context, tagName)
        XGPushManager.registerPush(context,
                "push" + mUserInfo.user_id, new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object o, int i) {
                        Logger.out("token:" + o);
                    }


                    @Override
                    public void onFail(Object o, int i, String s) {
                    }
                });

        XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();
        // 设置自定义通知layout,通知背景等可以在layout里设置
        build.setLayoutId(R.layout.notification);
        // 设置自定义通知内容id
        build.setLayoutTextId(R.id.content);
        // 设置自定义通知标题id
        build.setLayoutTitleId(R.id.title);
        // 设置自定义通知图片id
        build.setLayoutIconId(R.id.icon);
        // 设置自定义通知图片资源
        build.setLayoutIconDrawableId(R.drawable.icon_laucher);
        // 设置状态栏的通知小图标
        build.setIcon(R.drawable.icon_laucher);
        // 设置时间id
        build.setLayoutTimeId(R.id.time);
        // 若不设定以上自定义layout，又想简单指定通知栏图片资源
        // 客户端保存build_id
        XGPushManager.setPushNotificationBuilder(context, 1, build);

        XGPushConfig.enableDebug(this, false);
        XGPushManager.cancelAllNotifaction(this);
        int user_id = getIntent().getIntExtra("user_id", -1);
        if (user_id != -1) {
            Intent intent = new Intent(mActivity, TuiJianActivity.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        }
        //高德定位服务
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }


    @Override
    protected void initData() {
        HXHttpUtil.getInstance().loginCommon(mUserInfo.user_id)
                  .subscribe(new HttpSubscriber<LoginCommonBean>() {
                      @Override
                      public void onNext(LoginCommonBean loginCommonBean) {
                          boolean isLogin = false;
                          isLogin = loginCommonBean.is_login;
                          if (!isLogin) {
                              overdueAccount();
                          }
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
                          overdueAccount();
                      }
                  });
        HXHttpUtil.getInstance().config(BuildConfig.VERSION_NAME, 0)
                  .subscribe(new HttpSubscriber<ConfigBean>() {
                      @Override
                      public void onNext(ConfigBean configBean) {
                          HomeActivity homeActivity = (HomeActivity) mActivity;
                          if (homeActivity == null) {
                              return;
                          }
                          HXApp.getInstance().configMessage = configBean;
                          String force_update = configBean.android.force_update;
                          String update_info = configBean.android.update_info;
                          final String apk_url = configBean.android.apk_url;
                          SharedPreferenceUtil.setContext(mContext);
                          SharedPreferenceUtil.putString("apk_url", apk_url);
                          if (force_update.equals("0")) {//不提示

                          } else if (force_update.equals("1")) {//不强制升级
                              if (!"".equals(update_info)) {
                                  updataDialog = new ShowDialog(mActivity, update_info, "马上更新", "稍后更新");
                                  updataDialog.setbackGround();
                              } else {
                                  updataDialog = new ShowDialog(mActivity, update_info, "马上更新", "");
                                  updataDialog.setbackGround();
                              }
                              final HomeActivity finalHomeActivity = homeActivity;
                              updataDialog
                                      .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                          @Override
                                          public void doConfirm() {
                                              updataDialog.dismiss();
                                              dialog = new AngelLoadingDialog(mActivity, R.color.hx_main, false);
                                              dialog.setContentTextView("正在下载");
                                              dialog.show();
                                              finalHomeActivity
                                                      .download(apk_url, "heixiu.apk", false);
                                          }


                                          @Override
                                          public void doCancel() {

                                              updataDialog.dismiss();
                                          }
                                      });
                              updataDialog.show();
                          } else if (force_update.equals("2")) { //强制升级
                              updataDialog = new ShowDialog(mActivity, update_info, "马上更新", "稍后更新");
                              updataDialog.setbackGround();
                              final HomeActivity finalHomeActivity1 = homeActivity;
                              updataDialog
                                      .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                          @Override
                                          public void doConfirm() {
                                              updataDialog.dismiss();
                                              dialog = new AngelLoadingDialog(mActivity, R.color.hx_main, false);
                                              dialog.setContentTextView("正在下载");
                                              dialog.show();
                                              finalHomeActivity1
                                                      .download(apk_url, "heixiu.apk", true);
                                          }


                                          @Override
                                          public void doCancel() {
                                              finalHomeActivity1
                                                      .forcedExit(getApplication());
                                          }
                                      });
                              updataDialog
                                      .setOnKeyListener(new DialogInterface.OnKeyListener() {
                                          @Override
                                          public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                              if (keyCode ==
                                                      KeyEvent.KEYCODE_SEARCH) {
                                                  return true;
                                              } else {
                                                  return true; //默认返回 false，这里false不能屏蔽返回键，改成true就可以了
                                              }
                                          }
                                      });

                              updataDialog.show();
                          }
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
                      }
                  });
        getUserInfo();

        Xgavactivity();

        setChioceItem(0);
        home_page.setChecked(true);
        Drawable drawableAdd = getResources()
                .getDrawable(R.drawable.selector_oval_main);
        int px = (int) getResources().getDimension(R.dimen.heart_anim_init_x);
        drawableAdd.setBounds(0, 0, px, px);
        rb_player.setCompoundDrawables(null, drawableAdd, null, null);
        main_radio
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        switch (checkedId) {
                            case R.id.home_page:
                                flag = 0;
                                setChioceItem(0);
                                break;
                            case R.id.rb_player:
                                if (flag == 0) {
                                    home_page.setChecked(true);
                                } else if (flag == 1) {
                                    rb_findfragment.setChecked(true);
                                } else if (flag == 2) {
                                    rb_privatechat.setChecked(true);
                                } else if (flag == 3) {
                                    rb_main.setChecked(true);
                                }
                                rb_player.setChecked(false);
                                if (mUserInfo.forbid == 1) {
                                    showDialog = new ShowDialog(HomeActivity.this, "您已被封号，不能直播，谢谢！", "退出", "");
                                    showDialog.show();
                                    showDialog
                                            .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                                @Override
                                                public void doConfirm() {
                                                    showDialog.dismiss();
                                                }


                                                @Override
                                                public void doCancel() {

                                                }
                                            });
                                    return;
                                }
                                showDiagle();
                                break;
                            case R.id.rb_findfragment:
                                flag = 1;
                                setChioceItem(1);
                                break;
                            case R.id.rb_privatechat:
                                flag = 2;
                                setChioceItem(2);
                                break;
                            case R.id.rb_main:
                                flag = 3;
                                setChioceItem(3);
                                break;
                        }
                    }
                });
        badge = new BadgeView(this, bt);
    }


    @Subscriber(tag = EventConstants.UPDATE_EXIT_LOGING)
    public void onEvent(String event) {

        //退出AV那边群
        if ((mQavsdkControl != null) &&
                (mQavsdkControl.getAVContext() != null) &&
                (mQavsdkControl.getAVContext().getAudioCtrl() != null)) {
            mQavsdkControl.getAVContext().getAudioCtrl().stopTRAEService();
            mQavsdkControl.exitRoom();
        }
        HXApp.getInstance().setUserInfo(null);
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }


            @Override
            public void onSuccess() {

            }
        });

        HXApp.getInstance().finishAllActivity();
        final Intent intent = getPackageManager()
                .getLaunchIntentForPackage(getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EventConstants.ISONFORCEOFFLINE, true);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之

        //finish();
        HXApp.getInstance().goLogin(false, true);
    }


    private void overdueAccount() {
        HXApp.getInstance().setUserInfo(null);
        startActivity(new Intent(mActivity, LoginActivity.class));
        Notifier.showShortMsg(mContext, "账号过期请重新登录");
        finish();
        HXApp.getInstance().goLogin(true, false);
    }


    private void showDiagle() {
        HXHttpUtil.getInstance()
                  .authStatus(HXApp.getInstance().getUserInfo().user_id)
                  .subscribe(new HttpSubscriber<AuthStatusBean>() {
                      @Override
                      public void onNext(AuthStatusBean authStatusBean) {
                          int status = authStatusBean.status;
                          auth_control = HXApp.getInstance().configMessage.auth_control;
                          if (auth_control == 1) {
                              if (status == 3) {//未提交审核
                                  showAttestationDialog();
                              } else if (status == 4) {//提交过小框
                                  int agecount = sp.getInt("agecount", 19);
                                  if (agecount < 18) {//未满18周岁
                                      showDialog = new ShowDialog(HomeActivity.this, getResources()
                                              .getString(R.string.no_age_old), "知道哟！", "");
                                      showDialog.show();
                                      showDialog
                                              .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                                  @Override
                                                  public void doConfirm() {
                                                      showDialog.dismiss();
                                                  }


                                                  @Override
                                                  public void doCancel() {
                                                  }
                                              });
                                      return;
                                  }
                                  if (mUserInfo.grade >=
                                          HXApp.getInstance().configMessage.live_grade_limit ||
                                          mUserInfo.type == 2) {
                                      createRoom();
                                  } else {
                                      showDialog = new ShowDialog(HomeActivity.this,
                                              "等级在" +
                                                      HXApp.getInstance().configMessage.live_grade_limit +
                                                      "级以下的用户不能直播哦~多看直播和刷礼物可以快速升级的！", "知道哟！", "");
                                      showDialog.show();
                                      showDialog
                                              .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                                  @Override
                                                  public void doConfirm() {
                                                      showDialog.dismiss();
                                                  }


                                                  @Override
                                                  public void doCancel() {
                                                  }
                                              });
                                  }
                              } else {
                                  if (mUserInfo.grade >=
                                          HXApp.getInstance().configMessage.live_grade_limit ||
                                          mUserInfo.type == 2) {
                                      createRoom();
                                  } else {
                                      showDialog = new ShowDialog(HomeActivity.this,
                                              "等级在" +
                                                      HXApp.getInstance().configMessage.live_grade_limit +
                                                      "级以下的用户不能直播哦~多看直播和刷礼物可以快速升级的！", "知道哟！", "");
                                      showDialog.show();
                                      showDialog
                                              .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                                  @Override
                                                  public void doConfirm() {
                                                      showDialog.dismiss();
                                                  }


                                                  @Override
                                                  public void doCancel() {
                                                  }
                                              });
                                  }
                              }
                          } else if (auth_control == 0) {
                              createRoom();
                          }
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
                      }
                  });
    }


    private void showAttestationDialog() {
        if (isFinishing()) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View view = inflater.inflate(R.layout.showattestiondialog, null);
        dialogtwo = new AlertDialog.Builder(HomeActivity.this, R.style.dialog)
                .create();
        dialogtwo.setView(view);
        if (isFinishing()) {
            return;
        }
        dialogtwo.show();
        dialogtwo.getWindow()
                 .setLayout(getKjwidth(R.dimen.width_marginto), getKjwidth(R.dimen.height_marginto));
        Window window = dialogtwo.getWindow();
        window.setContentView(R.layout.showattestiondialog);
        final EditText editText = (EditText) dialogtwo
                .findViewById(R.id.et_name);
        final EditText et_id_cart = (EditText) dialogtwo
                .findViewById(R.id.et_id_cart);
        final ImageView iv_correct = (ImageView) dialogtwo
                .findViewById(R.id.iv_correct);
        final ImageView iv_error = (ImageView) dialogtwo
                .findViewById(R.id.iv_error);
        final RelativeLayout rl_correct = (RelativeLayout) dialogtwo
                .findViewById(R.id.rl_correct);
        final RelativeLayout rl_error = (RelativeLayout) dialogtwo
                .findViewById(R.id.rl_error);
        AngelMaterialButton btn_sure = (AngelMaterialButton) dialogtwo
                .findViewById(R.id.btn_sure);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    iv_correct.setVisibility(View.VISIBLE);
                } else {
                    iv_correct.setVisibility(View.GONE);
                }
            }
        });

        et_id_cart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 18) {
                    iv_error.setVisibility(View.VISIBLE);
                    rl_error.setBackgroundResource(R.drawable.shape_attesation_dialg);
                } else {
                    iv_error.setVisibility(View.GONE);
                    rl_error.setBackgroundResource(R.drawable.shape_attestion_error);
                }
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editText.getText().toString().trim();
                final String identity = et_id_cart.getText().toString().trim();
                if (name.isEmpty()) {
                    Notifier.showShortMsg(mContext, "姓名不能为空");
                    return;
                }
                if (identity.isEmpty()) {
                    Notifier.showShortMsg(mContext, "身份证号不能为空");
                    return;
                }
                showDialog = new ShowDialog(HomeActivity.this, getResources()
                        .getString(R.string.profit_attestation), getResources()
                        .getString(R.string.profit_sure), getResources()
                        .getString(R.string.profit_later));
                showDialog.show();
                showDialog
                        .setClicklistener(new ShowDialog.ClickListenerInterface() {
                            @Override
                            public void doConfirm() {
                                UserInfo userInfo = HXApp.getInstance()
                                                         .getUserInfo();
                                if (userInfo == null) {
                                    overdueAccount();
                                    return;
                                }
                                HXHttpUtil.getInstance()
                                          .firstSimpleAuth(userInfo.user_id, name, identity)
                                          .subscribe(new HttpSubscriber<Object>() {
                                              @Override
                                              public void onNext(Object o) {
                                                  showDialog = new ShowDialog(HomeActivity.this, getResources()
                                                          .getString(R.string.profit_message), getResources()
                                                          .getString(R.string.profit_sure), getResources()
                                                          .getString(R.string.profit_later));
                                                  showDialog.show();
                                                  showDialog
                                                          .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                                              @Override
                                                              public void doConfirm() {
                                                                  showDialog
                                                                          .dismiss();
                                                                  String oldAge = identity
                                                                          .substring(6, 10);
                                                                  String thisYers = Utils
                                                                          .getStringYers();
                                                                  int agecount =
                                                                          Integer.parseInt(thisYers) -
                                                                                  Integer.parseInt(oldAge);
                                                                  sp.edit()
                                                                    .putInt("agecount", agecount);
                                                                  if (agecount <
                                                                          18) {//未满18周岁
                                                                      showDialog = new ShowDialog(HomeActivity.this, getResources()
                                                                              .getString(R.string.no_age_old), "知道哟！", "");
                                                                      showDialog
                                                                              .show();
                                                                      showDialog
                                                                              .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                                                                  @Override
                                                                                  public void doConfirm() {
                                                                                      showDialog
                                                                                              .dismiss();
                                                                                  }


                                                                                  @Override
                                                                                  public void doCancel() {

                                                                                  }
                                                                              });
                                                                  } else {
                                                                      //判断等级
                                                                      if (mUserInfo.grade >=
                                                                              HXApp.getInstance().configMessage.live_grade_limit) {
                                                                          createRoom();
                                                                          dialogtwo
                                                                                  .dismiss();
                                                                      } else {
                                                                          showDialog = new ShowDialog(HomeActivity.this,
                                                                                  "等级在" +
                                                                                          HXApp.getInstance().configMessage.live_grade_limit +
                                                                                          "级以下的用户不能直播哦~多看直播和刷礼物可以快速升级的！", "知道哟！", "");
                                                                          showDialog
                                                                                  .show();
                                                                          showDialog
                                                                                  .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                                                                      @Override
                                                                                      public void doConfirm() {
                                                                                          showDialog
                                                                                                  .dismiss();
                                                                                      }


                                                                                      @Override
                                                                                      public void doCancel() {

                                                                                      }
                                                                                  });
                                                                      }
                                                                  }
                                                              }


                                                              @Override
                                                              public void doCancel() {
                                                                  showDialog
                                                                          .dismiss();
                                                              }
                                                          });
                                                  dialogtwo.dismiss();
                                              }


                                              @Override
                                              public void onError(Throwable e) {
                                                  super.onError(e);
                                                  showDialog = new ShowDialog(HomeActivity.this, getResources()
                                                          .getString(R.string.profit_message_error), getResources()
                                                          .getString(R.string.profit_sure), getResources()
                                                          .getString(R.string.profit_later));
                                                  showDialog.show();
                                                  showDialog
                                                          .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                                              @Override
                                                              public void doConfirm() {
                                                                  //重新提交
                                                                  showDialog
                                                                          .dismiss();
                                                              }


                                                              @Override
                                                              public void doCancel() {
                                                                  showDialog
                                                                          .dismiss();
                                                              }
                                                          });
                                              }
                                          });
                                //HashMap<String, Object> para = new HashMap<>();
                                //para.put("user_id", userInfo.user_id);
                                //para.put("real_name", name);
                                //para.put("id_card_no", identity);
                                //HXJavaNet
                                //        .post(HXJavaNet.url_firstsimpleauth, para, new AngelNetCallBack() {
                                //            @Override
                                //            public void onSuccess(int ret_code, String ret_data, String msg) {
                                //                if (ret_code == 200) {
                                //                    showDialog = new ShowDialog(HomeActivity.this, getResources()
                                //                            .getString(R.string.profit_message), getResources()
                                //                            .getString(R.string.profit_sure), getResources()
                                //                            .getString(R.string.profit_later));
                                //                    showDialog.show();
                                //                    showDialog
                                //                            .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                //                                @Override
                                //                                public void doConfirm() {
                                //                                    showDialog
                                //                                            .dismiss();
                                //                                    String oldAge = identity
                                //                                            .substring(6, 10);
                                //                                    String thisYers = Utils
                                //                                            .getStringYers();
                                //                                    int agecount =
                                //                                            Integer.parseInt(thisYers) -
                                //                                                    Integer.parseInt(oldAge);
                                //                                    sp.edit()
                                //                                      .putInt("agecount", agecount);
                                //                                    if (agecount <
                                //                                            18) {//未满18周岁
                                //                                        showDialog = new ShowDialog(HomeActivity.this, getResources()
                                //                                                .getString(R.string.no_age_old), "知道哟！", "");
                                //                                        showDialog
                                //                                                .show();
                                //                                        showDialog
                                //                                                .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                //                                                    @Override
                                //                                                    public void doConfirm() {
                                //                                                        showDialog
                                //                                                                .dismiss();
                                //                                                    }
                                //
                                //
                                //                                                    @Override
                                //                                                    public void doCancel() {
                                //
                                //                                                    }
                                //                                                });
                                //                                    } else {
                                //                                        //判断等级
                                //                                        if (mUserInfo.grade >=
                                //                                                HXApp.getInstance().live_grade_limit) {
                                //                                            createRoom();
                                //                                            dialogtwo
                                //                                                    .dismiss();
                                //                                        } else {
                                //                                            showDialog = new ShowDialog(HomeActivity.this,
                                //                                                    "等级在" +
                                //                                                            HXApp.getInstance().live_grade_limit +
                                //                                                            "级以下的用户不能直播哦~多看直播和刷礼物可以快速升级的！", "知道哟！", "");
                                //                                            showDialog
                                //                                                    .show();
                                //                                            showDialog
                                //                                                    .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                //                                                        @Override
                                //                                                        public void doConfirm() {
                                //                                                            showDialog
                                //                                                                    .dismiss();
                                //                                                        }
                                //
                                //
                                //                                                        @Override
                                //                                                        public void doCancel() {
                                //
                                //                                                        }
                                //                                                    });
                                //                                        }
                                //                                    }
                                //                                }
                                //
                                //
                                //                                @Override
                                //                                public void doCancel() {
                                //                                    showDialog
                                //                                            .dismiss();
                                //                                }
                                //                            });
                                //                }
                                //                dialogtwo.dismiss();
                                //            }
                                //
                                //
                                //            @Override
                                //            public void onFailure(String msg) {
                                //                showDialog = new ShowDialog(HomeActivity.this, getResources()
                                //                        .getString(R.string.profit_message_error), getResources()
                                //                        .getString(R.string.profit_sure), getResources()
                                //                        .getString(R.string.profit_later));
                                //                showDialog.show();
                                //                showDialog
                                //                        .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                //                            @Override
                                //                            public void doConfirm() {
                                //                                //重新提交
                                //                                showDialog
                                //                                        .dismiss();
                                //                            }
                                //
                                //
                                //                            @Override
                                //                            public void doCancel() {
                                //                                showDialog
                                //                                        .dismiss();
                                //                            }
                                //                        });
                                //            }
                                //        });
                                showDialog.dismiss();
                            }


                            @Override
                            public void doCancel() {
                                dialogtwo.dismiss();
                                showDialog.dismiss();
                            }
                        });
            }
        });
    }


    public int getKjwidth(int id) {
        Resources r = this.getResources();
        return (int) r.getDimension(id);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mUserInfo == null || mUserInfo.forbid == 2) {
            TIMManager.getInstance().logout();
        }
    }


    private Callback callback;


    //下载
    public void download(String url, String name, final boolean b) {
        callback = new FileCallBack(Environment.getExternalStorageDirectory()
                                               .getAbsolutePath(), name) {
            @Override
            public void onError(Call call, Exception e) {
            }


            @Override
            public void onResponse(final File response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (angelDialog == null) {
                    angelDialog = new AngelDialog(mActivity, "安装更新", "应用下载完成，请点击安装!");
                }
                angelDialog
                        .setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                                    return true;
                                } else {
                                    return true; //默认返回 false，这里false不能屏蔽返回键，改成true就可以了
                                }
                            }
                        });
                angelDialog.setOnOkClickedListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forcedExit(getApplication());
                        String absolutePath = response.getAbsolutePath();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri
                                .fromFile(new File(absolutePath)), "application/vnd.android.package-archive");
                        startActivity(intent);
                    }
                });
                angelDialog
                        .setOnCancelClickedListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (b) {
                                    forcedExit(getApplication());
                                }
                            }
                        });
                angelDialog.show();
                angelDialog.getOkButton().setText("安装");
            }


            @Override
            public void inProgress(float progress, long total) {
                if (dialog != null) {
                    dialog.setMax(100);
                    dialog.setProgress((int) (100 * progress));
                }
            }
        };
        OkHttpUtils.get().url(url).build().execute(callback);
    }


    @Subscriber(tag = "forcedExit")
    public void forcedExit(Object obj) {
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN) {
            if (flag == 1) {
                if (findfragment != null) {
                    //返回webView的上一页面
                    if (findfragment.webView.canGoBack()) {
                        findfragment.webView.goBack();
                        return true;
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Notifier.showShortMsg(mContext, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            HXApp.getInstance().AppExit();
            super.onBackPressed();
        }
    }


    void getUserInfo() {
        if (loading_info) {
            return;
        }
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            overdueAccount();
            return;
        }
        Subscription subscribe = HXHttpUtil.getInstance()
                                           .getUserInfos(userInfo.user_id, userInfo.user_id)
                                           .subscribe(new HttpSubscriber<UserInfo>() {
                                               @Override
                                               public void onStart() {
                                                   super.onStart();
                                                   if (mActivity != null) {
                                                       HomeActivity.this.loading_info = true;
                                                   }
                                               }


                                               @Override
                                               public void onNext(UserInfo userInfo) {
                                                   loading_info = false;
                                                   mUserInfo = userInfo;
                                                   HXApp.getInstance()
                                                        .setUserInfo(mUserInfo);
                                               }


                                               @Override
                                               public void onError(Throwable e) {
                                                   super.onError(e);
                                                   loading_info = false;
                                                   if (mActivity != null) {
                                                       HomeActivity.this.loading_info = false;
                                                   }
                                               }
                                           });
        addSubscription(subscribe);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mQavsdkControl.hasAVContext()) {
            loginHelper.imLogout();
        }
        if (loginHelper != null) {
            loginHelper.onDestory();
        }
        EventBus.getDefault().unregister(this);
        mQavsdkControl.setIsInStopContext(false);
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_attestation:
                Intent inten = new Intent(mActivity, CertificationiActivity.class);
                startActivity(inten);
                dialogtwo.dismiss();
                break;
            case R.id.btn_see_progress://查看状态
                Intent intent = new Intent(mActivity, CertificationiActivity.class);
                startActivity(intent);
                dialogtwo.dismiss();
                break;
            case R.id.btn_in_seeding://立即直播
                createRoom();
                dialogtwo.dismiss();
                break;
            case R.id.image_finsh_log:
                dialogtwo.dismiss();
                break;
        }
    }


    /**
     * qavsdk 注册信息
     */
    private void startContext(String sig, String id, String tag) {
        if (!mQavsdkControl.hasAVContext()) {
            if ("".equals(sig)) {
                finish();
                return;
            }
            int code = mQavsdkControl.startContext(id, sig, tag);
            Logger.out("startContext mLoginErrorCode   " + code);
            if (code != AVError.AV_OK) {
            }
        }
    }


    private NewPerMesInfo messageInfo;


    public void Xgavactivity() {

        if (HXApp.customcontent != null && !"".equals(HXApp.customcontent)) {
            JSONObject jsonObject = null;
            try {

                if (mQavsdkControl.getAVContext() == null) {
                    Logger.out("请重启应用");
                    return;
                }
                if (!Utils.isNetworkAvailable(mContext)) {
                    Notifier.showShortMsg(mContext, "无网络连接");
                    return;
                }
                if (mUserInfo.forbid == 1) {
                    // ford();//是否封号
                    return;
                }

                HXApp.getInstance().getUserInfo().isCreater = false;
                jsonObject = new JSONObject(HXApp.customcontent);

                Intent intent = new Intent(mActivity, AvActivity.class);
                intent.putExtra(Utils.EXTRA_ROOM_NUM, jsonObject
                        .optInt("room_num"));
                intent.putExtra(Utils.EXTRA_GROUP_ID, jsonObject
                        .optString("group_id"));

                if (jsonObject.optInt("user_id") != 0) {
                    HashMap<String, Object> parater = new HashMap<String, Object>();
                    parater.put("user_id", mUserInfo.user_id);//登录用户的id
                    parater.put("viewed_user_id", jsonObject.optInt("user_id"));
                    HXJavaNet
                            .post(HXJavaNet.url_user_infos, parater, new AngelNetCallBack() {
                                @Override
                                public void onSuccess(int ret_code, String ret_data, String msg) {
                                    if (ret_code == 200) {
                                        messageInfo = Utils
                                                .jsonToBean(ret_data, NewPerMesInfo.class);
                                        if (null != messageInfo) {
                                            if (messageInfo.live_info != null) {
                                                Intent intent = new Intent(getApplication(), AvActivity.class);
                                                intent.putExtra(Utils.EXTRA_ROOM_NUM, messageInfo.live_info.roomNum);
                                                intent.putExtra(Utils.EXTRA_GROUP_ID, messageInfo.live_info.groupId);
                                                intent.putExtra(Utils.EXTRA_SELF_IDENTIFIER, messageInfo.live_info.userId);
                                                intent.putExtra("nickname", messageInfo.nickname);
                                                intent.putExtra("identity", messageInfo.identity);
                                                intent.putExtra("photo", messageInfo.photo);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                }


                                @Override
                                public void onFailure(String msg) {
                                    Notifier.showNormalMsg(mContext, getString(R.string.notify_no_network));
                                }
                            });
                }

                HXApp.customcontent = "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void logoutSucc() {

    }


    @Override
    public void logoutFail() {

    }


    private static class CallBackSub extends AngelNetCallBack {
        private WeakReference<HomeActivity> mWeakActivity;


        public CallBackSub(HomeActivity activity) {
            this.mWeakActivity = new WeakReference<HomeActivity>(activity);
        }


        @Override
        public void onSuccess(int ret_code, String ret_data, String msg) {

        }


        @Override
        public void onFailure(String msg) {

        }


        @Nullable
        public HomeActivity getActivity() {
            if (mWeakActivity != null) {
                HomeActivity homeActivity = mWeakActivity.get();
                if (homeActivity != null) {
                    return homeActivity;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }


    /**
     * 设置点击选项卡的事件处理
     *
     * @param index 选项卡的标号：0, 1, 2, 3
     */
    private void setChioceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        //        clearChioce(); // 清空, 重置选项, 隐藏所有Fragment
        hideFragments(fragmentTransaction);

        switch (index) {
            case 0:
                //                firstImage.setImageResource(R.drawable.XXXX); 需要的话自行修改
                // 如果fg1为空，则创建一个并添加到界面上
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.realtabcontent, homeFragment);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(homeFragment);
                }

                break;

            case 1:
                if (findfragment == null) {
                    findfragment = new FindFragment();
                    fragmentTransaction.add(R.id.realtabcontent, findfragment);
                } else {
                    fragmentTransaction.show(findfragment);
                }

                break;

            case 2:
                if (privateletterfragment == null) {
                    privateletterfragment = new PrivateLetterFragment();
                    fragmentTransaction
                            .add(R.id.realtabcontent, privateletterfragment);
                } else {
                    fragmentTransaction.show(privateletterfragment);
                }

                break;

            case 3:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.realtabcontent, mineFragment);
                } else {
                    fragmentTransaction.show(mineFragment);
                }
                break;
        }

        fragmentTransaction.commitAllowingStateLoss();   // 提交
    }


    /**
     * 隐藏Fragment
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }

        if (mineFragment != null) {
            fragmentTransaction.hide(mineFragment);
        }
        if (findfragment != null) {
            fragmentTransaction.hide(findfragment);
        }
        if (privateletterfragment != null) {
            fragmentTransaction.hide(privateletterfragment);
        }
    }


    /**
     * 创建直播
     */
    private void createRoom() {
        if (!Utils.isNetworkAvailable(getApplicationContext())) {
            Notifier.showNormalMsg(getApplication(), getString(R.string.notify_no_network));
            return;
        }
        HXApp.getInstance().getUserInfo().isCreater = true;
        HXApp.getInstance().setRoomCoverPath(mUserInfo.photo);
        Subscription subscribe = HXHttpUtil.getInstance()
                                           .randomRoomNum(mUserInfo.user_id)
                                           .subscribe(new HttpSubscriber<RoomNumBean>() {
                                               @Override
                                               public void onStart() {
                                                   super.onStart();
                                                   if (loaddialog == null) {
                                                       loaddialog = new LoadingDialog(mActivity, R.style.dialog);
                                                   }
                                                   loaddialog.show();
                                               }


                                               @Override
                                               public void onNext(RoomNumBean roomNumBean) {
                                                   Intent intent = new Intent(HomeActivity.this, AvActivity.class);
                                                   intent.putExtra(Utils.EXTRA_ROOM_NUM, roomNumBean.room_num);
                                                   startActivity(intent);
                                                   if (loaddialog != null) {
                                                       loaddialog.todismiss();
                                                   }
                                               }


                                               @Override
                                               public void onError(Throwable e) {
                                                   super.onError(e);
                                                   if (loaddialog != null) {
                                                       loaddialog.todismiss();
                                                   }
                                               }
                                           });
        addSubscription(subscribe);
        //HXJavaNet
        //        .post(HXJavaNet.url_randomroomnum, "user_id", mUserInfo.user_id, new AngelNetCallBack() {
        //                    @Override
        //                    public void onStart() {
        //                        super.onStart();
        //                        if (loaddialog == null) {
        //                            loaddialog = new LoadingDialog(mActivity, R.style.dialog);
        //                        }
        //                        loaddialog.show();
        //                    }
        //
        //
        //            @Override
        //            public void onSuccess(int ret_code, String ret_data, String msg) {
        //                if (ret_code == 200) {
        //                    try {
        //                        JSONObject jo = new JSONObject(ret_data);
        //                        int roomNum = jo.getInt("room_num");
        //                        Intent intent = new Intent(HomeActivity.this, AvActivity.class);
        //                        intent.putExtra(Utils.EXTRA_ROOM_NUM, roomNum);
        //                        startActivity(intent);
        //
        //                        //sendBroadcast(new Intent(com.remair.heixiu.utils.Utils.ACTION_CREATE_ROOM_NUM_COMPLETE)
        //                        //        .putExtra("room_num", roomNum));
        //                    } catch (JSONException e) {
        //                        e.printStackTrace();
        //                    }
        //                } else if (ret_code == 503) {
        //                    //账号被封
        //                    Notifier.showNormalMsg(mContext, msg);
        //                } else {
        //                    Notifier.showShortMsg(mContext, "网络异常,请重试");
        //                    Logger.out("testhere " + " " +
        //                            ret_code);
        //                }
        //                if (loaddialog != null) {
        //                    loaddialog.todismiss();
        //                }
        //            }
        //
        //
        //            @Override
        //            public void onFailure(String msg) {
        //                Logger.out(msg);
        //                if (loaddialog != null) {
        //                    loaddialog.todismiss();
        //                }
        //                Notifier.showShortMsg(mContext, "网络异常,请重试");
        //            }
        //        });
    }
}
