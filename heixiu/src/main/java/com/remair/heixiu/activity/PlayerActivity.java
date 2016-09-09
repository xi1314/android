package com.remair.heixiu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import com.remair.heixiu.R;
import com.tencent.qcload.playersdk.ui.VideoRootFrame;
import com.tencent.qcload.playersdk.util.VideoInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JXHIUUI on 2016/6/27.
 */
public class PlayerActivity extends Activity {
    VideoRootFrame player;
    List<VideoInfo> videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        //获取页面中的播放器控件
        String url = getIntent().getExtras().getString("url");
        player = (VideoRootFrame) findViewById(R.id.player);


        ImageButton close = (ImageButton) findViewById(R.id.ib_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //调用播放器的播放方法
        videos = new ArrayList<VideoInfo>();
        VideoInfo v1 = new VideoInfo();
        v1.description = "标清";
        v1.type = VideoInfo.VideoType.MP4;
        v1.url = url;
        videos.add(v1);
        VideoInfo v2 = new VideoInfo();
        v2.description = "高清";
        v2.type = VideoInfo.VideoType.MP4;
        v2.url = url;
        videos.add(v2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.play(videos);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}
