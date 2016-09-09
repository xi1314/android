package com.remair.heixiu.controllers;

//test
/*
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
*/

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.utils.Utils;
import com.tencent.av.sdk.AVError;
import com.tencent.av.sdk.AVVideoCtrl;
import com.tencent.av.sdk.AVVideoCtrl.EnableCameraCompleteCallback;
import com.tencent.av.sdk.AVVideoCtrl.EnableExternalCaptureCompleteCallback;
import com.tencent.av.sdk.AVVideoCtrl.RemoteVideoPreviewCallback;
import com.tencent.av.sdk.AVVideoCtrl.SwitchCameraCompleteCallback;
import com.tencent.av.sdk.AVVideoCtrl.VideoFrame;
import studio.archangel.toolkitv2.util.Logger;

public class AVVideoControl {
    private static final String TAG = "AVVideoControl";
    private static final int CAMERA_NONE = -1;
    private static final int FRONT_CAMERA = 0;
    private static final int BACK_CAMERA = 1;
    RemoteVideoPreviewCallback remoteVideoPreviewCallback = new RemoteVideoPreviewCallback() {
        public void onFrameReceive(VideoFrame videoFrame) {
            Logger.out("real RemoteVideoPreviewCallback.onFrameReceive");
            Logger.out("len: " + videoFrame.dataLen);
            Logger.out("openid: " + videoFrame.identifier);
        }
    };


    private Context mContext = null;
    private boolean mIsEnableCamera = false;
    private boolean mCurrentCamera = true;
    private boolean mIsInOnOffCamera = false;
    private boolean mIsInSwitchCamera = false;
    private boolean mIsOnOffExternalCapture = false;
    private boolean mIsEnableExternalCapture = false;
    private EnableCameraCompleteCallback mEnableCameraCompleteCallback = new EnableCameraCompleteCallback() {
        protected void onComplete(boolean enable, int result) {
            super.onComplete(enable, result);
            Logger.out("WL_DEBUG mEnableCameraCompleteCallback.onComplete enable = " + enable);
            Logger.out("WL_DEBUG mEnableCameraCompleteCallback.onComplete result = " + result);
            mIsInOnOffCamera = false;

            if (result == AVError.AV_OK) {
                mIsEnableCamera = enable;
            }
            Logger.out("WL_DEBUG mEnableCameraCompleteCallback.onComplete mIsEnableCamera = " + mIsEnableCamera);
            mContext.sendBroadcast(new Intent(Utils.ACTION_ENABLE_CAMERA_COMPLETE).putExtra(Utils.EXTRA_AV_ERROR_RESULT, result).putExtra(Utils.EXTRA_IS_ENABLE, enable));
        }
    };
    private EnableExternalCaptureCompleteCallback mEnableExternalCaptureCompleteCallback = new EnableExternalCaptureCompleteCallback() {
        @Override
        protected void onComplete(boolean enable, int result) {
            super.onComplete(enable, result);
            Logger.out("WL_DEBUG mEnableExternalCaptureCompleteCallback.onComplete enable = " + enable);
            Logger.out("WL_DEBUG mEnableExternalCaptureCompleteCallback.onComplete result = " + result);
            mIsOnOffExternalCapture = false;
            Log.e("This", "heheheehehehehehe");
            if (result == AVError.AV_OK) {
                mIsEnableExternalCapture = enable;
            }

            mContext.sendBroadcast(new Intent(Utils.ACTION_ENABLE_EXTERNAL_CAPTURE_COMPLETE).putExtra(Utils.EXTRA_AV_ERROR_RESULT, result).putExtra(Utils.EXTRA_IS_ENABLE, enable));

        }
    };
    private SwitchCameraCompleteCallback mSwitchCameraCompleteCallback = new SwitchCameraCompleteCallback() {
        protected void onComplete(int cameraId, int result) {
            super.onComplete(cameraId, result);
            Logger.out("WL_DEBUG mSwitchCameraCompleteCallback.onComplete cameraId = " + cameraId);
            Logger.out("WL_DEBUG mSwitchCameraCompleteCallback.onComplete result = " + result);
            mIsInSwitchCamera = false;
            boolean isFront = cameraId == FRONT_CAMERA;

            if (result == AVError.AV_OK) {
                mCurrentCamera = isFront;
            }

            mContext.sendBroadcast(new Intent(Utils.ACTION_SWITCH_CAMERA_COMPLETE).putExtra(Utils.EXTRA_AV_ERROR_RESULT, result).putExtra(Utils.EXTRA_IS_FRONT, isFront));
        }
    };

