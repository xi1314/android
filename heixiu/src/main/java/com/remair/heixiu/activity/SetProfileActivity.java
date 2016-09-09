package com.remair.heixiu.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.LoginHelper;
import com.remair.heixiu.LoginView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.HXUtil;
import com.remair.heixiu.utils.ImageUtils;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.PanningView;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import rx.Subscription;
import studio.archangel.toolkitv2.activities.PickImageActivity;
import studio.archangel.toolkitv2.interfaces.AngelNetProgressCallBack;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelLoadingDialog;
import studio.archangel.toolkitv2.views.AngelMaterialButton;
import studio.archangel.toolkitv2.views.AngelMenuDialog;

/**
 * Created by JXHIUUI on 2016/3/7.
 */
public class SetProfileActivity extends HXBaseActivity implements LoginView {
    @BindView(R.id.et_nickname) EditText et_nickname;
    @BindView(R.id.sdv_photo) SimpleDraweeView sdv_photo;
    @BindView(R.id.act_register_finish) AngelMaterialButton login;
    @BindView(R.id.iv_finish) ImageView iv_finish;
    @BindView(R.id.et_gender_boy) ImageView et_gender_boy;
    @BindView(R.id.et_gender_gril) ImageView et_gender_gril;
    @BindView(R.id.pan_view) PanningView pan_view;
    boolean hasSet = false;
    int gender = -1;
    String photo = "";
    private String[] mlist;
    boolean bmlist;
    private boolean selectboy = false;
    private boolean selectgril = false;
    private LoginHelper loginHelper;
    private Context mContext;
    private int user_id;
    private UserInfo userInfo;
    private AngelLoadingDialog angelDialog;


    @Override
    protected void initUI() {
        userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            HXApp.getInstance().goLogin(true, false);
            return;
        }
        setContentView(R.layout.act_setprofile);
        mContext = getApplicationContext();
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        user_id = userInfo.user_id;
        loginHelper = new LoginHelper(getApplicationContext(), this);
        mlist = new String[] { "嘿秀", "系统", "官方", "加群", "管理", "妈的", "滚", "操",
                "草", "屎", "屌", "shit", "fuck", "日本", "公安", "机关", "政府", "共产党",
                "微信", "啪", "房事", "人流", "增粗", "堕胎", "妊娠", "肛裂", "催情", "乳房",
                "阿波罗", "奶头", "鸡鸡", "鸭子", "癌症", "满足", "强奸", "糜烂", "成人电影", "太平公主",
                "勃起", "医疗", "红肿", "泄", "生殖器", "激情", "又粗又大", "排卵", "性交", "痛经",
                "性福", "避孕", "maxman", "妇科病", "性", "计生", "无痛", "前列腺", "射精", "射了",
                "包茎", "包皮", "阴茎", "偷看", "缩阴", "遗精", "性病", "毛片", "泌尿", "阳痿",
                "增高", "持久", "阴道", "增大", "脱", "bb", "逼", "傻逼", "脑残", "智障", "一对一",
                "裸聊", "裸体", "骗子", "走私" };
        pan_view.setImageResource(R.drawable.bg1);
        pan_view.startPanning();
        RxViewUtil.viewBindClick(iv_finish, mOnClickListener);
        RxViewUtil.viewBindClick(et_gender_boy, mOnClickListener);
        RxViewUtil.viewBindClick(et_gender_gril, mOnClickListener);
        RxViewUtil.viewBindClick(sdv_photo, mOnClickListener);
        RxViewUtil.viewBindClick(login, mOnClickListener);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (!hasSet) {

            Notifier.showShortMsg(SetProfileActivity.this, getString(R.string.must_setting));

            hasSet = true;
        } else {
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x2711 && resultCode == HXApp.result_ok) {
            if (data == null) {
                Notifier.showNormalMsg(mContext, getString(R.string.photo_fail));
                return;
            }
            String file = data.getStringExtra("file");
            File f = new File(
                    getDir("temp", Context.MODE_PRIVATE).getAbsolutePath() +
                            "/temp_" + System.currentTimeMillis() + ".jpg");
            final String cropped_file = f.getAbsolutePath();
            try {
                ImageUtils
                        .createImageThumbnail(mContext, file, cropped_file, 250, 100, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file == null) {
                Notifier.showNormalMsg(mContext, getString(R.string.photo_fail));
                return;
            }
            HXUtil.getInstance().OSSUploadFile(mContext, file,
                    user_id + "", new AngelNetProgressCallBack() {
                        @Override
                        public void onStart() {
                            angelDialog = new AngelLoadingDialog(mActivity, R.color.hx_main, false);
                        }


                        @Override
                        public void onProgress(long bytesRead, long contentLength, boolean done) {
                            if (angelDialog != null) {
                                angelDialog.setMax(10000);
                                angelDialog.setProgress((int) (
                                        10000.0 * bytesRead / contentLength));
                            }
                        }


                        @Override
                        public void onSuccess(int ret_code, final String higt_ret_data, String msg) {
                            HXUtil.getInstance()
                                  .OSSUploadFile(mContext, cropped_file,
                                          user_id +
                                                  "", new AngelNetProgressCallBack() {
                                              @Override
                                              public void onProgress(long bytesRead, long contentLength, boolean done) {

                                              }


                                              @Override
                                              public void onSuccess(int ret_code, final String ret_data_suss, String msg) {
                                                  photo = ret_data_suss;
                                                  HashMap<String, String> para = new HashMap<>();
                                                  para.put("user_id",
                                                          user_id + "");
                                                  para.put("photo", ret_data_suss);
                                                  para.put("high_photo", higt_ret_data);
                                                  Subscription subscription = HXHttpUtil
                                                          .getInstance()
                                                          .editUser(para)
                                                          .subscribe(new HttpSubscriber<Object>() {
                                                              @Override
                                                              public void onNext(Object o) {
                                                                  HXImageLoader
                                                                          .loadImage(sdv_photo, ret_data_suss, Utils
                                                                                  .getPX(80), Utils
                                                                                  .getPX(80));
                                                                  angelDialog
                                                                          .dismiss();
                                                              }


                                                              @Override
                                                              public void onError(Throwable e) {
                                                                  super.onError(e);
                                                                  angelDialog
                                                                          .dismiss();
                                                              }
                                                          });
                                                  addSubscription(subscription);
                                              }


                                              @Override
                                              public void onFailure(String msg) {

                                              }
                                          });
                        }


                        @Override
                        public void onFailure(String msg) {
                            if (angelDialog != null) {
                                angelDialog.dismiss();
                            }
                            Notifier.showShortMsg(mContext, msg);
                        }
                    });
        }
    }


