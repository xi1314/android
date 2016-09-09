package studio.archangel.toolkitv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.facebook.drawee.drawable.ScalingUtils;
import java.util.Timer;
import java.util.TimerTask;
import studio.archangel.toolkitv2.util.Logger;

/**
 * Created by Michael on 2014/9/24.
 */
public abstract class AngelApplication extends Application {
    protected static String prefix = "";
    public static AngelApplication instance;
    public static String cpu_arch;
    public static int screen_width;
    public static int screen_height;
    public static float screen_width_dp;
    public static float screen_height_dp;
    public static int status_bar_height;
    public static String device_des;
    public static int app_version_code;
    public static String app_version_name;
    public static String app_package_name;
    static boolean is_debug = true;
    static boolean is_test_server = true;
    static boolean immersive = true;
    //    static boolean user_has_login = false;

    public static final int result_ok = Activity.RESULT_OK;
    public static final int result_fail = 1001;
    public static final int result_special = 1002;
    public static final int result_cancel = Activity.RESULT_CANCELED;
    private Timer activity_transition_timer;
    private TimerTask activity_transition_timer_task;
    public boolean was_in_background;
    public long activity_taken_to_back_time_stamp;
    boolean stop_timer_for_once = false;


    @Override
    public void onCreate() {
        super.onCreate();
        getScreenSize();
        instance = this;
        Logger.setEnable(true);
        initAPPParameters();
        initExtraTools();
    }


    void initAPPParameters() {
        cpu_arch = Build.CPU_ABI;
        if (Build.CPU_ABI2 != null && !Build.CPU_ABI2.isEmpty()) {
            cpu_arch += "（" + Build.CPU_ABI2 + "）";
        }
        //        Logger.out("cpu_arch:" + cpu_arch);
        Logger.setEnable(false);
        device_des = android.os.Build.MODEL + "(" + Build.VERSION.RELEASE + ")";

        try {
            app_version_name = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            app_version_name = "未知版本";
            e.printStackTrace();
        }
        try {
            app_package_name = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            app_package_name = "";
            e.printStackTrace();
        }
        try {
            app_version_code = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            app_version_code = -1;
            e.printStackTrace();
        }
    }


    void initExtraTools() {
        //        LeakCanary.install(this);
        //        TinyDancer.create()
        //                .show(this);
        //
        //        //alternatively
        //        TinyDancer.create()
        //                .redFlagPercentage(.1f) // set red indicator for 10%
        //                .startingGravity(Gravity.TOP)
        //                .startingXPosition(200)
        //                .startingYPosition(600)
        //                .show(this);
        //
        //        //you can add a callback to get frame times and the calculated
        //        //number of dropped frames within that window
        //        TinyDancer.create()
        //                .addFrameDataCallback(new FrameDataCallback() {
        //                    @Override
        //                    public void doFrame(long previousFrameNS, long currentFrameNS, int droppedFrames) {
        //                        //collect your stats here
        //                    }
        //                })
        //                .show(this);

    }


    public static AngelApplication getInstance() {
        return instance;
    }


    public static boolean isDebug() {
        return is_debug;
    }


    public static void setDebug(boolean b) {
        is_debug = b;
    }


    public static boolean isTestServer() {
        return is_test_server;
    }


    public static void setIsTestServer(boolean is_test_server) {
        AngelApplication.is_test_server = is_test_server;
    }


    public static boolean hasLogin() {
        return instance.getPreference().getBoolean("user_has_login", false);
    }


    public static void setHasLogin(boolean b) {
        instance.getEditor().putBoolean("user_has_login", b).commit();
    }


    public SharedPreferences getPreference() {
        return getSharedPreferences(getPrefix(), MODE_PRIVATE);
    }


    @SuppressLint("CommitPrefEdits")
    public SharedPreferences.Editor getEditor() {
        SharedPreferences pref = getPreference();
        SharedPreferences.Editor editor = pref.edit();
        return editor;
    }


    public static String getPrefix() {
        return prefix;
    }


    public static void setPrefix(String p) {
        prefix = p;
    }


    public abstract int getGalleryActivityPlaceholder();

    public abstract ScalingUtils.ScaleType getGalleryActivityPlaceholderScaleType();

    public abstract boolean isImmersiveMode();

    public abstract void loadLocalData();

    public abstract void saveLocalData();

    public abstract void clearLocalData();

    public abstract void initDB();

    public abstract long getTransitionTimeBetweenActivities();


    void getScreenSize() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;
        int resourceId = getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            status_bar_height = getResources()
                    .getDimensionPixelSize(resourceId);
        }
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float density = getResources().getDisplayMetrics().density;
        screen_width_dp = metrics.widthPixels / density;
        screen_height_dp = metrics.heightPixels / density;
        Logger.out("screen size:" + screen_width + "×" + screen_height);
    }


    public void setLastUserName(String username) {
        getEditor().putString("last_user_name", username).commit();
    }


    public String getLastUserName() {
        return getPreference().getString("last_user_name", "");
    }


    public void stopActivityTransitionTimerForOnce() {
        stop_timer_for_once = true;
    }

    /*public void startActivityTransitionTimer(final AngelActivity act) {
        if (stop_timer_for_once) {
            stop_timer_for_once = false;
            Logger.out("Timer stoped for once");
            return;
        }
        this.activity_transition_timer = new Timer();
        this.activity_transition_timer_task = new TimerTask() {
            public void run() {
                activity_taken_to_back_time_stamp = System.currentTimeMillis();
                act.onTakenToBackground(activity_taken_to_back_time_stamp - getTransitionTimeBetweenActivities());
                was_in_background = true;
                Logger.out("App was brought to background through " + act.getClass().getName());
                onAppBroughtToBack();
            }
        };

        this.activity_transition_timer.schedule(activity_transition_timer_task, getTransitionTimeBetweenActivities());
    }*/

    /*public void startActivityTransitionTimer(final AngelActivityV4 act) {
        if (stop_timer_for_once) {
            stop_timer_for_once = false;
            Logger.out("Timer stoped for once");
            return;
        }
        this.activity_transition_timer = new Timer();
        this.activity_transition_timer_task = new TimerTask() {
            public void run() {
                activity_taken_to_back_time_stamp = System.currentTimeMillis();
                act.onTakenToBackground(activity_taken_to_back_time_stamp - getTransitionTimeBetweenActivities());
                was_in_background = true;
                Logger.out("App was brought to background through " + act.getClass().getName());
                onAppBroughtToBack();
            }
        };

        this.activity_transition_timer.schedule(activity_transition_timer_task, getTransitionTimeBetweenActivities());
    }*/


    public static boolean hasFrontCamera() {
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return true;
            }
        }
        return false;
    }

   /* public void stopActivityTransitionTimer() {
        if (this.activity_transition_timer_task != null) {
            this.activity_transition_timer_task.cancel();
        }

        if (this.activity_transition_timer != null) {
            this.activity_transition_timer.cancel();
        }

        this.was_in_background = false;
    }*/




    public void onAppBroughtToBack() {

    }


    public void onAppBroughtToFront() {

    }
}
