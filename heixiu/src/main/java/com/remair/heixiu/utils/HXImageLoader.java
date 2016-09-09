package com.remair.heixiu.utils;

import android.net.Uri;
import android.text.TextUtils;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import info.liujun.image.ImageLoader;
import info.liujun.image.LJImageRequestBuilder;
import java.util.Locale;
import studio.archangel.toolkitv2.util.Logger;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/17 20:17
 * 修改人：LiuJun
 * 修改时间：16/8/17 20:17
 * 修改备注：
 */
public class HXImageLoader {

    public static final String OSS_CN_BEIJING = "oss-cn-beijing";
    public static final String IMG_CN_BEIJING = "img-cn-beijing";
    public static final String IMAGE_STYLE = "@%sw_%sh_1l_1e_1c_90Q_2o_1pr";
    public static final String BLUR_STYLE = "_%s-%sbl";
    public static final String GIF_STYLE = "@%sw_%sh_1l_1e";
    public static final String SUFFIX = "_1an.src";


    private HXImageLoader() {}


    /**
     * 加载图片
     *
     * @param width 控件宽度 单位：像素
     * @param height 控件高度 单位：像素
     */
    public static void loadImage(SimpleDraweeView view, String url, int width, int height) {
        if (view == null || TextUtils.isEmpty(url)) {
            return;
        }
        String newUrl = urlWithWidthAndHeight(url, width, height);
        try {
            Logger.outUpper("image: " + newUrl, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageLoader.loadImage(view, newUrl, newUrl);
    }


    /**
     * 加载模糊效果的图片
     *
     * @param width 控件宽度 单位：像素
     * @param height 控件高度 单位：像素
     */
    public static void loadBlurImage(SimpleDraweeView view, String url, int width, int height) {
        if (view == null || TextUtils.isEmpty(url)) {
            return;
        }
        if (isOssImage(url)) {
            String blurUrl = getBlurUrl(url, width, height);
            try {
                Logger.outUpper("blurImage: " + blurUrl, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ImageLoader.loadImage(view, blurUrl, blurUrl);
        } else {
            loadOtherBlurImage(view, url, width, height);
        }
    }


    public static void loadOtherBlurImage(SimpleDraweeView view, String url, int width, int height) {
        ResizeOptions resizeOptions;
        if (checkoutWH(width, height)) {
            int w, h;
            if (width > 2000 || height > 2000) {
                w = width / 6;
                h = height / 6;
            } else if (width > 1000 || height > 1000) {
                w = width / 5;
                h = height / 5;
            } else {
                w = width / 4;
                h = height / 4;
            }
            resizeOptions = new ResizeOptions(w, h);
        } else {
            resizeOptions = new ResizeOptions(150, 150);
        }
        LJImageRequestBuilder request = LJImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url), url);
        request.getImageRequestBuilder()
               .setPostprocessor(new BlurPostprocessor(url))
               .setProgressiveRenderingEnabled(true)
               .setResizeOptions(resizeOptions).build();
        ImageLoader.loadImage(view, request.build());
    }


    public static void loadGifImage(SimpleDraweeView view, String url, int width, int height) {
        if (view == null || TextUtils.isEmpty(url)) {
            return;
        }
        //String gifUrl = getGifUrl(url, width, height);
        try {
            Logger.outUpper("gifImage: " + url, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageLoader.loadImage(view, url, url);
    }


    public static void loadGifFromLocal(SimpleDraweeView view, int resId, int width, int height) {
        if (view == null || resId == 0) {
            return;
        }
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithResourceId(resId)
                .setResizeOptions(new ResizeOptions(width, height)).build();
        DraweeController draweeController = ImageLoader
                .getDraweeController(view, imageRequest);
        view.setController(draweeController);
    }


    /**
     * 使用图片控件的宽高对阿里OSS上的图片URL进行处理，添加模糊效果
     *
     * @param w 图片宽度
     * @param h 图片高度
     */
    public static String getBlurUrl(String url, int w, int h) {
        if (!checkoutParameter(url, w, h)) {
            return url;
        }
        return replaceEndpoint(url) + getImageBlurStyle(w, h) + SUFFIX;
    }


    /**
     * 使用图片控件的宽高对阿里OSS上的图片URL进行处理
     *
     * @param w 图片宽度
     * @param h 图片高度
     */
    public static String urlWithWidthAndHeight(String url, int w, int h) {
        if (!checkoutParameter(url, w, h)) {
            return url;
        }
        return replaceEndpoint(url) + getImageStyle(w, h) + SUFFIX;
    }


    public static String getGifUrl(String url, int w, int h) {
        if (!checkoutParameter(url, w, h)) {
            return url;
        }
        return replaceEndpoint(url) + getImageGifStyle(w, h) + SUFFIX;
    }


    /**
     * 检查属性是否需要对URL进行处理
     *
     * @return true:不需要进行处理   false:需要对URL进行处理
     */
    private static boolean checkoutParameter(String url, int w, int h) {
        return isOssImage(url) && checkoutWH(w, h);
    }


    private static boolean checkoutWH(int w, int h) {return w > 1 || h > 1;}


    private static boolean isOssImage(String url) {
        return url.contains(OSS_CN_BEIJING);
    }


    private static String replaceEndpoint(String url) {
        url = url.replace(OSS_CN_BEIJING, IMG_CN_BEIJING);
        return url;
    }


    public static String getImageStyle(int w, int h) {
        return String.format(Locale.getDefault(), IMAGE_STYLE, w, h);
    }


    public static String getImageBlurStyle(int w, int h) {
        return getImageStyle(w, h) +
                String.format(Locale.getDefault(), BLUR_STYLE, 30, 15);
    }


    public static String getImageGifStyle(int w, int h) {
        return String.format(Locale.getDefault(), GIF_STYLE, w, h);
    }


    public static Uri getUriFromLocal(int resId) {
        return new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                                .path(String.valueOf(resId)).build();
    }
}
