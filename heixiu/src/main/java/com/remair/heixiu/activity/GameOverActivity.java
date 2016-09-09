package com.remair.heixiu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXActivity;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.utils.GetMessageUtil;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;
import java.util.HashMap;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelDialog;

/**
 * 直播结束界面
 */
public class GameOverActivity extends HXActivity {
    public final static int ERROR_INTERNET = 0;
    public final static int SHOW_LIVE_INFO = ERROR_INTERNET + 1;
    @BindView(R.id.act_live_over_like) TextView tv_like;
    @BindView(R.id.saveRecord) TextView saveRecord;
    @BindView(R.id.act_live_over_title) TextView tv_title;
    @BindView(R.id.act_live_over_back) View b_back;
    @BindView(R.id.imageview_backbg) SimpleDraweeView imageview_backbg;

    @BindView(R.id.act_live_over_atton) TextView act_live_over_atton;

    private boolean hostleave = false;
    private int live_viewed_num;
    private int live_praise_num;
    private TextView mtextView;
    private String mPhoto;
    private SimpleDraweeView hostHeadh;
    private TextView act_live_over_like1;
    private TextView text_textview_name;
    private TextView act_live_over_view1111;

    private int attention_amount_living;
    private int charm_value;
    String mhost_id;

    private String live_user_nickname;
    int live_user_guanzhu;
    private boolean iscreate;

    boolean save;
    HashMap<String, Object> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }
        setContentView(R.layout.act_live_over);
        ButterKnife.bind(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        mtextView = (TextView) findViewById(R.id.act_live_over_view111);
        hostHeadh = (SimpleDraweeView) findViewById(R.id.host_headh);
        act_live_over_view1111 = (TextView) findViewById(R.id.act_live_over_view1111);
        text_textview_name = (TextView) findViewById(R.id.text_textview_name);
        act_live_over_like1 = (TextView) findViewById(R.id.act_live_over_like1);

        iscreate = bundle.getBoolean("iscreate", false);
        live_user_guanzhu = bundle.getInt("live_user_guanzhu", 0);
        live_praise_num = bundle.getInt("live_praise_num", 0);
        mPhoto = bundle.getString(Utils.EXTRA_ROOM_NUM_PHOTO);
        live_viewed_num = bundle.getInt("live_viewed_num");
        mhost_id = bundle.getString("mhost_id", "");
        live_praise_num = bundle.getInt("live_praise_num");
        attention_amount_living = bundle.getInt("attention_amount_living");
        charm_value = bundle.getInt("charm_value", 0);
        live_user_nickname = bundle.getString("live_user_nickname");

        mtextView.setText(live_viewed_num + "");
        tv_like.setText("" + live_praise_num);
        act_live_over_like1.setText(attention_amount_living + "");
        text_textview_name.setText(live_user_nickname);
        act_live_over_view1111.setText(charm_value + "");

        if (iscreate) {
            save = bundle.getBoolean("saveRecord", false);
            params = new HashMap<>();
            params.put("room_num", bundle.getInt("room_num", 0));
            params.put("vid", bundle.getString("vid", ""));
            params.put("duration", bundle.getLong("duration", 0));
            params.put("user_id", bundle.getInt("user_id", 0));
            if (save) {
                params.put("optType", 1);
                saveRecord.setVisibility(View.VISIBLE);
                saveRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameOver(params, 1);
                    }
                });
            } else {
                params.put("optType", 0);
                gameOver(params, 0);
            }
        } else {
            act_live_over_atton.setVisibility(View.VISIBLE);
            if (bundle.getInt("live_user_guanzhu", 0) == 1) {
                act_live_over_atton.setText("已关注");
            }
            act_live_over_atton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (live_user_guanzhu != 1) {
                        live_user_guanzhu = 1;
                        HashMap<String, Object> para = new HashMap<>();
                        para.put("user_id", getIntent()
                                .getIntExtra("user_id", 0));
                        para.put("passive_user_id", mhost_id + "");
                        HXJavaNet
                                .post(HXJavaNet.url_concern, para, new AngelNetCallBack() {
                                    @Override
                                    public void onSuccess(int ret_code, String ret_data, String msg) {

                                        if (ret_code == 200) {

                                            GetMessageUtil
                                                    .chageSavaData(GameOverActivity.this,
                                                            mhost_id +
                                                                    "", true);
                                            act_live_over_atton.setText("已关注");
                                        } else {
                                            Notifier.showShortMsg(GameOverActivity.this, "关注失败，请重试");
                                        }
                                    }


                                    @Override
                                    public void onFailure(String msg) {
                                        Notifier.showShortMsg(GameOverActivity.this, "关注失败，请重试");
                                    }
                                });
                    } else {
                        live_user_guanzhu = 0;
                        HashMap<String, Object> para = new HashMap<>();
                        para.put("user_id", getIntent()
                                .getIntExtra("user_id", 0));
                        para.put("passive_user_id", mhost_id + "");
                        HXJavaNet
                                .post(HXJavaNet.url_unconcern, para, new AngelNetCallBack() {

                                    @Override
                                    public void onSuccess(int ret_code, String ret_data, String msg) {
                                        if (ret_code == 200) {
                                            act_live_over_atton.setText("关注主播");
                                            GetMessageUtil
                                                    .chageSavaData(GameOverActivity.this,
                                                            mhost_id +
                                                                    "", false);
                                        }
                                    }


                                    @Override
                                    public void onFailure(String msg) {

                                    }
                                });
                    }
                }
            });
            findViewById(R.id.LinearLayout_live_over_like)
                    .setVisibility(View.GONE);
            findViewById(R.id.act_live_over_tmp11)
                    .setVisibility(View.INVISIBLE);
        }
        HXImageLoader
                .loadImage(hostHeadh, mPhoto, Utils.getPX(98), Utils.getPX(98));
        HXImageLoader
                .loadBlurImage(imageview_backbg, mPhoto, widthPixels, heightPixels);
        hostleave = bundle.getBoolean(Utils.EXTRA_LEAVE_MODE);
        if (hostleave) {
            tv_title.setText("主播已离开");
        }

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void gameOver(HashMap<String, Object> params, final int optType) {
        save = false;
        HXJavaNet.post(HXJavaNet.url_fileid, params, new AngelNetCallBack() {
            @Override
            public void onSuccess(int ret_code, String ret_data, String msg) {
                if (optType == 0 || isFinishing()) {
                    return;
                }
                if (ret_code == 406) {
                    final AngelDialog dislog = new AngelDialog(GameOverActivity.this, "注意", "您保存的视频数已达上限，请清理后再试");
                    dislog.getOkButton().setText("去清理");
                    dislog.getCancelButton().setText("放弃");
                    dislog.setOnOkClickedListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), MineLiveActivity.class));
                            dislog.getOkButton().setText("保存");
                            dislog.getCancelButton().setText("放弃");
                        }
                    });
                    dislog.setOnCancelClickedListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dislog.dismiss();
                        }
                    });
                    dislog.show();
                } else if (ret_code == 200) {
                    Notifier.showShortMsg(getApplication(), "保存成功");
                    finish();
                }
            }


            @Override
            public void onFailure(String msg) {

            }
        });
    }


    @Override
    public void finish() {
        if (iscreate && save) {
            gameOver(params, 0);
        }
        super.finish();
        overridePendingTransition(0, R.anim.fade_out);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
