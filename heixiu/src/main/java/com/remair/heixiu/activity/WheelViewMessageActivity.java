package com.remair.heixiu.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：16/4/18 11:42
 * 修改人：JXH
 * 修改时间：16/4/18 11:42
 * 修改备注：
 */
public class WheelViewMessageActivity extends HXBaseActivity implements View.OnClickListener{

    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;
    @BindView(R.id.web_view) WebView mWebView;
    @BindView(R.id.title_right_text) TextView mTitleRightText;
    @BindView(R.id.ll_webview) LinearLayout mLlWebview;
    private Context mContext;
    private Map<String, String> stringMap;
    private String substring;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_webview);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        mContext = getApplicationContext();
        String url = getIntent().getStringExtra("url");
        if (null == url) {
            return;
        }
        final String title = getIntent().getStringExtra("title");
        String data = getIntent().getStringExtra("data");
        mTitleTxt.setText(title);
        if (getIntent().getIntExtra("carousel_id", -1) ==
                DemoConstants.CAROUSEL_ID) {
            mTitleRightText.setText(getString(R.string.charge_his));
        }

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setBlockNetworkImage(false);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        int i = url.lastIndexOf("?");
        substring = url.substring(i + 1);
        if (substring.equals("ispost=1")) {
            String postdataza = "sign=" + data;
            mWebView.postUrl(url, postdataza.getBytes());
            ShareSDK.initSDK(mContext);
        } else {
            mWebView.loadUrl(url);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Uri parse = Uri.parse(url);
                Set<String> queryParameterNames = parse
                        .getQueryParameterNames();
                if (queryParameterNames.size() == 0) {
                    int i = url.lastIndexOf("/");
                    String substring = url.substring(i + 1);
                    if (substring != null) {
                        if (substring.equals("chargepage")) {
                            Intent intent = new Intent(getApplicationContext(), MinediamondActivity.class);
                            startActivity(intent);
                            return true;
                        } else if (substring.equals("heidoupage")) {
                            Intent intent = new Intent(getApplicationContext(), MineHeidouActivity.class);
                            startActivity(intent);
                            return true;
                        }
                    } else {
                        return true;
                    }
                }

                stringMap = new HashMap<String, String>();
                for (String i : queryParameterNames) {
                    String queryParameter = parse.getQueryParameter(i);
                    stringMap.put(i, queryParameter);
                }
                if (stringMap.size() == 0) {
                    mWebView.loadUrl(url);
                    return true;
                }
                if (stringMap.containsKey("shareurl")) {
                    View inflate = View
                            .inflate(getApplicationContext(), R.layout.web_share, null);
                    PopupWindow popupWindow = new PopupWindow(inflate, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.shape_button_white));
                    popupWindow
                            .showAtLocation(mLlWebview, Gravity.BOTTOM, mLlWebview
                                    .getLeft(), 0);
                    inflate.findViewById(R.id.qq)
                           .setOnClickListener(WheelViewMessageActivity.this);
                    inflate.findViewById(R.id.wechat)
                           .setOnClickListener(WheelViewMessageActivity.this);
                    inflate.findViewById(R.id.wechat_fr)
                           .setOnClickListener(WheelViewMessageActivity.this);
                } else {
                    mWebView.loadUrl(url);
                }
                HXApp.getInstance().loading_info = true;
                return true;
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (mWebView != null) {
            if (mWebView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
                mWebView.goBack();   //返回webView的上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCoder, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(mContext);
    }


    @OnClick({ R.id.title_left_image, R.id.title_right_text })
    public void onClick(View view) {
        String url = null;
        if (stringMap != null) {
            url = stringMap.get("shareurl") + "?id=" +
                    stringMap.get("id") +
                    "&sign=" + stringMap.get("sign");
        }
        switch (view.getId()) {
            case R.id.title_left_image:
                finish();
                break;
            case R.id.title_right_text:
                Intent intent = new Intent(mActivity, CaseHistryActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.qq:
                QQ.ShareParams qq = new QQ.ShareParams();
                qq.setTitle(stringMap.get("title"));
                qq.setText(stringMap.get("description"));
                qq.setImageUrl(HXApp.getInstance().getUserInfo().photo);
                qq.setTitleUrl(url);
                Platform platformQq = ShareSDK.getPlatform(QQ.NAME);
                platformQq.share(qq);
                break;
            case R.id.wechat:
                Wechat.ShareParams wechatSp = new Wechat.ShareParams();
                wechatSp.setTitle(stringMap.get("title"));
                wechatSp.setText(stringMap.get("description"));
                wechatSp.setImageUrl(HXApp.getInstance().getUserInfo().photo);
                wechatSp.setUrl(url);
                wechatSp.setShareType(Platform.SHARE_WEBPAGE);
                Platform weChat = ShareSDK.getPlatform(Wechat.NAME);
                weChat.share(wechatSp);
                break;
            case R.id.wechat_fr:
                WechatMoments.ShareParams wechatMom = new WechatMoments.ShareParams();
                wechatMom.setTitle(stringMap.get("title"));
                wechatMom.setText(stringMap.get("description"));
                wechatMom.setImageUrl(HXApp.getInstance().getUserInfo().photo);
                wechatMom.setUrl(url);
                wechatMom.setShareType(Platform.SHARE_WEBPAGE);
                Platform wechatPlat = ShareSDK.getPlatform(WechatMoments.NAME);
                wechatPlat.share(wechatMom);
                break;
        }
    }
}
