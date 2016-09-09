package studio.archangel.toolkitv2.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import studio.archangel.toolkitv2.util.Logger;

/**
 * Created by Michael on 2014/12/27.
 */
public class AngelViewPagerFragment extends Fragment {
    public String name;
    //    TextView tv;
    //    int back_res_id;
    public ViewGroup root;
    boolean displaying = false;
    boolean force_display = false;
    boolean inited = false;
    boolean debug = true;
    int layout_res;
    onVisibilityChangedListener listener;

    Handler display_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            displaying = true;
            if (debug)
                Logger.out(name + ":" + "onBroughtToFront");
            if (listener != null) {
                listener.onVisibilityChanged(true);
            }
        }
    };

    public AngelViewPagerFragment() {

    }

    public static Fragment newInstance(Context c, Class clazz, String name, int layout_res, boolean force_display, Bundle extra) {
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putInt("layout_res", layout_res);
        args.putBoolean("force_display", force_display);
        if (extra != null) {
            args.putAll(extra);
        }
        return Fragment.instantiate(c, clazz.getName(), args);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        if (debug)
            Logger.out(name + ":" + "onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (debug)
            Logger.out(name + ":" + "onResume");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (debug)
            Logger.out(name + ":" + "onActivityCreated");
        inited = true;
        if (force_display) {
            force_display = false;
            onBroughtToFront();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (debug)
            Logger.out(name + ":" + "onPause");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (debug)
            Logger.out(name + ":" + "onCreateView");
//        if(root==null){

        root = (ViewGroup) inflater.inflate(layout_res, container, false);
//        tv = (TextView) root.findViewById(R.id.frag_test_tv);
//        tv.setText(name);
//        if (back_res_id != -1) {
//            root.setBackgroundColor(getResources().getColor(back_res_id));
//        }
//        }else{
//            Logger.out(title + ":" + "use cache!!!");
//        }

        return root;

    }

    public void setOnVisibilityChangedListener(onVisibilityChangedListener listener) {
        this.listener = listener;
    }

    public void onBroughtToFront() {
        new Thread(new Runnable() {
            long countdown = 5000;

            @Override
            public void run() {
                while (true) {
                    if (AngelViewPagerFragment.this == null || countdown < 0) {
                        if (debug)
                            Logger.out(name + ":" + "onBroughtToFront--destroyed or wait too long, abort");
                        break;
                    }
                    if (inited) {
                        display_handler.sendEmptyMessage(0);
                        break;
                    } else {
                        try {
                            if (debug)
                                Logger.out(name + ":" + "onBroughtToFront--Not inited yet, wait for 500ms...");
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        countdown -= 500;
                    }
                }
            }
        }).start();

    }


    public void onBroughtToBack() {
        if (!displaying) {
            if (debug)
                Logger.out(name + ":" + "onBroughtToBack:already back");
            return;
        }
        if (debug)
            Logger.out(name + ":" + "onBroughtToBack");
        displaying = false;
        if (listener != null) {
            listener.onVisibilityChanged(false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        name = args.getString("name", "default");
        layout_res = args.getInt("layout_res", -1);
//        back_res_id = args.getInt("back", -1);
        force_display = args.getBoolean("force_display", false);
        if (debug)
            Logger.out(name + ":" + "onCreate");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (debug)
            Logger.out(name + ":" + "onAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (debug)
            Logger.out(name + ":" + "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (debug)
            Logger.out(name + ":" + "onDetach");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (debug)
            Logger.out(name + ":" + "onHiddenChanged");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (debug)
            Logger.out(name + ":" + "onLowMemory");
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        if (debug)
            Logger.out(name + ":" + "onInflate");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (debug)
            Logger.out(name + ":" + "onSaveInstanceState");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (debug)
            Logger.out(name + ":" + "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (debug)
            Logger.out(name + ":" + "onStop");
    }

//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//        if (debug)
//            Logger.out(name + ":" + "onTrimMemory");
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (debug)
            Logger.out(name + ":" + "onViewCreated");

    }

    public interface onVisibilityChangedListener {
        public void onVisibilityChanged(boolean to_front);
    }

}
