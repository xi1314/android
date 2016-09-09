/**
 *
 */
package studio.archangel.toolkitv2.activities;


import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import com.badoo.mobile.util.WeakHandler;
import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.ui.SystemBarTintManager;
import studio.archangel.toolkitv2.views.AngelActionBar;
import studio.archangel.toolkitv2.views.AngelLoadingDialog;

/**
 * @author Administrator
 */
public abstract class AngelActivityV4 extends FragmentActivity {
    public AngelLoadingDialog dialog;
    public boolean destroyed = false;
    //    Validator validator;
    private boolean orientation_set = false;
    private AngelActionBar bar;
    SystemBarTintManager tint_manager;
    public WeakHandler handler;
    //    public boolean track_app_status_change = true;
    /**
     * 加载指定的Feature
     *
     * @param savedInstanceState
     * @param feature_id
     */
    protected void onCreate(Bundle savedInstanceState, int[] feature_id, int orientation) {
        super.onCreate(savedInstanceState);
        init(feature_id, orientation);
    }

    protected void onCreate(Bundle savedInstanceState, int[] feature_id) {
        super.onCreate(savedInstanceState);
        init(feature_id, -1);
    }

    protected AngelActivityV4 getSelf() {
        return this;
    }

//    public boolean shouldTrackAppStatusChange() {
//        return track_app_status_change;
//    }
//
//    public void setShouldTrackAppStatusChange(boolean track_app_status_change) {
//        this.track_app_status_change = track_app_status_change;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init(null, -1);
    }

    public AngelActionBar getAngelActionBar() {
        return bar;
    }

    public void setAngelActionBar(AngelActionBar bar) {
        this.bar = bar;
    }


    protected void setOrientation(int info) {
        if (!shouldOverrideOrientation()) {
            return;
        }
        if (info == -1) {
            return;
        }
        try {
            setRequestedOrientation(info);
            orientation_set = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        destroyed = true;
        if (dialog != null) {
            dialog.forceDismiss();
        }
        super.onDestroy();
    }

    protected boolean shouldOverrideOrientation() {
        return false;
    }

    /**
     * 初始化。将Actionbar的图标设置为App图标，并将点击图标映射为返回键
     *
     * @param feature_id 要加载的Feature的id
     */
    void init(int[] feature_id, int orientation) {
        setOrientation(orientation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && false) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;

            win.setAttributes(winParams);
            // 创建状态栏的管理实例
            tint_manager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tint_manager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tint_manager.setNavigationBarTintEnabled(true);
            setStatusBarColor(getDefaultStatusBarColor());
        }
        if (!orientation_set) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (feature_id != null) {
            for (int id : feature_id) {
                requestWindowFeature(id);
                if (id == Window.FEATURE_NO_TITLE) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            }
        }
//        if (feature_id != -10) {
//            requestWindowFeature(feature_id);
//        }
        try {
            getActionBar().setIcon(R.drawable.ic_launcher);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
//            Logger.err(e.toString());
//            e.printStackTrace();
        }
        handler = new WeakHandler();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    protected void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    public abstract int getDefaultStatusBarColor();

    public void setStatusBarColor(int color) {
        if (tint_manager != null) {
            tint_manager.setStatusBarTintColor(color);
        }
    }
}
