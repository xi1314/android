package com.remair.heixiu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.bean.UserInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;

/**
 * Created by JXHIUUI on 2016/3/14.
 */
public class LocationService extends Service implements AMapLocationListener {
    //高德定位

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient;

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption
                .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());

        //设置定位回调监听
        mLocationClient.setLocationListener(this);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = aMapLocation.getLatitude();//获取纬度
                double longitude = aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation
                        .getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                String province = aMapLocation.getProvince();//省信息
                final String city = aMapLocation.getCity();//城市信息
                String district = aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                String cityCode = aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码
                String aoiName = aMapLocation.getAoiName();//获取当前定位点的AOI信息
                String locationDetail = aMapLocation.getLocationDetail();
                final int user_id = HXApp.getInstance().getUserInfo().user_id;
                HashMap<String, Object> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("location", province);
                params.put("city_name", city);
                params.put("city_code", cityCode);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("district", district);
                params.put("location_detail", locationDetail);

                HXJavaNet
                        .post(HXJavaNet.url_location, params, new AngelNetCallBack() {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                UserInfo userInfo = HXApp.getInstance()
                                                         .getUserInfo();
                                if (ret_code == 200 && userInfo != null) {
                                    userInfo.address = city;
                                    HXApp.getInstance().setUserInfo(userInfo);
                                }
                            }


                            @Override
                            public void onFailure(String msg) {
                            }
                        });

                //关闭服务
                stopSelf();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Logger.err("location Error, ErrCode:" +
                        aMapLocation.getErrorCode() + ", errInfo:" +
                        aMapLocation.getErrorInfo());
                stopSelf();
            }
        } else {
            stopSelf();
        }
    }
}

