package com.remair.heixiu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.HXActivity;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.bean.LiveList;
import com.remair.heixiu.bean.NewPerMesInfo;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.EditTextView;
import com.remair.heixiu.view.LoadingDialog;
import java.util.HashMap;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/8/11 11:43
 * 修改人：JXH
 * 修改时间：2016/8/11 11:43
 * 修改备注：
 */
public class PwdAvActivity extends HXBaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_close) ImageView mIvClose;
    @BindView(R.id.photo_secret) SimpleDraweeView mPhotoSecret;
    @BindView(R.id.tv_name) TextView mTvName;
    @BindView(R.id.et_pwd) EditTextView mEtPwd;
    @BindView(R.id.tv_hint) TextView mTvHint;
    @BindView(R.id.start_video) AngelMaterialButton mStartVideo;
    protected NewPerMesInfo mPerMesInfo;
    protected LiveList liveInfo;
    protected boolean isTrue = false;
    protected String mNumPwd = "";//输入的密码
    protected int type = -1;
    protected String mPhoto;
    protected String mNickname;
    protected int mRoomNum;
    protected int mUser_id;//主播id
    protected String mGroupId;
    protected String mIdentity;
    protected SharedPreferences sp;
    protected LoadingDialog dialog;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_pwd);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        sp = getApplicationContext()
                .getSharedPreferences(DemoConstants.LOCAL_DATA, MODE_PRIVATE);
        mStartVideo
                .setBackgroundColor(getResources().getColor(R.color.give_zuan));
        Parcelable personinfo = getIntent().getParcelableExtra("personinfo");
        if (personinfo instanceof NewPerMesInfo) {
            mPerMesInfo = (NewPerMesInfo) personinfo;
            type = 0;
        } else if (personinfo instanceof LiveList) {
            liveInfo = (LiveList) personinfo;
            type = 1;
        }
        if (null == mPerMesInfo && null == liveInfo) {
            return;
        }
        if (type == 0) {
            mPhoto = mPerMesInfo.photo;
            mNickname = mPerMesInfo.nickname;
            mRoomNum = mPerMesInfo.secret_live_info.roomNum;
            mUser_id = mPerMesInfo.user_id;
            mGroupId = mPerMesInfo.secret_live_info.groupId;
            mIdentity = mPerMesInfo.identity;
        } else if (type == 1) {
            mPhoto = liveInfo.photo;
            mNickname = liveInfo.nickname;
            mRoomNum = liveInfo.room_num;
            mUser_id = liveInfo.user_id;
            mGroupId = liveInfo.group_id;
            mIdentity = liveInfo.identity;
        }
        initDate();
        initListener();
    }


    private void initDate() {
        HXImageLoader.loadImage(mPhotoSecret, mPhoto, Util.getPX(98), Util
                .getPX(98));
        mTvName.setText(mNickname);
    }


    private void initListener() {
        mIvClose.setOnClickListener(this);
        mStartVideo.setOnClickListener(this);
        mEtPwd.setSecurityEditCompleListener(new EditTextView.SecurityEditCompleListener() {

            @Override
            public void onNumCompleted(String num) {
                isTrue = true;
                mNumPwd = num;
                Util.hideInputBoard(mEtPwd, getApplication());
                mStartVideo.setBackgroundColor(getResources()
                        .getColor(R.color.hx_main_three));
            }


            @Override
            public void onNumChanged(String num) {
                isTrue = false;
                mNumPwd = num;
                if (num.length() < 4) {
                    mTvHint.setText("");
                    mStartVideo.setBackgroundColor(getResources()
                            .getColor(R.color.give_zuan));
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.start_video:
                if (!isTrue) {
                    Notifier.showShortMsg(getApplication(), "请输入四位密码");
                    return;
                }
                if (dialog == null) {
                    dialog = new LoadingDialog(PwdAvActivity.this, R.style.dialog);
                }
                dialog.show();
                HashMap<String, Object> params = new HashMap<>();
                params.put("user_id", HXApp.getInstance()
                                           .getUserInfo().user_id);
                params.put("room_num", mRoomNum);
                params.put("type", 1);
                params.put("password", mNumPwd);
                HXJavaNet
                        .post(HXJavaNet.url_enter_room, params, new AngelNetCallBack() {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                if (dialog != null) {
                                    dialog.todismiss();
                                }
                                if (ret_code == 200) {
                                    sp.edit().putString(mUser_id + mRoomNum +
                                            "", mUser_id + mRoomNum +
                                            "").apply();
                                    Intent intent = new Intent(getApplication(), AvActivity.class);
                                    intent.putExtra(Utils.EXTRA_ROOM_NUM, mRoomNum);
                                    intent.putExtra(Utils.EXTRA_GROUP_ID, mGroupId);
                                    intent.putExtra(Utils.EXTRA_SELF_IDENTIFIER, mUser_id);
                                    intent.putExtra("nickname", mNickname);
                                    intent.putExtra("identity", mIdentity);
                                    intent.putExtra("photo", mPhoto);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    mTvHint.setText("密码错误");
                                    mTvHint.setTextColor(Color
                                            .parseColor("#ff4b4c"));
                                    //Notifier.showShortMsg(getApplication(), "您输入的密码不正确");
                                }
                            }


                            @Override
                            public void onFailure(String msg) {
                                if (dialog != null) {
                                    dialog.todismiss();
                                }
                                Notifier.showShortMsg(getApplication(), "请重试");
                            }
                        });
                break;
        }
    }
}
