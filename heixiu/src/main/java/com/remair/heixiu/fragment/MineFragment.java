package com.remair.heixiu.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.BillListActivity;
import com.remair.heixiu.activity.MineAttentionActivity;
import com.remair.heixiu.activity.MineFansActivity;
import com.remair.heixiu.activity.MineHeidouActivity;
import com.remair.heixiu.activity.MineLiveActivity;
import com.remair.heixiu.activity.MinediamondActivity;
import com.remair.heixiu.activity.ProfileActivity;
import com.remair.heixiu.activity.ProfitActivity;
import com.remair.heixiu.activity.SettingActivity;
import com.remair.heixiu.activity.WheelViewMessageActivity;
import com.remair.heixiu.bean.Profitincomebean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.fragment.base.HXBaseFragment;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.RSAUtils;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import java.util.HashMap;
import java.util.List;
import rx.Subscription;
import rx.functions.Action1;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.views.AngelDivider;
import studio.archangel.toolkitv2.views.AngelMaterialProperties;
import studio.archangel.toolkitv2.views.AngelOptionItem;
import studio.archangel.toolkitv2.views.AngelOptionItem2;

/**
 * ,
 * Created by Michael on 2015/4/8.
 */
public class MineFragment extends HXBaseFragment {
    @BindView(R.id.iv_updata) ImageView ivUpdata;
    @BindView(R.id.frag_mine_avatar) SimpleDraweeView iv_avatar;
    @BindView(R.id.frag_mine_profit) AngelOptionItem aoi_profit;
    @BindView(R.id.frag_mine_diamond) AngelOptionItem aoi_diamond;
    @BindView(R.id.frag_mine_setting) AngelOptionItem aoi_setting;
    @BindView(R.id.aoi_guanzhu) AngelOptionItem2 aoi_guanzhu;
    @BindView(R.id.aoi_fensi) AngelOptionItem2 aoi_fensi;
    @BindView(R.id.aoi_zhibo) AngelOptionItem2 aoi_zhibo;
    @BindView(R.id.frag_mine_nickname) TextView mine_nickname;
    @BindView(R.id.tv_id) TextView tv_id;
    @BindView(R.id.tv_adress) TextView tv_adress;
    @BindView(R.id.divider) AngelDivider divider;
    @BindView(R.id.frag_mine_sex) ImageView frag_mine_sex;
    @BindView(R.id.tv_grade) TextView tv_grade;
    @BindView(R.id.frag_mine_grade) SimpleDraweeView frag_mine_grade;
    @BindView(R.id.frag_mine_new) AngelOptionItem frag_mine_new;
    @BindView(R.id.listbillboard) AngelOptionItem listbillboard;//榜单
    @BindView(R.id.dizhi_linerar) LinearLayout mLlAddress;
    @BindView(R.id.tupian_linearlayout) LinearLayout mLlgrade;
    @BindView(R.id.relativelayot_gexing) RelativeLayout relativelayot_gexing;
    @BindView(R.id.person_gexingqianming) TextView person_gexingqianming;
    @BindView(R.id.title_left_image) ImageButton title_left_image;