    private void initImageDir() {
        File sd = Environment.getExternalStorageDirectory();
        String image = sd.getPath() + "/image";
        File file = new File(image);
        if (!file.exists()) {
            file.mkdir();
        }
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_finish:
                    finish();
                    if (null != pan_view) {
                        pan_view.stopPanning();
                        pan_view = null;
                    }
                    break;
                case R.id.et_gender_boy:
                    if (!selectboy) {
                        et_gender_boy.setImageResource(R.drawable.gender_boy);
                        gender = 0;
                        selectboy = true;
                        selectgril = false;
                        et_gender_gril
                                .setImageResource(R.drawable.gender_gril_hint);
                    } else {
                        et_gender_boy
                                .setImageResource(R.drawable.gender_boy_hint);
                        selectboy = false;
                        gender = -1;
                    }
                    break;
                case R.id.et_gender_gril:
                    if (!selectgril) {
                        et_gender_gril.setImageResource(R.drawable.gender_gril);
                        gender = 1;
                        selectgril = true;
                        selectboy = false;
                        et_gender_boy
                                .setImageResource(R.drawable.gender_boy_hint);
                    } else {
                        et_gender_gril
                                .setImageResource(R.drawable.gender_gril_hint);
                        selectgril = false;
                        gender = -1;
                    }
                    break;
                case R.id.sdv_photo://设置头像
                    AngelMenuDialog dialog = new AngelMenuDialog(mActivity, getString(R.string.modify_photo), new String[] {
                            getString(R.string.photo_pic),
                            getString(R.string.select_photo) }, R.layout.item_menudialog, R.id.item_menu_tv, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(mContext, PickImageActivity.class);
                            if (position == 0) {
                                intent.putExtra("mode", PickImageActivity.mode_take_a_photo);
                                try {
                                    String path = File.createTempFile(
                                            "upload_image_" + userInfo.user_id +
                                                    "_" +
                                                    System.currentTimeMillis(), ".jpg", getExternalCacheDir())
                                                      .getAbsolutePath();
                                    intent.putExtra("file_name", path);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else if (position == 1) {
                                intent.putExtra("mode", PickImageActivity.mode_select_from_gallery);
                            }
                            startActivityForResult(intent, 0x2711);
                        }
                    });
                    dialog.show();
                    break;
                case R.id.act_register_finish://登陆
                    login();
                    break;
                default:
                    break;
            }
        }
    };


    private void login() {
        String nickname = et_nickname.getText().toString().trim();
        if (nickname.contains(" ")) {
            nickname = nickname.replaceAll(" ", "");
        }

        for (String ii : mlist) {
            if (nickname.contains(ii)) {
                Notifier.showShortMsg(mContext, getString(R.string.illegal));
                bmlist = true;
                return;
            }
        }
        if (bmlist) {
            bmlist = false;
            return;
        }
        if (photo.isEmpty()) {
            Notifier.showShortMsg(mContext, getString(R.string.updata_photo));
            return;
        }
        if (nickname.isEmpty()) {
            Notifier.showShortMsg(mContext, getString(R.string.nickname));
            return;
        }
        if (gender == -1) {
            Notifier.showShortMsg(mContext, getString(R.string.sex_select));
            return;
        }
        Map<String, String> pamars = new HashMap<>();
        pamars.put("user_id", user_id + "");
        pamars.put("nickname", nickname);
        pamars.put("photo", photo);
        pamars.put("gender", gender + "");
        Subscription subsription = HXHttpUtil.getInstance().editUser(pamars)
                                             .subscribe(new HttpSubscriber<Object>() {
                                                 @Override
                                                 public void onNext(Object o) {
                                                     loginHelper.imLogin(
                                                             userInfo.user_id +
                                                                     "", userInfo.tlsSig);
                                                 }


                                                 @Override
                                                 public void onError(Throwable e) {
                                                     super.onError(e);
                                                 }
                                             });
        addSubscription(subsription);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginHelper != null) {
            loginHelper.onDestory();
        }
        if (null != pan_view) {
            pan_view.stopPanning();
            pan_view = null;
        }
    }


    @Override
    public void loginSucc() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user_id", userInfo.user_id );
        startActivity(intent);
        finish();
    }


    @Override
    public void loginFail() {
        Notifier.showNormalMsg(mContext, getString(R.string.fail_login));
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
