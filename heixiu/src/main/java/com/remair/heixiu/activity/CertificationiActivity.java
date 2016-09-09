package com.remair.heixiu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.bean.CertifitionBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.giftview.StrokeTextView;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.HXUtil;
import java.io.File;
import java.io.IOException;
import rx.Subscription;
import studio.archangel.toolkitv2.activities.PickImageActivity;
import studio.archangel.toolkitv2.interfaces.AngelNetProgressCallBack;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelLoadingDialog;
import studio.archangel.toolkitv2.views.AngelMaterialButton;
import studio.archangel.toolkitv2.views.AngelMenuDialog;

/**
 * Created by wsk on 16/4/15.
 */
public class CertificationiActivity extends HXBaseActivity implements View.OnClickListener {
    @BindView(R.id.et_name) EditText et_name;
    @BindView(R.id.et_identity) EditText et_identity;
    @BindView(R.id.et_caka) EditText et_caka;
    @BindView(R.id.et_phone) EditText et_phone;
    @BindView(R.id.tv_mes) TextView tv_mes;
    @BindView(R.id.item_show_front) SimpleDraweeView item_show_front;
    @BindView(R.id.btn_front_delete) ImageView btn_front_delete;

    @BindView(R.id.btn_submit_attestation)
    AngelMaterialButton btn_submit_attestation;

    @BindView(R.id.btn_attestation_front) ImageView btn_attestation_front;
    @BindView(R.id.ll_front) LinearLayout ll_front;
    @BindView(R.id.pload_fail_front) StrokeTextView pload_fail_front;
    @BindView(R.id.btn_front_sucess) ImageView btn_front_sucess;
    @BindView(R.id.image_finsh_log) ImageView image_finsh_log;//反
    @BindView(R.id.btn_attestation) ImageView btn_attestation;//反
    @BindView(R.id.btn_back_sucess) ImageView btn_back_sucess;//反
    @BindView(R.id.ll_back) LinearLayout ll_back;//反
    @BindView(R.id.item_show_back) SimpleDraweeView item_show_back;//反
    @BindView(R.id.btn_hand_delete) ImageView btn_hand_delete;//手持
    @BindView(R.id.btn_attestation_hand) ImageView btn_attestation_hand;//手持
    @BindView(R.id.tv_hand_mess) StrokeTextView tv_hand_mess;//手持
    @BindView(R.id.tv_back_fail) StrokeTextView tv_back_fail;//fan
    @BindView(R.id.item_show_hand) SimpleDraweeView item_show_hand;//手持
    @BindView(R.id.btn_hand_sucess) ImageView btn_hand_sucess;//手持
    @BindView(R.id.ll_submit_server) RelativeLayout ll_submit_server;//状态1
    @BindView(R.id.iv_status) ImageView iv_status;//状
    @BindView(R.id.tv_message_status) TextView tv_message_status;
    @BindView(R.id.tv_turn_case) TextView tv_turn_case;//驳回
    @BindView(R.id.ll_reason1) LinearLayout ll_reason1;
    @BindView(R.id.tv_reason1) TextView tv_reason1;
    @BindView(R.id.ll_reason2) LinearLayout ll_reason2;
    @BindView(R.id.tv_reason2) TextView tv_reason2;
    @BindView(R.id.ll_status) LinearLayout ll_status;
    @BindView(R.id.ll_submit) LinearLayout ll_submit;//提交申请
    @BindView(R.id.title_txt) TextView title_txt;
    @BindView(R.id.title_left_image) ImageButton title_left_image;
    private AngelLoadingDialog angelDialog;

    String photo = "";
    String photoreverse = "";
    String photohand = "";
    int user_id;
    boolean isfront = false;
    boolean isback = false;
    boolean ishand = false;


