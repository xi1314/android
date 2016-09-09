package com.remair.heixiu.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.adapters.PersonMessageAdapter;
import com.remair.heixiu.bean.FriendInfo;
import com.remair.heixiu.bean.NewPerMesInfo;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.sqlite.ConcernInfoDB;
import com.remair.heixiu.sqlite.ConcernInfoDao;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDB;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDao;
import com.remair.heixiu.utils.GetMessageUtil;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import java.util.ArrayList;
import java.util.List;
import rx.Subscription;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelMaterialProperties;
import studio.archangel.toolkitv2.views.AngelOptionItem;
import studio.archangel.toolkitv2.views.AngelOptionItem2;

/**
 * Created by wsk on 16/3/1.
 */
public class FriendMessageActivity extends HXBaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_id) TextView textView_Id;
    @BindView(R.id.tv_concern) TextView tv_concern;
    boolean isconcern = false;
    List<FriendInfo> shows, showsnoodles;
    PersonMessageAdapter adapter, noodlesAdapter;
    private PopupWindow pw;
    @BindView(R.id.frag_mine_avatar) SimpleDraweeView frag_mine_avatar;//头像
    @BindView(R.id.frag_mine_nickname) TextView frag_mine_nickname;//
    @BindView(R.id.frag_mine_sex) ImageView frag_mine_sex;
    @BindView(R.id.frag_mine_grade) SimpleDraweeView frag_mine_grade;
    @BindView(R.id.tv_grade) TextView tv_grade;
    @BindView(R.id.iv_updata) ImageView iv_updata;
    @BindView(R.id.tv_adress) TextView tv_adress;
    @BindView(R.id.ll_bottom) View ll_bottom;
    @BindView(R.id.rl_parent) RelativeLayout rl_parent;
    @BindView(R.id.aoi_guanzhu) AngelOptionItem2 aoi_guanzhu;
    @BindView(R.id.aoi_fensi) AngelOptionItem2 aoi_fensi;
    @BindView(R.id.aoi_zhibo) AngelOptionItem2 aoi_zhibo;
    @BindView(R.id.listbillboard) AngelOptionItem listbillboard;//贡献帮
    @BindView(R.id.frag_mine_qianming) AngelOptionItem frag_mine_qianming;
    //存在感
    @BindView(R.id.frag_mine_diamond) AngelOptionItem frag_mine_diamond;//送出的钻石
    @BindView(R.id.frag_mine_neddddw) AngelOptionItem frag_mine_neddddw;//个性强命
    @BindView(R.id.message) TextView message;
    @BindView(R.id.iv_state) ImageView mIvState;//直播状态
    @BindView(R.id.tv_load) TextView mTvLoad;//数据加载中
    @BindView(R.id.tupian_linearlayout) LinearLayout mLlGrade;
    @BindView(R.id.dizhi_linerar) LinearLayout mLlAddress;
    @BindView(R.id.title_left_image) ImageButton title_left_image;
    SimpleDraweeView fansFirst;
    SimpleDraweeView fansSecond;
    SimpleDraweeView fansThird;
    SharedPreferences sp;
    private NewPerMesInfo messageInfo;
    int flag = 0;//长按点击临时标注
    int viewed_user_id;
    private UserInfo userInfo;


    @Override
    protected void initUI() {
        setContentView(R.layout.activity_friend_message);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            finish();
            return;
        }
        sp = getSharedPreferences(DemoConstants.LOCAL_DATA, MODE_PRIVATE);
        mTvLoad.setVisibility(View.VISIBLE);
        mLlGrade.setVisibility(View.GONE);
        mLlAddress.setVisibility(View.GONE);
        viewed_user_id = getIntent().getIntExtra("viewed_user_id", 0);//查看的用户的id

        if (viewed_user_id != 0) {
            Subscription subscribe = HXHttpUtil.getInstance()
                                               .getUserInfo(userInfo.user_id, viewed_user_id)
                                               .subscribe(new HttpSubscriber<NewPerMesInfo>() {
                                                   @Override
                                                   public void onNext(NewPerMesInfo newPerMesInfo) {
                                                       if (null !=
                                                               newPerMesInfo) {
                                                           messageInfo = newPerMesInfo;
                                                           initdata(newPerMesInfo);
                                                       }
                                                   }
                                               });
            addSubscription(subscribe);
        }
    }


    private void initdata(final NewPerMesInfo messageInfo) {
        mTvLoad.setVisibility(View.GONE);
        mLlAddress.setVisibility(View.VISIBLE);
        mLlGrade.setVisibility(View.VISIBLE);
        //if (messageInfo.live_info != null) {
        //    //直播中
        //    mIvState.setVisibility(View.VISIBLE);
        //    mIvState.setImageResource(R.drawable.state_public);
        //} else if (messageInfo.secret_live_info != null) {
        //    //私密直播中
        //    mIvState.setVisibility(View.VISIBLE);
        //    mIvState.setImageResource(R.drawable.state_secret);
        //}
        RxViewUtil.viewBindClick(title_left_image, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RxViewUtil.viewBindClick(iv_updata, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow1(tv_concern, viewed_user_id);
            }
        });
        RxViewUtil.viewBindClick(mIvState, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AvActivity activity = (AvActivity) HXApp.getInstance()
                                                        .getActivity(AvActivity.class);
                if (messageInfo.secret_live_info != null) {
                    if (activity == null) {
                        String string = sp
                                .getString(messageInfo.secret_live_info.userId +
                                        messageInfo.secret_live_info.roomNum +
                                        "", "");
                        if (string.isEmpty()) {
                            startAvactivity(2, messageInfo);
                        } else {
                            startAvactivity(1, messageInfo);
                        }
                    } else {
                        if (activity.mHostIdentifier
                                .equals(messageInfo.secret_live_info.userId +
                                        "")) {
                            startAvactivity(1, messageInfo);
                        } else {
                            Notifier.showShortMsg(getApplication(), "您正在观看直播");
                        }
                    }
                } else if (messageInfo.live_info != null) {
                    if (activity == null) {
                        startAvactivity(1, messageInfo);
                    } else {
                        if (activity.mHostIdentifier
                                .equals(messageInfo.live_info.userId + "")) {
                            startAvactivity(1, messageInfo);
                        } else {
                            Notifier.showShortMsg(getApplication(), "您正在观看直播");
                        }
                    }
                }
            }
        });
        if (!"".equals(messageInfo.photo)) {
            HXImageLoader.loadImage(frag_mine_avatar, messageInfo.photo, Util
                    .getPX(78), Util.getPX(78));
        }

        if (!"".equals(messageInfo.identity)) {
            textView_Id.setText("ID:" + messageInfo.identity);
        }

        frag_mine_nickname.setText(messageInfo.nickname);
        if (messageInfo.gender == 0) {
            frag_mine_sex.setImageResource(R.drawable.sex_man);
        } else {
            frag_mine_sex.setImageResource(R.drawable.sex_woman);
        }
        Xtgrade.mXtgrade(messageInfo.grade, frag_mine_grade, tv_grade);
        iv_updata.setImageResource(R.drawable.threedpoint);
        tv_adress.setText(messageInfo.address);
        aoi_guanzhu.setTitle(messageInfo.attention_amount + "");//关注的数量
        aoi_fensi.setTitle(messageInfo.fans_amount + "");//粉丝数量
        aoi_zhibo.setTitle(messageInfo.replay_amount + "");//直播数量

        View inflate = View.inflate(this, R.layout.item_fans_photo, null);
        fansFirst = (SimpleDraweeView) inflate.findViewById(R.id.fans_first);
        fansSecond = (SimpleDraweeView) inflate.findViewById(R.id.fans_second);
        fansThird = (SimpleDraweeView) inflate.findViewById(R.id.fans_third);
        listbillboard.setCustomView(inflate);
        ArrayList<String> jsonArray = messageInfo.my_fans_photo;
        for (int i = 0; i < jsonArray.size(); i++) {
            if (i == 0) {
                fansFirst.setVisibility(View.VISIBLE);
                HXImageLoader.loadImage(fansFirst, jsonArray.get(i), Util
                        .getPX(28), Util.getPX(28));
            } else if (i == 1) {
                fansSecond.setVisibility(View.VISIBLE);
                HXImageLoader.loadImage(fansSecond, jsonArray.get(i), Util
                        .getPX(28), Util.getPX(28));
            } else if (i == 2) {
                fansThird.setVisibility(View.VISIBLE);
                HXImageLoader.loadImage(fansThird, jsonArray.get(i), Util
                        .getPX(28), Util.getPX(28));
            }
        }
        listbillboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BillListActivity.class);
                intent.putExtra("viewed_user_id", viewed_user_id);
                startActivity(intent);
            }
        });
        frag_mine_qianming.setContent(messageInfo.ticket_amount + "");

        frag_mine_diamond.setContent(messageInfo.send_out_vca + "");
        if (messageInfo.signature.equals("")) {
            frag_mine_neddddw.setTitle("<这个家伙很懒，什么都没留下!>");
        } else {
            frag_mine_neddddw.setTitle(messageInfo.signature);
        }
        //关注
        aoi_guanzhu.setEffectMode(AngelMaterialProperties.EffectMode.light);
        aoi_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mActivity, MineAttentionActivity.class);
                it.putExtra("user_id", messageInfo.user_id);
                startActivity(it);
            }
        });
        //粉丝
        aoi_fensi.setEffectMode(AngelMaterialProperties.EffectMode.light);
        aoi_fensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mActivity, MineFansActivity.class);
                it.putExtra("user_id", messageInfo.user_id);
                startActivity(it);
            }
        });
        //直播
        aoi_zhibo.setEffectMode(AngelMaterialProperties.EffectMode.light);
        aoi_zhibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mActivity, MineLiveActivity.class);
                it.putExtra("user_id", messageInfo.user_id);
                it.putExtra("userName", messageInfo.nickname);
                it.putExtra("count", aoi_zhibo.getTitle());
                it.putExtra("type", 0);//最新
                startActivity(it);
            }
        });

        if (messageInfo.relation_type == 0) {
            tv_concern.setText("+加关注");
            isconcern = false;
        } else if (messageInfo.relation_type == -1) {
            tv_concern.setText("+加关注");
            isconcern = false;
        } else {
            tv_concern.setText("取消关注");
            isconcern = true;
        }
        tv_concern.setOnClickListener(this);
        message.setOnClickListener(this);
    }


    private void startAvactivity(int type, NewPerMesInfo messageInfo) {
        if (type == 1) {
            //公开直播
            Intent intent = new Intent(getApplication(), AvActivity.class);
            intent.putExtra(Utils.EXTRA_ROOM_NUM, messageInfo.live_info.roomNum);
            intent.putExtra(Utils.EXTRA_GROUP_ID, messageInfo.live_info.groupId);
            intent.putExtra(Utils.EXTRA_SELF_IDENTIFIER, messageInfo.live_info.userId);
            intent.putExtra("nickname", messageInfo.nickname);
            intent.putExtra("identity", messageInfo.identity);
            intent.putExtra("photo", messageInfo.photo);
            startActivity(intent);
        } else if (type == 2) {
            //私密直播
            startActivity(new Intent(getApplication(), PwdAvActivity.class)
                    .putExtra("personinfo", messageInfo));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_concern:// 关注
                if (!isconcern) {
                    Subscription subscribe = HXHttpUtil.getInstance()
                                                       .concern(HXApp
                                                               .getInstance()
                                                               .getUserInfo().user_id, messageInfo.user_id)
                                                       .subscribe(new HttpSubscriber<Object>() {
                                                           @Override
                                                           public void onNext(Object o) {
                                                               tv_concern
                                                                       .setText("取消关注");
                                                               aoi_fensi
                                                                       .setTitle(
                                                                               Integer.valueOf(aoi_fensi
                                                                                       .getTitle()) +
                                                                                       1 +
                                                                                       "");
                                                               GetMessageUtil
                                                                       .chageSavaData(FriendMessageActivity.this,
                                                                               messageInfo.user_id +
                                                                                       "", true);
                                                               messageInfo.relation_type = 1;
                                                               isconcern = true;
                                                           }
                                                       });
                    addSubscription(subscribe);
                } else {

                    Subscription subscribe = HXHttpUtil.getInstance()
                                                       .unconcern(HXApp
                                                               .getInstance()
                                                               .getUserInfo().user_id, messageInfo.user_id)
                                                       .subscribe(new HttpSubscriber<Object>() {
                                                           @Override
                                                           public void onNext(Object o) {
                                                               tv_concern
                                                                       .setText("+加关注");
                                                               aoi_fensi
                                                                       .setTitle(
                                                                               Integer.valueOf(aoi_fensi
                                                                                       .getTitle()) -
                                                                                       1 +
                                                                                       "");
                                                               GetMessageUtil
                                                                       .chageSavaData(FriendMessageActivity.this,
                                                                               messageInfo.user_id +
                                                                                       "", false);
                                                               messageInfo.relation_type = 0;
                                                               isconcern = false;
                                                           }
                                                       });
                    addSubscription(subscribe);
                }
                break;
            case R.id.message://私信
                Intent intent = new Intent(FriendMessageActivity.this, ChatActivity.class);
                intent.putExtra("user_id", messageInfo.user_id);
                intent.putExtra("user_name", messageInfo.nickname);
                intent.putExtra("user_photo", messageInfo.photo);
                intent.putExtra("relation", messageInfo.relation_type);
                startActivity(intent);
                break;
            case R.id.tv_blacklist://拉黑
                Subscription subscribe = HXHttpUtil.getInstance()
                                                   .pullBlack(HXApp
                                                           .getInstance()
                                                           .getUserInfo().user_id, messageInfo.user_id)
                                                   .subscribe(new HttpSubscriber<Object>() {
                                                       @Override
                                                       public void onNext(Object o) {
                                                           if (flag == 1) {
                                                               shows.remove(messageInfo);
                                                               adapter.notifyDataSetChanged();
                                                           } else if (flag ==
                                                                   2) {
                                                               showsnoodles
                                                                       .remove(messageInfo);
                                                               noodlesAdapter
                                                                       .notifyDataSetChanged();
                                                           }
                                                           Notifier.showShortMsg(FriendMessageActivity.this,
                                                                   "您已拉黑了" +
                                                                           messageInfo.nickname);
                                                           if (pw != null &&
                                                                   pw.isShowing()) {
                                                               pw.dismiss();
                                                           }
                                                       }


                                                       @Override
                                                       public void onError(Throwable e) {
                                                           super.onError(e);
                                                           Notifier.showShortMsg(FriendMessageActivity.this, "操作失败");
                                                       }
                                                   });
                addSubscription(subscribe);

                break;
            case R.id.tv_report://举报
                Notifier.showShortMsg(FriendMessageActivity.this, "还未完成，谢谢使用");
                break;
            case R.id.tv_dimiss://取消
                if (pw != null && pw.isShowing()) {
                    pw.dismiss();
                    WindowManager.LayoutParams params = FriendMessageActivity.this
                            .getWindow().getAttributes();
                    params.alpha = 1f;
                    FriendMessageActivity.this.getWindow()
                                              .setAttributes(params);
                }
                break;
        }
    }


    private void showPopupWindow1(View v, final int viewed_user_id) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.show_popu, null);
        TextView tv_blacklist = (TextView) view.findViewById(R.id.tv_blacklist);
        TextView tv_report = (TextView) view.findViewById(R.id.tv_report);
        TextView tv_dimiss = (TextView) view.findViewById(R.id.tv_dimiss);
        tv_blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subscription subscribe = HXHttpUtil.getInstance()
                                                   .pullBlack(HXApp
                                                           .getInstance()
                                                           .getUserInfo().user_id, messageInfo.user_id)
                                                   .subscribe(new HttpSubscriber<Object>() {
                                                       @Override
                                                       public void onNext(Object o) {
                                                           if (flag == 1) {
                                                               shows.remove(messageInfo);
                                                               adapter.notifyDataSetChanged();
                                                           } else if (flag ==
                                                                   2) {
                                                               showsnoodles
                                                                       .remove(messageInfo);
                                                               noodlesAdapter
                                                                       .notifyDataSetChanged();
                                                           }
                                                           ConcernInfoDao concernInfoDao = new ConcernInfoDao(FriendMessageActivity.this);
                                                           UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(FriendMessageActivity.this);
                                                           ConcernInfoDB concernInfoByUId = null;
                                                           try {
                                                               concernInfoByUId = concernInfoDao
                                                                       .getConcernInfoByUId(
                                                                               viewed_user_id +
                                                                                       "");
                                                               UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                                                       .getUnFollowConcernInfoByUId(
                                                                               viewed_user_id +
                                                                                       "");

                                                               if (null !=
                                                                       concernInfoByUId) {
                                                                   concernInfoDao
                                                                           .deleteConcernInfoByUId(
                                                                                   viewed_user_id +
                                                                                           "");
                                                               }
                                                               if (null !=
                                                                       unFollowConcernInfoByUId) {
                                                                   unconcernInfoDao
                                                                           .deleteUnFollowConcernInfoByUId(
                                                                                   viewed_user_id +
                                                                                           "");
                                                               }
                                                           } catch (Exception e) {
                                                               e.printStackTrace();
                                                           }

                                                           Notifier.showShortMsg(FriendMessageActivity.this,
                                                                   "您已拉黑了" +
                                                                           messageInfo.nickname);

                                                           tv_concern
                                                                   .setText("+加关注");
                                                           aoi_fensi.setTitle(
                                                                   Integer.valueOf(aoi_fensi
                                                                           .getTitle()) -
                                                                           1 +
                                                                           "");
                                                           isconcern = false;

                                                           if (pw != null &&
                                                                   pw.isShowing()) {
                                                               pw.dismiss();
                                                           }
                                                       }


                                                       @Override
                                                       public void onError(Throwable e) {
                                                           super.onError(e);
                                                           Notifier.showShortMsg(FriendMessageActivity.this, "操作失败");
                                                       }
                                                   });
                addSubscription(subscribe);
            }
        });
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifier.showShortMsg(FriendMessageActivity.this,
                        "您已举报了" + messageInfo.nickname);
                pw.dismiss();
            }
        });
        tv_dimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null && pw.isShowing()) {
                    pw.dismiss();
                    WindowManager.LayoutParams params = FriendMessageActivity.this
                            .getWindow().getAttributes();
                    params.alpha = 1f;
                    FriendMessageActivity.this.getWindow()
                                              .setAttributes(params);
                }
            }
        });
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        view.measure(0, 0);
        pw = new PopupWindow(view, width, view.getMeasuredHeight());
        // 点击外部可以被关闭
        pw.setOutsideTouchable(false);
        backgroundAlpha(0.7f);
        pw.setFocusable(true);
        pw.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }


    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
