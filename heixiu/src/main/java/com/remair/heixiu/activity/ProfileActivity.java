package com.remair.heixiu.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.HXUtil;
import com.remair.heixiu.utils.ImageUtils;
import com.remair.heixiu.utils.RxViewUtil;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import rx.functions.Action1;
import studio.archangel.toolkitv2.activities.PickImageActivity;
import studio.archangel.toolkitv2.interfaces.AngelNetProgressCallBack;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelLoadingDialog;
import studio.archangel.toolkitv2.views.AngelMenuDialog;
import studio.archangel.toolkitv2.views.AngelOptionItem;

/**
 * Created by Michael
 * user profile page
 */

public class ProfileActivity extends HXBaseActivity {
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;
    @BindView(R.id.act_profile_avatar) AngelOptionItem mActProfileAvatar;
    @BindView(R.id.act_profile_name) AngelOptionItem mActProfileName;
    @BindView(R.id.act_profile_id) AngelOptionItem mActProfileId;
    @BindView(R.id.act_profile_signature) AngelOptionItem mActProfileSignature;
    @BindView(R.id.act_profile_grade) AngelOptionItem mActProfileGrade;
    @BindView(R.id.act_profile_certification)
    AngelOptionItem mActProfileCertification;

    protected SimpleDraweeView iv_avatar;
    protected Context mContext;
    private UserInfo mUserInfo;
    private AngelLoadingDialog angelDialog;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_profile);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        mUserInfo = HXApp.getInstance().getUserInfo();
        if (null == mUserInfo) {
            return;
        }
        mTitleTxt.setText(getString(R.string.profle_title));
        mContext = getApplicationContext();
        iv_avatar = new SimpleDraweeView(mContext);
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder.setFadeDuration(300)
                                                  .setPlaceholderImage(getResources()
                                                          .getDrawable(R.drawable.placeholder))
                                                  .setRoundingParams(new RoundingParams()
                                                          .setRoundAsCircle(true))
                                                  .setPlaceholderImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                                                  .build();
        int px = Util.getPX(42);
        iv_avatar.setHierarchy(hierarchy);
        HXImageLoader.loadImage(iv_avatar, mUserInfo.photo, px, px);
        mActProfileAvatar.setCustomView(iv_avatar);
        ViewGroup.LayoutParams params = iv_avatar.getLayoutParams();
        params.width = px;
        params.height = px;
        initTitle();
        initClick();
    }


    private void initClick() {
        RxViewUtil.viewBindClick(mTitleLeftImage, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        RxViewUtil.viewBindClick(mActProfileAvatar, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                AngelMenuDialog dialog = new AngelMenuDialog(mActivity, "修改头像", new String[] {
                        "拍照",
                        "从相册选择" }, R.layout.item_menudialog, R.id.item_menu_tv, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(mContext, PickImageActivity.class);
                        if (position == 0) {
                            intent.putExtra("mode", PickImageActivity.mode_take_a_photo);
                            try {
                                String path = File.createTempFile(
                                        "upload_image_" + mUserInfo.user_id +
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
            }
        });

        RxViewUtil.viewBindClick(mActProfileName, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(mActivity, ChangeNameActivity.class);
                intent.putExtra("nickname", mUserInfo.nickname);
                intent.putExtra("user_id", mUserInfo.user_id);
                startActivityForResult(intent, 0x2722);
            }
        });

        RxViewUtil.viewBindClick(mActProfileSignature, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(mActivity, SignatureActivity.class);
                intent.putExtra("signature", mUserInfo.signature);
                intent.putExtra("user_id", mUserInfo.user_id);
                startActivityForResult(intent, 0x2733);
            }
        });

        RxViewUtil.viewBindClick(mActProfileId, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                c.setText(mUserInfo.identity);//设置Clipboard 的内容
                Notifier.showShortMsg(mContext, getString(R.string.clipboard_toast));
            }
        });

        //实名认证
        RxViewUtil.viewBindClick(mActProfileCertification, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(mActivity, CertificationiActivity.class);
                startActivity(intent);
            }
        });
        //我的等级

        RxViewUtil.viewBindClick(mActProfileGrade, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(mActivity, LevelActivity.class);
                intent.putExtra("user_id", mUserInfo.user_id);
                intent.putExtra("grade", mUserInfo.grade);
                startActivity(intent);
            }
        });
    }


    private void initTitle() {
        mActProfileName.setContent(mUserInfo.nickname);
        String string = mUserInfo.signature;
        if (!TextUtils.isEmpty(string)) {
            if (string.length() > 15) {
                string = string.substring(0, 14) + "...";
            }
            mActProfileSignature.setContent(string);
            mActProfileSignature
                    .setContentColor(getResources().getColor(R.color.qing));
        }
        mActProfileId.setContent(mUserInfo.identity);
        int grade = mUserInfo.grade;
        mActProfileGrade.setContent(grade + getString(R.string.grade_count));
        mActProfileGrade
                .setContentColor(getResources().getColor(R.color.give_zuan));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mUserInfo == null) {
            return;
        }
        if (requestCode == 0x2711 && resultCode == HXApp.result_ok) {
            if (data == null) {
                Notifier.showNormalMsg(mContext, getString(R.string.photo_fail));
                return;
            }
            final String file = data.getStringExtra("file");
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
            HXUtil.getInstance().OSSUploadFile(mContext, file, String
                    .valueOf(mUserInfo.user_id), new AngelNetProgressCallBack() {
                @Override
                public void onStart() {
                    angelDialog = new AngelLoadingDialog(mActivity, R.color.hx_main, false);
                }


                @Override
                public void onProgress(long bytesRead, long contentLength, boolean done) {
                    if (angelDialog != null) {
                        angelDialog.setMax(10000);
                        angelDialog.setProgress((int) (10000.0 * bytesRead /
                                                               contentLength));
                    }
                }


                @Override
                public void onSuccess(int ret_code, final String higt_ret_data, String msg) {
                    HXUtil.getInstance()
                          .OSSUploadFile(mContext, cropped_file, String
                                  .valueOf(mUserInfo.user_id), new AngelNetProgressCallBack() {
                              @Override
                              public void onProgress(long bytesRead, long contentLength, boolean done) {

                              }


                              @Override
                              public void onSuccess(int ret_code, final String ret_data_suss, String msg) {
                                  HashMap<String, Object> para = new HashMap<>();
                                  para.put("user_id", mUserInfo.user_id);
                                  para.put("photo", ret_data_suss);
                                  para.put("high_photo", higt_ret_data);
                                  HXJavaNet
                                          .post(HXJavaNet.url_edituser, para, new AngelNetCallBack() {
                                              @Override
                                              public void onSuccess(int ret_code, String ret_data, String msg) {
                                                  if (ret_code == 200) {
                                                      mUserInfo.high_photo = higt_ret_data;
                                                      mUserInfo.photo = ret_data_suss;
                                                      HXApp.getInstance()
                                                           .setUserInfo(mUserInfo);
                                                  } else {
                                                      Notifier.showShortMsg(mContext, msg);
                                                  }
                                                  angelDialog.dismiss();
                                                  HXImageLoader
                                                          .loadImage(iv_avatar, ret_data_suss, Util
                                                                  .getPX(42), Util
                                                                  .getPX(42));
                                              }


                                              @Override
                                              public void onFailure(String msg1) {
                                                  Notifier.showNormalMsg(mContext, msg1);
                                                  angelDialog.dismiss();
                                                  setResult(HXApp.result_cancel);
                                                  finish();
                                              }
                                          });
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
                }
            });
        } else if (requestCode == 0x2722 && resultCode == HXApp.result_ok) {
            String nickname = mUserInfo.nickname;
            if (!TextUtils.isEmpty(nickname)) {
                mActProfileName.setContent(nickname);
            }
        } else if (requestCode == 0x2733 && resultCode == HXApp.result_ok) {
            String string = mUserInfo.signature;
            if (null != string && !string.isEmpty()) {
                mActProfileSignature
                        .setContentColor(getResources().getColor(R.color.qing));
                if (string.length() > 15) {
                    string = string.substring(0, 14) + "...";
                }

                mActProfileSignature.setContent(string);
            } else {
                mActProfileSignature.setContentColor(getResources()
                        .getColor(R.color.give_zuan));
                mActProfileSignature.setContent(getString(R.string.sig_con));
            }
        }
    }
}
