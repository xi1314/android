package com.remair.heixiu.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.utils.RxViewUtil;
import rx.functions.Action1;

/**
 * 项目名称：Android
 * 类描述： 封号规则显示
 * 创建人：Jw
 * 创建时间：16/9/2 下午8:45
 * 修改人：
 * 修改时间：16/9/2 下午8:45
 * 修改备注：
 */
public class ForbidRulesActivity extends HXBaseActivity {
    @BindView(R.id.web_view) WebView mWebView;
    @BindView(R.id.title_txt) TextView mTitle;
    @BindView(R.id.title_left_image) View title_left_image;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_forbid_rules);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        mTitle.setText("封禁规则");
        mWebView.loadUrl("file:///android_asset/forbidrules.html");
        RxViewUtil.viewBindClick(/**/title_left_image, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
    }
}
