package com.remair.heixiu.fragment;

import android.content.Intent;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.FriendMessageActivity;
import com.remair.heixiu.fragment.base.HXBaseFragment;

/**
 * ,
 * Created by Michael on 2015/4/8.
 */
public class FindFragment extends HXBaseFragment {

    @BindView(R.id.web_view) public WebView webView;
    @BindView(R.id.title_txt) public TextView title_txt;
    @BindView(R.id.title_left_image) public ImageButton title_left_image;


    //public void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //    setHasOptionsMenu(true);
    //    SharedPreferenceUtil.setContext(getContext());
    //}


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        View view=inflater.inflate(R.layout.act_webview,container,false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void initData() {
        title_txt.setText(getString(R.string.list));
        title_left_image.setVisibility(View.GONE);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setNeedInitialFocus(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSaveFormData(true);
        settings.setBlockNetworkImage(false);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        webView.loadUrl("http://app.imheixiu.com/Find_page/rank");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (Patterns.WEB_URL.matcher(url).matches()) {
                    //符合标准
                    webView.loadUrl(url);
                } else {
                    //不符合标准
                    startActivity(new Intent(getContext(), FriendMessageActivity.class)
                            .putExtra("viewed_user_id", Integer
                                    .parseInt(url.split(":")[1])));
                }
                return true;
            }
        });
    }


    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //    if (!super
    //            .onCreateView(inflater, container, savedInstanceState, R.layout.act_webview)) {
    //        ButterKnife.bind(this, cache);
    //    }
    //    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) webView
    //            .getLayoutParams();
    //    layoutParams.topMargin = Util.getPX(48);
    //    webView.requestLayout();
    //
    //    WebSettings settings = webView.getSettings();
    //    settings.setJavaScriptEnabled(true);
    //    settings.setNeedInitialFocus(true);
    //    settings.setBuiltInZoomControls(true);
    //    settings.setSupportZoom(true);
    //    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    //    settings.setUseWideViewPort(true);
    //    settings.setLoadWithOverviewMode(true);
    //    settings.setSaveFormData(true);
    //    settings.setBlockNetworkImage(false);
    //    settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    //    settings.setUseWideViewPort(true);
    //    //自适应屏幕
    //    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    //    settings.setLoadWithOverviewMode(true);
    //    webView.loadUrl("http://app.imheixiu.com/Find_page/rank");
    //    webView.setWebViewClient(new WebViewClient() {
    //        @Override
    //        public boolean shouldOverrideUrlLoading(WebView view, String url) {
    //            if (Patterns.WEB_URL.matcher(url).matches()) {
    //                //符合标准
    //                webView.loadUrl(url);
    //            } else {
    //                //不符合标准
    //                startActivity(new Intent(getContext(), FriendMessageActivity.class)
    //                        .putExtra("viewed_user_id", Integer
    //                                .parseInt(url.split(":")[1])));
    //            }
    //            return true;
    //        }
    //    });
    //    return cache;
    //}


    //@Override
    //public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    //    AngelActionBar aab = getSelf().getAngelActionBar();
    //    if (aab == null) {
    //        return;
    //    }
    //    aab.setBackgroundResource(R.drawable.shape_main_background);
    //    aab.setTitleText("榜单");
    //    aab.setTextColor(getResources().getColor(R.color.white));
    //    aab.setDisplayMode(AngelActionBar.DisplayPosition.left, AngelActionBar.DisplayMode.none);
    //    aab.setDisplayMode(AngelActionBar.DisplayPosition.right, AngelActionBar.DisplayMode.none);
    //}
}
