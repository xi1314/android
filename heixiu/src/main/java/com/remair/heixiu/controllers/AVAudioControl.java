package com.remair.heixiu.controllers;

import android.content.Context;
import android.content.Intent;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.utils.Utils;
import com.tencent.av.sdk.AVAudioCtrl;
import com.tencent.av.sdk.AVAudioCtrl.Delegate;
import com.tencent.av.sdk.AVError;

public class AVAudioControl {
    private Context mContext = null;



    private Delegate mDelegate = new Delegate() {
        @Override
        protected void onOutputModeChange(int outputMode) {
            super.onOutputModeChange(outputMode);
            mContext.sendBroadcast(new Intent(Utils.ACTION_OUTPUT_MODE_CHANGE));
        }
    };

    AVAudioControl(Context context) {
        mContext = context;
    }

    void initAVAudio() {
        QavsdkControl qavsdk = ((HXApp) mContext)
                .getQavsdkControl();
        qavsdk.getAVContext().getAudioCtrl().setDelegate(mDelegate);
    }

    boolean getHandfreeChecked() {
        QavsdkControl qavsdk = ((HXApp) mContext)
                .getQavsdkControl();
        return qavsdk.getAVContext().getAudioCtrl().getAudioOutputMode() == AVAudioCtrl.OUTPUT_MODE_HEADSET;
    }

    String getQualityTips() {
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVAudioCtrl avAudioCtrl;
        if (qavsdk != null) {
            avAudioCtrl = qavsdk.getAVContext().getAudioCtrl();
            return avAudioCtrl.getQualityTips();
        }

        return "";
    }


    void registAudioDataCallback1() {
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVAudioCtrl avAudioCtrl;
        if (qavsdk != null) {
            avAudioCtrl = qavsdk.getAVContext().getAudioCtrl();
            avAudioCtrl.registAudioDataCallback(AVAudioCtrl.AudioDataSourceType.AUDIO_DATA_SOURCE_MIXTOPLAY
                    , callback);
        }


    }

    void unregistAudioDataCallback1() {
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVAudioCtrl avAudioCtrl;
        if (qavsdk != null) {
            avAudioCtrl = qavsdk.getAVContext().getAudioCtrl();
          avAudioCtrl.unregistAudioDataCallback(AVAudioCtrl.AudioDataSourceType.AUDIO_DATA_SOURCE_MIXTOSEND); //取消注册麦克风采集输出回调

        }
    }

    AVAudioCtrl.RegistAudioDataCompleteCallback callback = new AVAudioCtrl.RegistAudioDataCompleteCallback() {
        protected int onComplete(AVAudioCtrl.AudioFrame audioframe, int srcType ) {
   /* do something */
            return AVError.AV_OK;
        }
    };


}