    SimpleDraweeView fansFirst;
    SimpleDraweeView fansSecond;
    SimpleDraweeView fansThird;
    private int user_id;
    private String addr;//位置
    int px = Utils.getPX(78);
    int px1 = Utils.getPX(28);
    private Unbinder unbinder;


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo.gender == 0) {
            frag_mine_sex.setImageResource(R.drawable.sex_man);
        } else if (userInfo.gender == 1) {
            frag_mine_sex.setImageResource(R.drawable.sex_woman);
        }
        title_left_image.setVisibility(View.GONE);
        HXImageLoader.loadImage(iv_avatar, userInfo.photo, px, px);
        mine_nickname.setText(userInfo.nickname);
        tv_id.setText("ID:" + userInfo.identity);
        Xtgrade.mXtgrade(userInfo.grade, frag_mine_grade, tv_grade);
        aoi_diamond.setContent(userInfo.virtual_currency_amount + "");
        frag_mine_new.setContent(userInfo.heidou_amount + "");
        aoi_profit.setContent("¥" + Xtgrade.double2Str(userInfo.my_income));
        aoi_guanzhu.setTitle(userInfo.attention_amount + "");//关注的数量
        aoi_fensi.setTitle(userInfo.fans_amount + "");//粉丝数量
        aoi_zhibo.setTitle(userInfo.replay_amount + "");//直播数量
        relativelayot_gexing.setVisibility(View.GONE);//个性签名
        person_gexingqianming.setText(userInfo.signature.equals("")
                                      ? getString(R.string.sign)
                                      : userInfo.signature);

        RxViewUtil.viewBindClick(aoi_setting, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(getContext(), SettingActivity.class);
                startActivity(it);
            }
        });
        if (HXApp.getInstance().configMessage.withdraw_switch.equals("1")) {
            divider.setVisibility(View.VISIBLE);
            aoi_profit.setVisibility(View.VISIBLE);
            RxViewUtil.viewBindClick(aoi_profit, new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    UserInfo userInfo1 = HXApp.getInstance().getUserInfo();
                    if (userInfo1 == null) {
                        return;
                    }
                    HXHttpUtil.getInstance().income().subscribe(new HttpSubscriber<Profitincomebean>() {
                        @Override
                        public void onNext(Profitincomebean profitincomebean) {
                            Intent it = new Intent(getContext(), ProfitActivity.class);
                            it.putExtra("yue", profitincomebean);
                            startActivity(it);
                        }


                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            Intent it = new Intent(getContext(), ProfitActivity.class);
                            it.putExtra("yue", "json");
                            startActivity(it);
                        }
                    });
                    //HashMap<String, Object> params = new HashMap<>();
                    //params.put("user_id", userInfo1.user_id);
                    //HXJavaNet
                    //        .post(HXJavaNet.url_newincome, params, new AngelNetCallBack() {
                    //            @Override
                    //            public void onStart() {
                    //                super.onStart();
                    //            }
                    //
                    //
                    //            @Override
                    //            public void onSuccess(final int status, final String json, final String readable_msg) {
                    //                if (getContext() == null) {
                    //                    return;
                    //                }
                    //                Logger.out(status + " " + json + " " +
                    //                        readable_msg);
                    //                try {
                    //                    Intent it = new Intent(getContext(), ProfitActivity.class);
                    //                    it.putExtra("yue", json);
                    //                    startActivity(it);
                    //                } catch (Exception e) {
                    //                    e.printStackTrace();
                    //                }
                    //            }
                    //
                    //
                    //            @Override
                    //            public void onFailure(String msg) {
                    //                if (getContext() == null) {
                    //                    return;
                    //                }
                    //                Intent it = new Intent(getContext(), ProfitActivity.class);
                    //                it.putExtra("yue", "json");
                    //                startActivity(it);
                    //
                    //                Logger.err(msg);
                    //            }
                    //        });
                }
            });
        }

        Logger.out("recharge_switch" + HXApp.getInstance().configMessage.recharge_switch);
        if (HXApp.getInstance().configMessage.recharge_switch.equals("1")) {
            aoi_diamond.setVisibility(View.VISIBLE);
            RxViewUtil.viewBindClick(aoi_diamond, new Action1<Void>() {

                private String mEncrypt;


                @Override
                public void call(Void aVoid) {
                    if (HXApp.getInstance().configMessage.ali_recharge_switch.equals("1")) {
                        try {
                            String source =
                                    HXApp.getInstance().getUserInfo().identity +
                                            "," +
                                            HXApp.getInstance()
                                                 .getUserInfo().user_id;
                            mEncrypt = RSAUtils.encrypt(source);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (mEncrypt == null) {
                            Intent it = new Intent(getContext(), MinediamondActivity.class);
                            startActivity(it);
                            return;
                        }
                        Intent intent = new Intent(getContext(), WheelViewMessageActivity.class);
                        if (HXApp.isTest) {
                            intent.putExtra("url", "http://www.imheixiu.com/alipay/?ispost=1");
                        } else {
                            intent.putExtra("url", "http://www-test.imheixiu.com/alipay/?ispost=1");
                        }

                        intent.putExtra("title", getString(R.string.fast_pay));
                        intent.putExtra("data", mEncrypt);
                        intent.putExtra("carousel_id", DemoConstants.CAROUSEL_ID);
                        startActivity(intent);
                    } else {
                        Intent it = new Intent(getContext(), MinediamondActivity.class);
                        startActivity(it);
                    }
                }
            });
        }

        //位置
        addr = TextUtils.isEmpty(userInfo.address) ? "火星来的？" : userInfo.address;
        tv_adress.setText(addr);

        aoi_guanzhu.setEffectMode(AngelMaterialProperties.EffectMode.light);
        //关注
        RxViewUtil.viewBindClick(aoi_guanzhu, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(getContext(), MineAttentionActivity.class);
                it.putExtra("user_id", user_id);
                startActivity(it);
            }
        });
        //粉丝
        aoi_fensi.setEffectMode(AngelMaterialProperties.EffectMode.light);
        RxViewUtil.viewBindClick(aoi_fensi, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(getContext(), MineFansActivity.class);
                it.putExtra("user_id", user_id);
                startActivity(it);
            }
        });
        //回放
        aoi_zhibo.setEffectMode(AngelMaterialProperties.EffectMode.light);
        RxViewUtil.viewBindClick(aoi_zhibo, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(getContext(), MineLiveActivity.class);
                it.putExtra("user_id", user_id);
                it.putExtra("count", aoi_zhibo.getTitle());
                it.putExtra("type", 0);//最新
                startActivity(it);
            }
        });

        //嘿豆
        RxViewUtil.viewBindClick(frag_mine_new, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(getContext(), MineHeidouActivity.class);
                startActivity(it);
            }
        });
        View inflate = View
                .inflate(getContext(), R.layout.item_fans_photo, null);
        fansFirst = (SimpleDraweeView) inflate.findViewById(R.id.fans_first);
        fansSecond = (SimpleDraweeView) inflate.findViewById(R.id.fans_second);
        fansThird = (SimpleDraweeView) inflate.findViewById(R.id.fans_third);
        listbillboard.setCustomView(inflate);
        //榜单
        RxViewUtil.viewBindClick(listbillboard, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), BillListActivity.class);
                intent.putExtra("viewed_user_id", user_id);
                startActivity(intent);
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo user = HXApp.getInstance().getUserInfo();
                if (user == null) {
                    return;
                }
                Intent it = new Intent(getContext(), ProfileActivity.class);
                it.putExtra("avatar", user.photo);
                it.putExtra("name", user.nickname);
                it.putExtra("user_id", user.identity);
                it.putExtra("signature", user.signature);
                startActivity(it);
            }
        };
        RxViewUtil.viewBindClick(ivUpdata, listener);
        RxViewUtil.viewBindClick(iv_avatar, listener);
        RxViewUtil.viewBindClick(mLlgrade, listener);
        user_id = userInfo.user_id;
        return view;
    }


    @Override
    public void initData() {
        if (HXApp.getInstance().loading_info) {
            return;
        }
        UserInfo info = HXApp.getInstance().getUserInfo();
        if (info == null) {
            return;
        }
        Subscription subscription = HXHttpUtil.getInstance()
                                              .getUserInfos(user_id, user_id)
                                              .subscribe(new HttpSubscriber<UserInfo>() {
                                                  @Override
                                                  public void onStart() {
                                                      super.onStart();
                                                      HXApp.getInstance().loading_info = true;
                                                  }


                                                  @Override
                                                  public void onNext(UserInfo userInfo) {
                                                      if (getActivity() ==
                                                              null ||
                                                              getActivity()
                                                                      .isFinishing()) {
                                                          return;
                                                      }
                                                      HXApp.getInstance().loading_info = false;
                                                      HXApp.getInstance()
                                                           .setUserInfo(userInfo);
                                                      List<String> myFansPhoto = userInfo.my_fans_photo;
                                                      if (myFansPhoto != null &&
                                                              !myFansPhoto
                                                                      .isEmpty()) {
                                                          for (int i = 0; i <
                                                                  myFansPhoto
                                                                          .size();
                                                               i++) {
                                                              if (i == 0) {
                                                                  HXImageLoader
                                                                          .loadImage(fansFirst, myFansPhoto
                                                                                  .get(i), px1, px1);
                                                              } else if (i ==
                                                                      1) {
                                                                  HXImageLoader
                                                                          .loadImage(fansSecond, myFansPhoto
                                                                                  .get(i), px1, px1);
                                                              } else if (i ==
                                                                      2) {
                                                                  HXImageLoader
                                                                          .loadImage(fansThird, myFansPhoto
                                                                                  .get(i), px1, px1);
                                                              }
                                                          }
                                                      }
                                                      aoi_diamond.setContent(
                                                              userInfo.virtual_currency_amount +
                                                                      "");
                                                      frag_mine_new.setContent(
                                                              userInfo.heidou_amount +
                                                                      "");
                                                      aoi_profit.setContent(
                                                              "¥" +
                                                                      Xtgrade.double2Str(userInfo.my_income));
                                                      HXImageLoader
                                                              .loadImage(iv_avatar, userInfo.photo, px, px);
                                                      mine_nickname
                                                              .setText(userInfo.nickname);
                                                      tv_id.setText("ID:" +
                                                              userInfo.identity);
                                                      aoi_guanzhu.setTitle(
                                                              userInfo.attention_amount +
                                                                      "");//关注的数量
                                                      aoi_fensi.setTitle(
                                                              userInfo.fans_amount +
                                                                      "");//粉丝数量
                                                      aoi_zhibo.setTitle(
                                                              userInfo.replay_amount +
                                                                      "");//直播数量
                                                      tv_adress
                                                              .setText(userInfo.address);
                                                  }


                                                  @Override
                                                  public void onError(Throwable e) {
                                                      super.onError(e);
                                                      HXApp.getInstance().loading_info = false;
                                                  }
                                              });
        addSubscription(subscription);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        HXApp.getInstance().loading_info = false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