    @Override
    protected void initUI() {
        setContentView(R.layout.activity_certification);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        title_txt.setText(getString(R.string.certification));
        btn_attestation.setOnClickListener(this);
        btn_submit_attestation.setOnClickListener(this);
        btn_attestation_front.setOnClickListener(this);
        btn_attestation.setOnClickListener(this);
        btn_attestation_hand.setOnClickListener(this);

        btn_front_delete.setOnClickListener(this);
        image_finsh_log.setOnClickListener(this);
        btn_hand_delete.setOnClickListener(this);
        user_id = HXApp.getInstance().getUserInfo().user_id;
        initdata();
    }

    private void initdata() {
        Subscription subscribe =  HXHttpUtil.getInstance().userSearch(user_id)
                  .subscribe(new HttpSubscriber<CertifitionBean>() {
                      @Override
                      public void onNext(CertifitionBean certifitionBean) {
                          showViewData(certifitionBean);
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
                      }
                  });
        addSubscription(subscribe);
    }


    private void setTextStle() {
        et_name.setGravity(Gravity.RIGHT);
        et_identity.setGravity(Gravity.RIGHT);
        et_caka.setGravity(Gravity.RIGHT);
        et_phone.setGravity(Gravity.RIGHT);
    }


    private void showViewData(CertifitionBean certifitionBean) {
        if (0 == certifitionBean.status) {//0:审核中
            et_name.setText(certifitionBean.real_name);
            et_identity.setText(certifitionBean.id_card_no);
            et_caka.setText(certifitionBean.bank_card_no);
            et_phone.setText(certifitionBean.phone);
            tv_mes.setVisibility(View.GONE);
            ll_status.setVisibility(View.VISIBLE);
            item_show_front.setVisibility(View.VISIBLE);
            item_show_back.setVisibility(View.VISIBLE);
            item_show_hand.setVisibility(View.VISIBLE);
            ll_submit_server.setVisibility(View.GONE);
            iv_status.setImageResource(R.drawable.auditing);
            tv_message_status.setText(R.string.examine_ing);
            tv_message_status.setTextColor(this.getResources()
                                               .getColor(R.color.blue_color));
            tv_turn_case.setVisibility(View.GONE);
            ll_reason1.setVisibility(View.GONE);
            ll_reason2.setVisibility(View.GONE);
            btn_attestation_front.setVisibility(View.GONE);
            btn_attestation.setVisibility(View.GONE);
            btn_attestation_hand.setVisibility(View.GONE);

            btn_submit_attestation.setVisibility(View.GONE);
            HXImageLoader
                    .loadImage(item_show_front, certifitionBean.id_card_front, Util
                            .getPX(96), Util.getPX(48));
            HXImageLoader
                    .loadImage(item_show_back, certifitionBean.id_card_back, Util
                            .getPX(96), Util.getPX(48));
            HXImageLoader
                    .loadImage(item_show_hand, certifitionBean.id_card_hand, Util
                            .getPX(96), Util.getPX(48));
            setEdable(false);
            setTextStle();
        } else if (1 == certifitionBean.status) {//1审核通过
            et_name.setText(certifitionBean.real_name);
            et_name.setFocusable(false);
            et_name.setEnabled(false);
            et_identity.setText(certifitionBean.id_card_no);
            et_identity.setFocusable(false);
            et_identity.setEnabled(false);
            et_caka.setText(certifitionBean.bank_card_no);
            et_caka.setFocusable(false);
            et_caka.setEnabled(false);
            et_phone.setText(certifitionBean.phone);
            et_phone.setFocusable(false);
            et_phone.setEnabled(false);
            tv_mes.setVisibility(View.GONE);
            ll_submit_server.setVisibility(View.GONE);
            item_show_front.setVisibility(View.VISIBLE);
            item_show_back.setVisibility(View.VISIBLE);
            item_show_hand.setVisibility(View.VISIBLE);
            ll_status.setVisibility(View.VISIBLE);
            btn_attestation_front.setVisibility(View.GONE);
            btn_attestation.setVisibility(View.GONE);
            btn_attestation_hand.setVisibility(View.GONE);
            btn_submit_attestation.setVisibility(View.GONE);
            iv_status.setImageResource(R.drawable.auditing);
            tv_message_status.setText("信息审核通过");
            tv_message_status.setTextColor(this.getResources()
                                               .getColor(R.color.blue_color));
            ll_reason1.setVisibility(View.GONE);
            ll_reason2.setVisibility(View.GONE);
            tv_turn_case.setVisibility(View.GONE);

            HXImageLoader
                    .loadImage(item_show_front, certifitionBean.id_card_front, Util
                            .getPX(96), Util.getPX(48));
            HXImageLoader
                    .loadImage(item_show_back, certifitionBean.id_card_back, Util
                            .getPX(96), Util.getPX(48));
            HXImageLoader
                    .loadImage(item_show_hand, certifitionBean.id_card_hand, Util
                            .getPX(96), Util.getPX(48));
            setEdable(false);
            setTextStle();
        } else if (2 == certifitionBean.status) {//2:未通过
            et_name.setText(certifitionBean.real_name);
            et_identity.setText(certifitionBean.id_card_no);
            et_caka.setText(certifitionBean.bank_card_no);
            et_phone.setText(certifitionBean.phone);
            tv_mes.setVisibility(View.GONE);
            ll_submit_server.setVisibility(View.GONE);
            ll_status.setVisibility(View.VISIBLE);
            item_show_front.setVisibility(View.VISIBLE);
            item_show_back.setVisibility(View.VISIBLE);
            item_show_hand.setVisibility(View.VISIBLE);
            btn_attestation_front.setVisibility(View.GONE);
            btn_attestation.setVisibility(View.GONE);
            btn_attestation_hand.setVisibility(View.GONE);
            if ("".equals(certifitionBean.reason)) {
                tv_reason1.setText(": 无");
            } else {
                tv_reason1.setText(": " + certifitionBean.reason);
            }
            ll_reason2.setVisibility(View.GONE);
            btn_submit_attestation.setVisibility(View.VISIBLE);
            btn_submit_attestation.setText("编辑");
            HXImageLoader
                    .loadImage(item_show_front, certifitionBean.id_card_front, Util
                            .getPX(96), Util.getPX(48));
            HXImageLoader
                    .loadImage(item_show_back, certifitionBean.id_card_back, Util
                            .getPX(96), Util.getPX(48));
            HXImageLoader
                    .loadImage(item_show_hand, certifitionBean.id_card_hand, Util
                            .getPX(96), Util.getPX(48));
            setEdable(false);
            setTextStle();
        } else if (3 == certifitionBean.status) {//3未提交审核
            tv_mes.setVisibility(View.VISIBLE);
            ll_submit_server.setVisibility(View.VISIBLE);
            ll_status.setVisibility(View.GONE);
            btn_submit_attestation.setVisibility(View.VISIBLE);
            setEdable(true);
        } else if (4 == certifitionBean.status) {//提交过小框incomplete_message
            et_identity.setText(certifitionBean.id_card_no);
            et_name.setText(certifitionBean.real_name);
            tv_mes.setVisibility(View.VISIBLE);
            ll_submit_server.setVisibility(View.VISIBLE);
            ll_status.setVisibility(View.GONE);
            btn_submit_attestation.setVisibility(View.VISIBLE);
            setEdable(true);
            et_identity.setFocusableInTouchMode(false);
            et_name.setFocusableInTouchMode(false);
        }
    }


