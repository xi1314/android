package com.remair.heixiu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.AvActivity;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.controllers.QavsdkControl;
import com.tencent.av.sdk.AVAudioCtrl;
import java.util.HashMap;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.views.AngelMaterialButton;
import studio.archangel.toolkitv2.views.AngelMaterialRelativeLayout;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：wsk
 * 创建时间：16/8/19 下午12:20
 * 修改人：wsk
 * 修改时间：16/8/19 下午12:20
 * 修改备注：
 */
public class LiveShareView extends RelativeLayout {
    boolean share_weibo = false;
    boolean share_wechat = false;
    boolean share_moment = false;
    boolean share_qq = false;
    boolean share_qqzone = false;
    public int isScret = 0;//是否是加密直播
    public String mPwd;
    UserInfo mSelfUserInfo;
    public int roomNum;
    public AngelMaterialRelativeLayout v_weibo;
    public AngelMaterialRelativeLayout v_wechat;
    public AngelMaterialRelativeLayout v_moment;
    public AngelMaterialRelativeLayout v_qq;
    public AngelMaterialRelativeLayout v_qqzone;
    public ImageView iv_weibo;
    public ImageView iv_wechat;
    public ImageView iv_moment;
    public ImageView iv_qq;
    public ImageView iv_qqzone;
    public TextView mTypeChange;
    public LinearLayout mTypeChangeLl;
    public LinearLayout mTypePwd;
    public TextView mTvScrHint;
    public EditTextView mEtPwd;
    public EditText et_name;

    private ClickListenerInterface clickListenerInterface;

    private QavsdkControl mQavsdkControl;
    private Context context;
    private TextView meilizhi_textview;
    private int heart_user_id;

    public interface ClickListenerInterface {
        void doShare();
    }


    public LiveShareView(Context context, int roomNum, QavsdkControl mQavsdkControl, TextView meilizhi_textview, int heart_user_id) {
        super(context);
        mSelfUserInfo = HXApp.getInstance().getUserInfo();
        this.context = context;
        this.roomNum = roomNum;
        this.mQavsdkControl = mQavsdkControl;
        this.meilizhi_textview = meilizhi_textview;
        this.heart_user_id = heart_user_id;
        initView();
    }