    public AVVideoControl(Context context) {
        mContext = context;
    }

    //打开或关闭摄像头
    int enableCamera(boolean isEnable) {
        int result = AVError.AV_OK;

        if (mIsEnableCamera != isEnable) {
            QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
            AVVideoCtrl avVideoCtrl = qavsdk.getAVContext().getVideoCtrl();
            mIsInOnOffCamera = true;
            // TODO: 2016/4/14
            result = avVideoCtrl.enableCamera(FRONT_CAMERA, isEnable, mEnableCameraCompleteCallback);
//            if (result==AVError.AV_ERR_FAILED){
//                mContext.sendBroadcast(new Intent(Utils.ACTION_CLOSE_ROOM_COMPLETE).putExtra(Utils.EXTRA_AV_ERROR_RESULT, result));
//            }
//            if (result!=AVError.AV_OK){
//                mIsInOnOffCamera = false;
//                mContext.sendBroadcast(new Intent(Utils.ACTION_EXTRA_STOPRECORDD).putExtra("true",false));
//            }
            mCurrentCamera = true;
        }
        Logger.out("WL_DEBUG enableCamera: " + result);
        Logger.out("WL_DEBUG enableCamera isEnable = " + isEnable);
        Logger.out("WL_DEBUG enableCamera  mIsInOnOffCamera = " + mIsInOnOffCamera);
        return result;
    }

    public int enableExternalCapture(boolean isEnable) {

        int result = AVError.AV_OK;
        if (mIsEnableExternalCapture != isEnable) {
            QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
            AVVideoCtrl avVideoCtrl = qavsdk.getAVContext().getVideoCtrl();
            mIsOnOffExternalCapture = true;
            result = avVideoCtrl.enableExternalCapture(isEnable, mEnableExternalCaptureCompleteCallback);
        }

        Logger.out("WL_DEBUG enableExternalCapture isEnable = " + isEnable);
        Logger.out("WL_DEBUG enableExternalCapture result = " + result);
        return result;
    }

    public int enableExternalCapture(boolean isEnable, EnableExternalCaptureCompleteCallback mEnableExternalCaptureCompleteCallback) {

        int result = AVError.AV_OK;
        if (mIsEnableExternalCapture != isEnable) {
            QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
            AVVideoCtrl avVideoCtrl = qavsdk.getAVContext().getVideoCtrl();
            mIsOnOffExternalCapture = true;
            result = avVideoCtrl.enableExternalCapture(isEnable, mEnableExternalCaptureCompleteCallback);
        }

        Logger.out("WL_DEBUG enableExternalCapture isEnable = " + isEnable);
        Logger.out("WL_DEBUG enableExternalCapture result = " + result);
        return result;
    }


    //切换摄像头
    int switchCamera(boolean needCamera) {
        int result = AVError.AV_OK;
        if (mCurrentCamera != needCamera) {
            QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
            AVVideoCtrl avVideoCtrl = qavsdk.getAVContext().getVideoCtrl();
            mIsInSwitchCamera = true;
            Logger.out("switchCamera 1111 currentCamera " + mCurrentCamera + " needCamera  " + needCamera);
            if (mCurrentCamera) {
                Logger.out("switchCamera to backCamera ");

            } else {
                Logger.out("switchCamera to frontCamera ");
            }

            result = avVideoCtrl.switchCamera(needCamera ? FRONT_CAMERA : BACK_CAMERA, mSwitchCameraCompleteCallback);
        }
        Logger.out("WL_DEBUG switchCamera isFront = " + needCamera);
        Logger.out("WL_DEBUG switchCamera result = " + result);
        return result;
    }

