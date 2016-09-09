package com.remair.heixiu.utils;

import android.graphics.Bitmap;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.remair.heixiu.view.FastBlur;
import studio.archangel.toolkitv2.util.Logger;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/22 11:43
 * 修改人：LiuJun
 * 修改时间：16/8/22 11:43
 * 修改备注：
 */
public class BlurPostprocessor extends BasePostprocessor {

    private static int MAX_RADIUS = 25;

    private String mUrl;
    private int mRadius;


    public BlurPostprocessor(String url) {
        this(url, MAX_RADIUS);
    }


    public BlurPostprocessor(String url, int radius) {
        this.mUrl = url;
        this.mRadius = radius;
    }


    @Override
    public void process(Bitmap bitmap) {
        long startMs = System.currentTimeMillis();
        FastBlur.doBlur(bitmap, mRadius, true);
        Logger.out("sourceBitmap Width:" + bitmap.getWidth() +
                " sourceBitmap Height:" + bitmap.getHeight() +
                "   time:" + (System.currentTimeMillis() - startMs) + "ms");
    }


    @Override
    public CacheKey getPostprocessorCacheKey() {
        return new SimpleCacheKey(mUrl + ",radius=" + mRadius);
    }
}
