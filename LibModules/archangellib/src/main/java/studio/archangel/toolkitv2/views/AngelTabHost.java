package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;
import android.widget.TabHost;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/18.
 */
public class AngelTabHost extends FragmentTabHost {
    OnSpecialTabButtonClickListener listener;
    ArrayList<Boolean> is_special_button;

    public AngelTabHost(Context context) {
        this(context, null);
    }

    public AngelTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        is_special_button = new ArrayList<>();
    }

    @Override
    public void setCurrentTab(int index) {
        if (is_special_button.size() > index && is_special_button.get(index)) {
            if (listener != null) {
                listener.OnSpecialTabButtonClicked(index);
            }
        } else {
            super.setCurrentTab(index);
        }
    }

    public boolean isSpecialButtonTab(int index) {
        if (is_special_button == null) {
            return false;
        }
        if (index >= is_special_button.size()) {
            return false;
        }
        return is_special_button.get(index);
    }

    @Override
    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        super.addTab(tabSpec, clss, args);
        is_special_button.add(false);
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args, boolean is_special_button) {
        super.addTab(tabSpec, clss, args);
        this.is_special_button.add(is_special_button);
    }

    public interface OnSpecialTabButtonClickListener {
        void OnSpecialTabButtonClicked(int index);
    }
}