    private void setEdable(boolean flag) {
        et_name.setFocusableInTouchMode(flag);
        et_identity.setFocusableInTouchMode(flag);
        et_caka.setFocusableInTouchMode(flag);
        et_phone.setFocusableInTouchMode(flag);
    }


    @Override
    public void onClick(View v) {
        final UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_attestation://反面
                if (isback) {
                    tv_back_fail.setVisibility(View.GONE);
                    ;
                    btn_attestation.setImageResource(R.drawable.add_pic);
                    isback = false;
                } else {
                    AngelMenuDialog dialog = new AngelMenuDialog(this, "上传认证照片", new String[] {
                            "拍照",
                            "从相册选择" }, R.layout.item_menudialog, R.id.item_menu_tv, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplication(), PickImageActivity.class);
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
                            startActivityForResult(intent, 33333);
                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.btn_attestation_front://正面
                if (isfront) {
                    pload_fail_front.setVisibility(View.GONE);
                    btn_attestation_front.setImageResource(R.drawable.add_pic);
                    isfront = false;
                } else {
                    AngelMenuDialog dialogreverse = new AngelMenuDialog(this, "上传认证照片", new String[] {
                            "拍照",
                            "从相册选择" }, R.layout.item_menudialog, R.id.item_menu_tv, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplication(), PickImageActivity.class);
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
                            startActivityForResult(intent, 33334);
                        }
                    });
                    dialogreverse.show();
                }
                break;
            case R.id.btn_attestation_hand://手持
                if (ishand) {
                    tv_hand_mess.setVisibility(View.GONE);
                    btn_attestation_hand.setImageResource(R.drawable.add_pic);
                    ishand = false;
                } else {
                    AngelMenuDialog dialoghand = new AngelMenuDialog(this, "上传认证照片", new String[] {
                            "拍照",
                            "从相册选择" }, R.layout.item_menudialog, R.id.item_menu_tv, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplication(), PickImageActivity.class);
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
                            startActivityForResult(intent, 33335);
                        }
                    });
                    dialoghand.show();
                }
                break;
            case R.id.btn_front_delete://正
                item_show_front.setVisibility(View.GONE);
                btn_front_delete.setVisibility(View.GONE);
                btn_attestation_front.setVisibility(View.VISIBLE);
                btn_front_sucess.setVisibility(View.GONE);
                break;
            case R.id.image_finsh_log://反
                item_show_back.setVisibility(View.GONE);
                image_finsh_log.setVisibility(View.GONE);
                btn_attestation.setVisibility(View.VISIBLE);
                btn_back_sucess.setVisibility(View.GONE);
                break;
            case R.id.btn_hand_delete://手持
                item_show_hand.setVisibility(View.GONE);
                btn_hand_delete.setVisibility(View.GONE);
                btn_attestation_hand.setVisibility(View.VISIBLE);
                btn_hand_sucess.setVisibility(View.GONE);
                break;

            case R.id.btn_submit_attestation://提交或编辑
                if (btn_submit_attestation.getText().toString().equals("编辑")) {
                    setEdable(true);
                    tv_mes.setVisibility(View.VISIBLE);
                    ll_submit_server.setVisibility(View.VISIBLE);
                    btn_front_delete.setVisibility(View.VISIBLE);
                    image_finsh_log.setVisibility(View.VISIBLE);
                    btn_hand_delete.setVisibility(View.VISIBLE);
                    btn_submit_attestation.setVisibility(View.VISIBLE);
                    btn_submit_attestation.setText("提交认证");
                } else if (btn_submit_attestation.getText().toString()
                                                 .equals("信息审核通过")) {

                    finish();
                } else {
                    final String phoneNumber = et_phone.getText().toString();
                    final String name = et_name.getText().toString();
                    final String bankCark = et_caka.getText().toString();
                    final String identity = et_identity.getText().toString();
                    if (phoneNumber.isEmpty()) {
                        Notifier.showShortMsg(CertificationiActivity.this, "请输入电话号码");
                        return;
                    }

                    if (name.isEmpty()) {
                        Notifier.showShortMsg(CertificationiActivity.this, "姓名不能为空");
                        return;
                    }
                    if (identity.isEmpty()) {
                        Notifier.showShortMsg(CertificationiActivity.this, "身份证号不能为空");
                        return;
                    }
                    if (bankCark.isEmpty()) {
                        Notifier.showShortMsg(CertificationiActivity.this, "银行卡号不能为空");
                        return;
                    }
                    if (photo.isEmpty()) {
                        Notifier.showShortMsg(CertificationiActivity.this, "您还未上传正面证件照");
                        return;
                    }
                    if (photoreverse.isEmpty()) {
                        Notifier.showShortMsg(CertificationiActivity.this, "您还未上传反面证件照");
                        return;
                    }
                    if (photohand.isEmpty()) {
                        Notifier.showShortMsg(CertificationiActivity.this, "您还未上传手持证件照");
                        return;
                    }

                    Subscription subscribe = HXHttpUtil.getInstance()
                                                       .auth(user_id, name, identity, bankCark, phoneNumber, photo, photoreverse, photohand)
                                                       .subscribe(new HttpSubscriber<Object>() {
                                  @Override
                                  public void onNext(Object o) {
                                      et_name.setText(name);
                                      et_identity.setText(identity);
                                      et_caka.setText(bankCark);
                                      et_phone.setText(phoneNumber);
                                      tv_mes.setVisibility(View.GONE);
                                      ll_status.setVisibility(View.VISIBLE);
                                      item_show_front
                                              .setVisibility(View.VISIBLE);
                                      item_show_back
                                              .setVisibility(View.VISIBLE);
                                      item_show_hand
                                              .setVisibility(View.VISIBLE);
                                      ll_submit_server
                                              .setVisibility(View.GONE);
                                      iv_status
                                              .setImageResource(R.drawable.auditing);
                                      tv_message_status
                                              .setText(R.string.examine_ing);
                                      tv_message_status
                                              .setTextColor(CertificationiActivity.this
                                                      .getResources()
                                                      .getColor(R.color.blue_color));
                                      tv_turn_case.setVisibility(View.GONE);
                                      ll_reason1.setVisibility(View.GONE);
                                      ll_reason2.setVisibility(View.GONE);
                                      btn_attestation_front
                                              .setVisibility(View.GONE);
                                      btn_attestation
                                              .setVisibility(View.GONE);
                                      btn_attestation_hand
                                              .setVisibility(View.GONE);
                                      image_finsh_log
                                              .setVisibility(View.GONE);
                                      btn_front_delete
                                              .setVisibility(View.GONE);
                                      btn_hand_delete
                                              .setVisibility(View.GONE);
                                      btn_submit_attestation
                                              .setVisibility(View.GONE);

                                      btn_submit_attestation
                                              .setVisibility(View.GONE);
                                      HXImageLoader
                                              .loadImage(item_show_front, photo, Util
                                                      .getPX(96), Util
                                                      .getPX(48));
                                      HXImageLoader
                                              .loadImage(item_show_back, photohand, Util
                                                      .getPX(96), Util
                                                      .getPX(48));
                                      HXImageLoader
                                              .loadImage(item_show_hand, photohand, Util
                                                      .getPX(96), Util
                                                      .getPX(48));
                                      setEdable(false);
                                  }


                                  @Override
                                  public void onError(Throwable e) {
                                      super.onError(e);
                                  }
                              });
                    addSubscription(subscribe);

                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showPopto(requestCode, resultCode, data);
    }


    private void showPopto(final int requestCode, int resultCode, Intent data) {
        if (resultCode == HXApp.result_ok) {
            if (data == null) {
                Notifier.showNormalMsg(getApplication(), "图片生成中遇到了问题...");
                if (requestCode == 33333) {//反面
                    tv_back_fail.setVisibility(View.VISIBLE);
                    ;
                    btn_attestation.setImageResource(R.drawable.upload_fail);
                    isback = true;
                } else if (requestCode == 33334) {//正面
                    pload_fail_front.setVisibility(View.VISIBLE);
                    btn_attestation_front
                            .setImageResource(R.drawable.upload_fail);
                    isfront = true;
                } else if (requestCode == 33335) {
                    tv_hand_mess.setVisibility(View.VISIBLE);
                    btn_attestation_hand
                            .setImageResource(R.drawable.upload_fail);
                    ishand = true;
                }
                return;
            }
            String file = data.getStringExtra("file");
            if (file == null) {
                if (requestCode == 33333) {//反面
                    tv_back_fail.setVisibility(View.VISIBLE);
                    ;
                    btn_attestation.setImageResource(R.drawable.upload_fail);
                    isback = true;
                } else if (requestCode == 33334) {//正面
                    pload_fail_front.setVisibility(View.VISIBLE);
                    btn_attestation_front
                            .setImageResource(R.drawable.upload_fail);
                    isfront = true;
                } else if (requestCode == 33335) {
                    tv_hand_mess.setVisibility(View.VISIBLE);
                    btn_attestation_hand
                            .setImageResource(R.drawable.upload_fail);
                    ishand = true;
                }
                Notifier.showNormalMsg(getApplication(), "图片生成中遇到了问题...");
                return;
            }
            HXUtil.getInstance().OSSUploadFile(getApplication(), file,
                    user_id + "", new AngelNetProgressCallBack() {
                        @Override
                        public void onStart() {
                            angelDialog = new AngelLoadingDialog(CertificationiActivity.this, R.color.hx_main, false);
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
                        public void onSuccess(int ret_code, final String ret_data, String msg) {
                            if (requestCode == 33333) {
                                photo = ret_data;
                            } else if (requestCode == 33334) {
                                photoreverse = ret_data;
                            } else if (requestCode == 33335) {
                                photohand = ret_data;
                            }

                            angelDialog
                                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog1) {

                                            if (requestCode == 33333) {//反面
                                                item_show_back
                                                        .setVisibility(View.VISIBLE);
                                                image_finsh_log
                                                        .setVisibility(View.VISIBLE);
                                                btn_attestation
                                                        .setVisibility(View.GONE);
                                                btn_back_sucess
                                                        .setVisibility(View.VISIBLE);
                                                HXImageLoader
                                                        .loadImage(item_show_back, ret_data, Util
                                                                .getPX(96), Util
                                                                .getPX(48));
                                            } else if (requestCode ==
                                                    33334) {//正面
                                                item_show_front
                                                        .setVisibility(View.VISIBLE);
                                                btn_front_delete
                                                        .setVisibility(View.VISIBLE);
                                                btn_attestation_front
                                                        .setVisibility(View.GONE);
                                                btn_front_sucess
                                                        .setVisibility(View.VISIBLE);
                                                HXImageLoader
                                                        .loadImage(item_show_front, ret_data, Util
                                                                .getPX(96), Util
                                                                .getPX(48));
                                            } else if (requestCode == 33335) {
                                                item_show_hand
                                                        .setVisibility(View.VISIBLE);
                                                btn_hand_delete
                                                        .setVisibility(View.VISIBLE);
                                                btn_attestation_hand
                                                        .setVisibility(View.GONE);
                                                btn_hand_sucess
                                                        .setVisibility(View.VISIBLE);
                                                HXImageLoader
                                                        .loadImage(item_show_hand, ret_data, Util
                                                                .getPX(96), Util
                                                                .getPX(48));
                                            }
                                        }
                                    });
                            angelDialog.dismiss();
                        }


                        @Override
                        public void onFailure(String msg) {
                            if (angelDialog != null) {
                                angelDialog.dismiss();
                            }
                            if (requestCode == 33333) {//反面
                                tv_back_fail.setVisibility(View.VISIBLE);
                                ;
                                btn_attestation
                                        .setImageResource(R.drawable.upload_fail);
                                isback = true;
                            } else if (requestCode == 33334) {//正面
                                pload_fail_front.setVisibility(View.VISIBLE);
                                btn_attestation_front
                                        .setImageResource(R.drawable.upload_fail);
                                isfront = true;
                            } else if (requestCode == 33335) {
                                tv_hand_mess.setVisibility(View.VISIBLE);
                                btn_attestation_hand
                                        .setImageResource(R.drawable.upload_fail);
                                ishand = true;
                            }
                            Notifier.showShortMsg(CertificationiActivity.this, msg);
                        }
                    });
        }
    }
}
