package com.remair.heixiu.activity;

import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.BuildConfig;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.RxViewUtil;
import org.json.JSONObject;
import rx.functions.Action1;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelOptionItem;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/3/16 15:57
 * 修改人：JXH
 * 修改时间：2016/3/16 15:57
 * 修改备注：
 */
public class AboutMeActivity extends HXBaseActivity {
    @BindView(R.id.tv_version) TextView tv_version;
    @BindView(R.id.protocol_about) AngelOptionItem protocol_about;
    public String url;
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_about_me);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        mTitleTxt.setText("关于我们");
        tv_version.setText("版本号" + BuildConfig.VERSION_NAME);
        HXHttpUtil.getInstance().url_about_us()
                  .subscribe(new HttpSubscriber<JSONObject>() {
                      @Override
                      public void onNext(JSONObject s) {
                          if (s == null) {
                              return;
                          }
                          url = s.optString("url");
                      }
                  });

        RxViewUtil.viewBindClick(protocol_about, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (null != url) {
                    Intent intent = new Intent(getApplication(), ProtocolActivity.class);
                    intent.putExtra("url", AboutMeActivity.this.url);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                } else {
                    Notifier.showShortMsg(getApplication(), "请求失败，稍后再试");
                }
            }
        });
        RxViewUtil.viewBindClick(mTitleLeftImage, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        RxViewUtil.viewBindClick(protocol_about, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (null != url) {
                    Intent intent = new Intent(getApplication(), ProtocolActivity.class);
                    intent.putExtra("url", AboutMeActivity.this.url);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                } else {
                    Notifier.showShortMsg(getApplication(), "请求失败，稍后再试");
                }
            }
        });
    }
}
