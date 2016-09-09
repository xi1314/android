package com.remair.heixiu.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.utils.RSAUtils;
import com.remair.heixiu.utils.RxViewUtil;

/**
 *
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/3/16 11:02
 * 修改人：JXH
 * 修改时间：2016/3/16 11:02
 * 修改备注：
 * @version
 *
 */
public class ProtocolActivity extends HXBaseActivity {
    @BindView(R.id.title_txt) TextView title_txt;
    @BindView(R.id.title_left_image) ImageButton title_left_image;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_webview);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        int type = getIntent().getIntExtra("type", 0);
        String url = getIntent().getStringExtra("url");
        final WebView webView = (WebView) findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setBuiltInZoomControls(false);
        String serverRoot = "http://www.imheixiu.com";

        if (type == 1) {
            title_txt.setText(getString(R.string.profit));
            webView.loadUrl(serverRoot + "/service_agreement.html");
        } else if (type == 2) {
            title_txt.setText(getString(R.string.income_protocol));
            webView.loadUrl(serverRoot + "/withdrawal_agreement.html");
        } else if (type == 3) {
            title_txt.setText(getString(R.string.service_protocol));
            String source = HXApp.getInstance().getUserInfo().identity + "," +
                    HXApp.getInstance().getUserInfo().user_id;
            String data;
            try {
                data = RSAUtils.encrypt(source);
                String postdata = "sign=" + data;
                webView.postUrl(
                        serverRoot + "/labourAgreement.php?ispost=1", postdata
                                .getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        RxViewUtil.viewBindClick(title_left_image, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