    public LiveShareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public LiveShareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LiveShareView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void initView() {
        View view = LayoutInflater.from(getContext())
                                  .inflate(R.layout.act_show_create, this);
        RelativeLayout act_show_create = (RelativeLayout) view
                .findViewById(R.id.act_show_create_mask);
        AngelMaterialRelativeLayout v_close = (AngelMaterialRelativeLayout) view
                .findViewById(R.id.act_show_create_close);
        et_name = (EditText) view.findViewById(R.id.act_show_create_name);
        AngelMaterialButton v_start = (AngelMaterialButton) view
                .findViewById(R.id.act_show_create_start);
        v_weibo = (AngelMaterialRelativeLayout) view
                .findViewById(R.id.act_show_create_weibo);
        v_wechat = (AngelMaterialRelativeLayout) view
                .findViewById(R.id.act_show_create_wechat);
        v_moment = (AngelMaterialRelativeLayout) view
                .findViewById(R.id.act_show_create_moment);
        v_qq = (AngelMaterialRelativeLayout) view
                .findViewById(R.id.act_show_create_qq);
        v_qqzone = (AngelMaterialRelativeLayout) view
                .findViewById(R.id.act_show_create_qqzone);
        iv_weibo = (ImageView) view
                .findViewById(R.id.act_show_create_weibo_image);
        iv_wechat = (ImageView) view
                .findViewById(R.id.act_show_create_wechat_image);
        iv_moment = (ImageView) view
                .findViewById(R.id.act_show_create_moment_image);
        iv_qq = (ImageView) view.findViewById(R.id.act_show_create_qq_image);
        iv_qqzone = (ImageView) view
                .findViewById(R.id.act_show_create_qqzone_image);
        LinearLayout mParentShare = (LinearLayout) view
                .findViewById(R.id.parent_share);
        mTypeChange = (TextView) view.findViewById(R.id.type_change);
        mTypeChangeLl = (LinearLayout) view.findViewById(R.id.type_change_ll);
        mEtPwd = (EditTextView) view.findViewById(R.id.et_pwd);
        mTypePwd = (LinearLayout) view.findViewById(R.id.type_pwd);
        mTvScrHint = (TextView) view.findViewById(R.id.tv_scret_hint);
        initShraeView();
        initScretView();
        v_start.setOnClickListener(new clickListener());
        v_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AvActivity) context).memberCloseAlertDialog();
                //context.onBackPressed();
            }
        });
    }


    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }


    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.act_show_create_start:
                    clickListenerInterface.doShare();
                    break;
            }
        }
    }


    private void initShraeView() {
        v_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_weibo = !share_weibo;
                iv_weibo.setImageResource(share_weibo
                                          ? R.drawable.icon_weibo_active
                                          : R.drawable.icon_weibo_hint);
            }
        });
        v_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_wechat = !share_wechat;
                if (share_wechat) {
                    share_moment = false;
                    share_qq = false;
                    share_qqzone = false;
                    iv_moment.setImageResource(R.drawable.icon_moment);
                    iv_qq.setImageResource(R.drawable.icon_qq_hint);
                    iv_qqzone.setImageResource(R.drawable.icon_qqzone);
                }
                iv_wechat.setImageResource(share_wechat
                                           ? R.drawable.icon_wechat_active
                                           : R.drawable.icon_wechat);
            }
        });
        v_moment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_moment = !share_moment;
                if (share_moment) {
                    share_wechat = false;
                    share_qq = false;
                    share_qqzone = false;
                    iv_wechat.setImageResource(R.drawable.icon_wechat);
                    iv_qq.setImageResource(R.drawable.icon_qq_hint);
                    iv_qqzone.setImageResource(R.drawable.icon_qqzone);
                }
                iv_moment.setImageResource(share_moment
                                           ? R.drawable.icon_moment_active
                                           : R.drawable.icon_moment);
            }
        });
        v_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_qq = !share_qq;
                if (share_qq) {
                    share_wechat = false;
                    share_moment = false;
                    share_qqzone = false;
                    iv_wechat.setImageResource(R.drawable.icon_wechat);
                    iv_moment.setImageResource(R.drawable.icon_moment);
                    iv_qqzone.setImageResource(R.drawable.icon_qqzone);
                }
                iv_qq.setImageResource(share_qq
                                       ? R.drawable.icon_qq_active
                                       : R.drawable.icon_qq_hint);
            }
        });
        v_qqzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_qqzone = !share_qqzone;
                if (share_qqzone) {
                    share_wechat = false;
                    share_moment = false;
                    share_qq = false;
                    iv_wechat.setImageResource(R.drawable.icon_wechat);
                    iv_moment.setImageResource(R.drawable.icon_moment);
                    iv_qq.setImageResource(R.drawable.icon_qq_hint);
                }
                iv_qqzone.setImageResource(share_qqzone
                                           ? R.drawable.icon_qqzone_active
                                           : R.drawable.icon_qqzone);
            }
        });
    }


    private void initScretView() {
        final Drawable drawablePub = getResources()
                .getDrawable(R.drawable.icon_type_public);
        drawablePub.setBounds(0, 0, drawablePub.getIntrinsicWidth(), drawablePub
                .getIntrinsicHeight());
        final Drawable drawableScr = getResources()
                .getDrawable(R.drawable.icon_type_scret);
        drawableScr.setBounds(0, 0, drawableScr.getIntrinsicWidth(), drawableScr
                .getIntrinsicHeight());
        mTypeChangeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isScret == 0) {
                    isScret = 1;
                    mTypePwd.setVisibility(View.VISIBLE);
                    mTypeChange
                            .setCompoundDrawables(drawableScr, null, null, null);
                    mTypeChange.setText("私密直播");
                    mTvScrHint.setText(R.string.type_change);
                } else {
                    isScret = 0;
                    mTypePwd.setVisibility(View.INVISIBLE);
                    mTypeChange
                            .setCompoundDrawables(drawablePub, null, null, null);
                    mTypeChange.setText("公开直播");
                    mTvScrHint.setText(R.string.type_hint);
                }
            }
        });
    }


    public void share() {
        String title = mSelfUserInfo.nickname + "（" + mSelfUserInfo.identity +
                "）正在直播";
        String textcontent = "不在别处仰望，只在嘿秀欢畅！" + mSelfUserInfo.nickname +
                "正在直播，火速围观...";
        if (share_weibo) {
            SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
            sp.setTitle(title);
            sp.setText(textcontent);
            sp.setImageUrl(HXApp.getInstance().getRoomCoverPath());

            sp.setTitleUrl(getShareUrl(
                    mSelfUserInfo.user_id + "", roomNum, "SinaWeibo"));

            Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
            weibo.share(sp);
            String des = HXApp.getInstance().getUserInfo().nickname +
                    " 分享了这个直播到新浪微博";
            ((AvActivity) context).sendShareMsg(des, "share");
        }

        if (share_wechat) {
            Wechat.ShareParams wechat = new Wechat.ShareParams();
            wechat.setTitle(title);
            wechat.setText(textcontent);
            wechat.setImageUrl(HXApp.getInstance().getRoomCoverPath());
            wechat.setUrl(getShareUrl(
                    mSelfUserInfo.user_id + "", roomNum, "WechatSession"));
            wechat.setShareType(Platform.SHARE_WEBPAGE);
            Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
            weixin.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                }


                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }


                @Override
                public void onCancel(Platform platform, int i) {
                }
            });
            weixin.share(wechat);
            String des = HXApp.getInstance().getUserInfo().nickname +
                    " 分享了这个直播到微信";
            ((AvActivity) context).sendShareMsg(des, "share");
        }
        if (share_qq) {
            QQ.ShareParams sp = new QQ.ShareParams();
            sp.setTitle(title);
            sp.setTitleUrl(getShareUrl(
                    mSelfUserInfo.user_id + "", roomNum, "QQFriend"));
            sp.setText(textcontent);
            sp.setImageUrl(HXApp.getInstance().getRoomCoverPath());
            Platform qq = ShareSDK.getPlatform(QQ.NAME);
            qq.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                }


                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }


                @Override
                public void onCancel(Platform platform, int i) {
                }
            });
            // 执行图文分享
            qq.share(sp);
            String des = HXApp.getInstance().getUserInfo().nickname +
                    " 分享了这个直播到QQ";
            ((AvActivity) context).sendShareMsg(des, "share");
        }
        //朋友圈
        if (share_moment) {
            WechatMoments.ShareParams emmom = new WechatMoments.ShareParams();
            emmom.setTitle(title);
            emmom.setUrl(getShareUrl(
                    mSelfUserInfo.user_id + "", roomNum, "WechatTimeline"));
            emmom.setText(textcontent);
            emmom.setImageUrl(HXApp.getInstance().getRoomCoverPath());
            emmom.setShareType(Platform.SHARE_WEBPAGE);
            Platform qq = ShareSDK.getPlatform(WechatMoments.NAME);
            qq.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                }


                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }


                @Override
                public void onCancel(Platform platform, int i) {

                }
            }); // 设置分享事件回调
            // 执行图文分享
            qq.share(emmom);
            String des = HXApp.getInstance().getUserInfo().nickname +
                    " 分享了这个直播到微信朋友圈";
            ((AvActivity) context).sendShareMsg(des, "share");
        }
        //QQSpace
        if (share_qqzone) {
            QZone.ShareParams qZone = new QZone.ShareParams();
            qZone.setTitle(title);
            qZone.setTitleUrl(getShareUrl(
                    mSelfUserInfo.user_id + "", roomNum, "QZone"));
            qZone.setText(textcontent);
            qZone.setImageUrl(HXApp.getInstance().getRoomCoverPath());
            qZone.setShareType(Platform.SHARE_WEBPAGE);
            Platform qq = ShareSDK.getPlatform(QZone.NAME);
            qq.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                }


                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }


                @Override
                public void onCancel(Platform platform, int i) {
                }
            }); // 设置分享事件回调
            // 执行图文分享
            qq.share(qZone);
            String des = HXApp.getInstance().getUserInfo().nickname +
                    " 分享了这个直播到QQ空间";
            ((AvActivity) context).sendShareMsg(des, "share");
        }
        if (!share_weibo && !share_wechat && !share_qq && !share_moment &&
                !share_qqzone) {
        }
        if (mSelfUserInfo.isCreater) {
            AVAudioCtrl avAudioCtrl = mQavsdkControl.getAVContext()
                                                    .getAudioCtrl();
            avAudioCtrl.enableMic(true);
        } else {
            mQavsdkControl.getAVContext().getAudioCtrl().enableMic(false);
        }
    }


    private String getShareUrl(String user_id, int roomNum, String type) {
        String url = null;
        url = "http://www.imheixiu.com/live/live.php?" + "user_id=" + user_id +
                "&room_id=" + roomNum + "&platform_type=" + type;
        //下面的是测试的
        //        url = "http://www-test.imheixiu.com/live/live.php?" + "user_id=" + user_id + "&room_id=" + roomNum + "&channel_id=" + channel_id + "&platform_type=" + type;
        Logger.out("url:  " + url);
        return url;
    }
}
