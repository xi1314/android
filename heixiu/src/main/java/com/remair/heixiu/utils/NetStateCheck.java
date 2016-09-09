package com.remair.heixiu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetStateCheck {
    private static NetStateCheck mNetStateCheck = null;


    private NetStateCheck() {
        // TODO Auto-generated constructor stub
    }


    public static NetStateCheck getInstance() {

        if (mNetStateCheck == null) {
            mNetStateCheck = new NetStateCheck();
        }
        return mNetStateCheck;
    }


    public boolean isNetworkConnected(Context context) {
        if (context != null) {

            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }
}