    //设置摄像头旋转角度
    void setRotation(int rotation) {
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVVideoCtrl avVideoCtrl = qavsdk.getAVContext().getVideoCtrl();
        avVideoCtrl.setRotation(rotation);
        Logger.out("WL_DEBUG setRotation rotation = " + rotation);

    }

    //获取通话中实时视频质量相关信息
    String getQualityTips() {
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVVideoCtrl avVideoCtrl = qavsdk.getAVContext().getVideoCtrl();
        return avVideoCtrl.getQualityTips();
    }

    int toggleEnableCamera() {
        return enableCamera(!mIsEnableCamera);
    }

    public boolean enableCustomerRendMode() {
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVVideoCtrl avVideoCtrl = qavsdk.getAVContext().getVideoCtrl();
        return avVideoCtrl.setRemoteVideoPreviewCallback(remoteVideoPreviewCallback);
    }

    public boolean enableCustomerRendMode(AVVideoCtrl.LocalVideoPreviewCallback var1) {
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVVideoCtrl avVideoCtrl = qavsdk.getAVContext().getVideoCtrl();
        return avVideoCtrl.setLocalVideoPreviewCallback(var1);

    }

    public boolean enableCustomerRendMode(RemoteVideoPreviewCallback remoteVideoPreviewCallback) {
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVVideoCtrl avVideoCtrl = qavsdk.getAVContext().getVideoCtrl();
        return avVideoCtrl.setRemoteVideoPreviewCallback(remoteVideoPreviewCallback);
    }


    public boolean enableviewRendMode1(AVVideoCtrl.LocalVideoPreProcessCallback var1) {
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVVideoCtrl avVideoCtrl = qavsdk.getAVContext().getVideoCtrl();
        return avVideoCtrl.setLocalVideoPreProcessCallback(var1);

    }

    public boolean enableviewRendMode2(AVVideoCtrl.RemoteScreenVideoPreviewCallback var1) {
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVVideoCtrl avVideoCtrl = qavsdk.getAVContext().getVideoCtrl();
        return avVideoCtrl.setRemoteScreenVideoPreviewCallback(var1);

    }


    int toggleSwitchCamera() {
        Logger.out("toggleSwitchCamera mCurrentCamera: " + mCurrentCamera);
        return switchCamera(!mCurrentCamera);
    }

    boolean getIsInOnOffCamera() {
        return mIsInOnOffCamera;
    }

    public void setIsInOnOffCamera(boolean isInOnOffCamera) {
        this.mIsInOnOffCamera = isInOnOffCamera;
    }

    boolean getIsInSwitchCamera() {
        return mIsInSwitchCamera;
    }

    public void setIsInSwitchCamera(boolean isInSwitchCamera) {
        this.mIsInSwitchCamera = isInSwitchCamera;
    }

    boolean getIsEnableCamera() {
        return mIsEnableCamera;
    }

    boolean getIsInOnOffExternalCapture() {
        return mIsOnOffExternalCapture;
    }

    public boolean getIsEnableExternalCapture() {
        return mIsEnableExternalCapture;
    }

    public void setIsOnOffExternalCapture(boolean isOnOffExternalCapture) {
        this.mIsOnOffExternalCapture = isOnOffExternalCapture;
    }

    boolean getIsFrontCamera() {
        return mCurrentCamera;
    }

    void initAVVideo() {
        mIsEnableCamera = false;
        mCurrentCamera = true;
        mIsInOnOffCamera = false;
        mIsInSwitchCamera = false;
        mIsEnableExternalCapture = false;
        mIsOnOffExternalCapture = false;
    }
}