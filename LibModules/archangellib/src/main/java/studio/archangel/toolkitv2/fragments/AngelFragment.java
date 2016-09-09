/**
 *
 */
package studio.archangel.toolkitv2.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import studio.archangel.toolkitv2.activities.AngelActivity;

/**
 * @author Michael
 */
public class AngelFragment extends Fragment {
    protected AngelActivity owner;
    protected View cache = null;
    //    Validator validator;
    protected String realname;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        owner = (AngelActivity) activity;
    }

    public AngelActivity getSelf() {
        return owner;
    }

    public AngelFragment() {
        super();
        this.realname = getClass().getName();
    }

    public AngelFragment(String realname) {
        super();
        this.realname = realname;
    }

    /**
     * 初始化方法，可以使用缓存，使得此Fragment在ViewPager中不会因为划出屏幕而重新加载
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @param layout
     * @return
     */
    public boolean onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int layout) {
        boolean use_cache = (cache != null);
        if (!use_cache) {
            cache = inflater.inflate(layout, null);
        }

        ViewGroup parent = (ViewGroup) cache.getParent();
        if (parent != null) {
            parent.removeView(cache);
        }
        return use_cache;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //    protected void setValidator(Validator.ValidationListener l) {
//        validator = new Validator(this);
//        validator.setValidationListener(l);
//    }
//
//    protected void validate() {
//        if (validator != null) {
//            validator.validate();
//        }
//    }


}
