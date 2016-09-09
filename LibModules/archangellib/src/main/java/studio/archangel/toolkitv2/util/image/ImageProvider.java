package studio.archangel.toolkitv2.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Animatable;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import studio.archangel.toolkitv2.interfaces.ImageLoadingListener;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.Util;

/**
 * Created by Michael on 2015/5/15.
 */
public class ImageProvider {

    public static void loadGifPicInApp(@NonNull SimpleDraweeView simpleDraweeView, @NonNull int resId) {
        if (simpleDraweeView == null) {
            return;
        }
        Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                                   .path(String.valueOf(resId)).build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                                                  .setUri(uri)
                                                  .setAutoPlayAnimations(true)
                                                  .build();
        simpleDraweeView.setController(draweeController);
    }


    public static void load(View v, Object res) {
        ImageRequestBuilder request_builder = null;
        if (res instanceof Integer) {
            request_builder = ImageRequestBuilder
                    .newBuilderWithResourceId((Integer) res)
                    .setCacheChoice(ImageRequest.CacheChoice.DEFAULT);
        } else if (res instanceof String) {
            request_builder = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse((String) res));
        }
        if (request_builder == null) {
            Logger.out("Failed to create image request builder.");
            return;
        }
        request_builder.setAutoRotateEnabled(true)
                       .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                       .setProgressiveRenderingEnabled(true);
        if (!(v instanceof ImageView)) {
            Logger.out("Target view is not a hxapp ImageView.");
            return;
        }
        if (v instanceof SimpleDraweeView) {
            final SimpleDraweeView iv = (SimpleDraweeView) v;
            Uri tag = (Uri) iv.getTag();
            if (tag == null ||
                    (request_builder.getSourceUri()).compareTo(tag) != 0) {
                //            iv.setImageURI(uri);
                iv.setTag(request_builder.getSourceUri());
                PipelineDraweeControllerBuilder controller_builder = Fresco
                        .newDraweeControllerBuilder();
                controller_builder.setOldController(iv.getController())
                                  .setAutoPlayAnimations(true)
                                  .setImageRequest(request_builder.build());

                iv.setController(controller_builder.build());
            }
        } else {
            final ImageView iv = (ImageView) v;
            DataSource<CloseableReference<CloseableImage>> dataSource = Fresco
                    .getImagePipeline().
                            fetchDecodedImage(request_builder
                                    .build(), Util.app_context);

            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                    iv.post(new Runnable() {
                        public void run() {
                            iv.setImageBitmap(bitmap);
                        }
                    });
                }


                @Override
                public void onFailureImpl(DataSource dataSource) {
                    // No cleanup required here.
                }
            }, CallerThreadExecutor.getInstance());
        }
    }


    public static void load3(View v, Object res) {
        ImageRequestBuilder request_builder = null;
        if (res instanceof Integer) {
            request_builder = ImageRequestBuilder
                    .newBuilderWithResourceId((Integer) res)
                    .setCacheChoice(ImageRequest.CacheChoice.DEFAULT)
                    .setResizeOptions(new ResizeOptions(Util.getPX(130), Util
                            .getPX(130)));
            //            request_builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse("res:///"+ res));

        } else if (res instanceof String) {
            request_builder = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse((String) res));
        }
        if (request_builder == null) {
            Logger.out("Failed to create image request builder.");
            return;
        }
        request_builder.setAutoRotateEnabled(false)
                       .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                       .setProgressiveRenderingEnabled(false);
        //        if (v instanceof AngelCompoundImageView) {
        //            AngelCompoundImageView aciv = (AngelCompoundImageView) v;
        //            load(aciv.getImageView(), res, l);
        //            return;
        //        }
        if (!(v instanceof ImageView)) {
            Logger.out("Target view is not a ImageView.");
            return;
        }
        if (v instanceof SimpleDraweeView) {
            final SimpleDraweeView iv = (SimpleDraweeView) v;
            Uri tag = (Uri) iv.getTag();
            if (tag == null ||
                    (request_builder.getSourceUri()).compareTo(tag) != 0) {
                iv.setTag(request_builder.getSourceUri());
                PipelineDraweeControllerBuilder controller_builder = Fresco
                        .newDraweeControllerBuilder();
                controller_builder.setOldController(iv.getController())
                                  .setAutoPlayAnimations(true)
                                  .setImageRequest(request_builder.build());

                iv.setController(controller_builder.build());
            }
        } else {
            final ImageView iv = (ImageView) v;
            DataSource<CloseableReference<CloseableImage>> dataSource = Fresco
                    .getImagePipeline().
                            fetchDecodedImage(request_builder
                                    .build(), Util.app_context);

            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                    iv.post(new Runnable() {
                        public void run() {
                            iv.setImageBitmap(bitmap);
                        }
                    });
                }


                @Override
                public void onFailureImpl(DataSource dataSource) {
                    // No cleanup required here.
                }
            }, CallerThreadExecutor.getInstance());
        }
    }


    public static void load2(View v, Object res) {
        ImageRequestBuilder request_builder = null;
        if (res instanceof Integer) {
            request_builder = ImageRequestBuilder
                    .newBuilderWithResourceId((Integer) res)
                    .setCacheChoice(ImageRequest.CacheChoice.DEFAULT)
                    .setResizeOptions(new ResizeOptions(Util.getPX(29), Util
                            .getPX(29)));
            //            request_builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse("res:///"+ res));

        } else if (res instanceof String) {
            request_builder = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse((String) res));
        }
        if (request_builder == null) {
            Logger.out("Failed to create image request builder.");
            return;
        }
        request_builder.setAutoRotateEnabled(true)
                       .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)//最低请求级别
                       .setProgressiveRenderingEnabled(true);
        if (!(v instanceof ImageView)) {
            Logger.out("Target view is not a ImageView.");
            return;
        }
        if (v instanceof SimpleDraweeView) {
            final SimpleDraweeView iv = (SimpleDraweeView) v;

            Uri tag = (Uri) iv.getTag();
            if (tag == null ||
                    (request_builder.getSourceUri()).compareTo(tag) != 0) {
                //            iv.setImageURI(uri);
                iv.setTag(request_builder.getSourceUri());
                PipelineDraweeControllerBuilder controller_builder = Fresco
                        .newDraweeControllerBuilder();
                controller_builder.setOldController(iv.getController())
                                  .setAutoPlayAnimations(true)
                                  .setImageRequest(request_builder.build());

                iv.setController(controller_builder.build());
            }
        } else {
            final ImageView iv = (ImageView) v;
            DataSource<CloseableReference<CloseableImage>> dataSource = Fresco
                    .getImagePipeline().
                            fetchDecodedImage(request_builder
                                    .build(), Util.app_context);

            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                    iv.post(new Runnable() {
                        public void run() {
                            iv.setImageBitmap(bitmap);
                        }
                    });
                }


                @Override
                public void onFailureImpl(DataSource dataSource) {
                    // No cleanup required here.
                }
            }, CallerThreadExecutor.getInstance());
        }
    }

    //图片高斯模糊处理


    public static void load(View v, Object res, final ImageLoadingListener l) {
        ImageRequestBuilder request_builder = null;
        if (res instanceof Integer) {
            request_builder = ImageRequestBuilder
                    .newBuilderWithResourceId((Integer) res);
        } else if (res instanceof String) {
            request_builder = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse((String) res));
        }
        if (request_builder == null) {
            Logger.out("Failed to create image request builder.");
            return;
        }
        request_builder.setAutoRotateEnabled(true)
                       .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                       .setProgressiveRenderingEnabled(true);//渐进式图片加载
        //        if (v instanceof AngelCompoundImageView) {
        //            AngelCompoundImageView aciv = (AngelCompoundImageView) v;
        //            load(aciv.getImageView(), res, l);
        //            return;
        //        }
        if (!(v instanceof ImageView)) {
            Logger.out("Target view is not a ImageView.");
            return;
        }
        if (v instanceof SimpleDraweeView) {
            final SimpleDraweeView iv = (SimpleDraweeView) v;
            //            iv.setImageURI(uri);
            PipelineDraweeControllerBuilder controller_builder = Fresco
                    .newDraweeControllerBuilder();
            controller_builder.setOldController(iv.getController())
                              .setAutoPlayAnimations(true)
                              .setImageRequest(request_builder.build());
            if (l != null) {
                ControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                        l.onImageLoaded(imageInfo);
                        if (imageInfo == null) {
                            return;
                        }
                        QualityInfo qualityInfo = imageInfo.getQualityInfo();
                        Logger.out(String
                                .format("Final image received! Size %d x %d Quality level %d, good enough: %s, full quality: %s", imageInfo
                                        .getWidth(), imageInfo
                                        .getHeight(), qualityInfo
                                        .getQuality(), qualityInfo
                                        .isOfGoodEnoughQuality(), qualityInfo
                                        .isOfFullQuality()));
                    }


                    @Override
                    public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                        l.onImageLoaded(imageInfo);
                    }


                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        l.onFailure();
                    }
                };
                controller_builder.setControllerListener(controllerListener);
            }
            iv.setController(controller_builder.build());
        } else {
            final ImageView iv = (ImageView) v;
            DataSource<CloseableReference<CloseableImage>> dataSource = Fresco
                    .getImagePipeline().
                            fetchDecodedImage(request_builder
                                    .build(), Util.app_context);

            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                    iv.post(new Runnable() {
                        public void run() {
                            if (l != null) {
                                l.onImageLoaded(null);
                            }
                            iv.setImageBitmap(bitmap);
                        }
                    });
                }


                @Override
                public void onFailureImpl(DataSource dataSource) {
                    // No cleanup required here.
                }
            }, CallerThreadExecutor.getInstance());
        }
    }


    /**
     * 将图片预加载到缓存中
     */
    public static void preload(Object res) {
        ImageRequestBuilder builder = null;
        if (res instanceof Integer) {
            builder = ImageRequestBuilder
                    .newBuilderWithResourceId((Integer) res);
        } else if (res instanceof String) {
            builder = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse((String) res));
        }
        if (builder == null) {
            Logger.out("Failed to create image request builder.");
            return;
        }
        builder.setAutoRotateEnabled(true)
               .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)

               .setProgressiveRenderingEnabled(false);

        Fresco.getImagePipeline()
              .prefetchToBitmapCache(builder.build(), Util.app_context);
    }


    public static boolean isInCache(Object res) {
        ImageRequestBuilder builder = null;
        if (res instanceof Integer) {
            builder = ImageRequestBuilder
                    .newBuilderWithResourceId((Integer) res);
        } else if (res instanceof String) {
            builder = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse((String) res));
        }
        if (builder == null) {
            Logger.out("Failed to create image request builder.");
            return false;
        }
        builder.setAutoRotateEnabled(true)
               .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)

               .setProgressiveRenderingEnabled(false);
        return Fresco.getImagePipeline().isInBitmapMemoryCache(builder.build());
    }
    //
    //    public static void load(SimpleDraweeView iv, String uri_text) {
    //        load(iv, uri_text, null);
    //    }
    //
    //
    //    public static void load(SimpleDraweeView iv, String uri_text, final ImageLoadingListener l) {
    ////        Logger.out(uri_text);
    //        if (uri_text != null) {
    //            Uri uri = Uri.parse(uri_text);
    //            iv.setImageURI(uri);
    //            if (l == null) {
    //                return;
    //            }
    //            ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
    //                @Override
    //                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
    //                    l.onImageLoaded();
    //                    if (imageInfo == null) {
    //                        return;
    //                    }
    //                    QualityInfo qualityInfo = imageInfo.getQualityInfo();
    //                    Logger.out(String.format("Final image received! Size %d x %d Quality level %d, good enough: %s, full quality: %s",
    //                            imageInfo.getWidth(),
    //                            imageInfo.getHeight(),
    //                            qualityInfo.getQuality(),
    //                            qualityInfo.isOfGoodEnoughQuality(),
    //                            qualityInfo.isOfFullQuality()));
    //                }
    //
    //                @Override
    //                public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
    //                    l.onImageLoaded();
    //                }
    //
    //                @Override
    //                public void onFailure(String id, Throwable throwable) {
    //                    l.onFailure();
    //                }
    //            };
    //            DraweeController controller = Fresco.newDraweeControllerBuilder()
    //                    .setControllerListener(controllerListener)
    //                    .setUri(uri).build();
    //            iv.setController(controller);
    //
    //        }
    //
    //    }
    //
    //    /**
    //     * 加载图片到指定的ImageView
    //     *
    //     * @param iv  要加载图片的ImageView
    //     * @param res 图片资源，Uri（包括网址，本地地址等）或资源ID
    //     */
    //    @Deprecated
    //    public static void display(ImageView iv, Object res) {
    //        display(iv, res, null);
    //    }
    //
    //    /**
    //     * 加载图片到指定的ImageView
    //     *
    //     * @param iv  要加载图片的ImageView
    //     * @param res 图片资源，Uri（包括网址，本地地址等）或资源ID
    //     * @param l   图片加载监听器
    //     */
    //    @Deprecated
    //    public static void display(ImageView iv, Object res, com.nostra13.universalimageloader.core.listener.ImageLoadingListener l) {
    //        if (res == null) {
    //            iv.setImageResource(R.color.trans);
    //        } else if (res instanceof String) {
    //            try {
    //                int i = Integer.parseInt(res.toString());
    //                iv.setImageResource(i);
    //            } catch (NumberFormatException e) {
    ////                if (l == null) {
    ////                    ImageLoader.getInstance().displayImage(res.toString(), iv);
    ////                } else {
    //                ImageLoader.getInstance().displayImage(res.toString(), iv, l);
    ////                }
    //
    //            }
    //        } else if (res instanceof Integer) {
    //            iv.setImageResource((Integer) res);
    //        }
    //    }


    /**
     * 生成缩略图
     *
     * @param uri 目标图片的URI
     * @return 生成的缩略图的文件信息
     */
    public static FileInfo getSmallPic(Context c, Uri uri) {
        FileInfo info = new FileInfo();
        Bitmap bitmap = null;
        String path = Util.getPath(c, uri);
        //        ExifInterface exif = null;
        //        try {
        //            exif = new ExifInterface(path);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        bitmap = getBitmap(path);
        String thumb_path = null;
        Bitmap thumb = bitmap;
        if (thumb == null) {
            return null;
        }
        FileOutputStream out = null;
        File file = new File(
                c.getDir("temp", Context.MODE_PRIVATE).getAbsolutePath() +
                        "/temp_" + System.currentTimeMillis() + ".jpg");
        thumb_path = file.getAbsolutePath();
        try {
            out = new FileOutputStream(file);
            thumb.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            thumb.recycle();
        }
        info.width = thumb.getWidth();
        info.height = thumb.getHeight();
        //        if (exif != null) {
        //            try {
        //                ExifInterface exif2 = new ExifInterface(thumb_path);
        //                exif2.setAttribute(ExifInterface.TAG_ORIENTATION, exif.getAttribute(ExifInterface.TAG_ORIENTATION));
        //                exif2.saveAttributes();
        //            } catch (IOException e) {
        //                e.printStackTrace();
        //            }
        //        }
        info.path = thumb_path;
        Logger.out(thumb_path + " file size:" + Util.getFileSize(file));
        return info;
    }


    static Bitmap getBitmap(String path) {

        //        ContentResolver resolver = c.getContentResolver();
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            //            in = resolver.openInputStream(uri);
            in = new FileInputStream(path);
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            //            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {

                scale++;
            }
            Logger.out("scale = " + scale + ", orig-width: " + o.outWidth +
                    ",orig - height: " + o.outHeight);

            Bitmap b = null;
            //            in = resolver.openInputStream(uri);
            in = new FileInputStream(path);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Logger.out("1th scale operation dimenions - width: " + width +
                        ",height: " + height);

                double y = Math
                        .sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap
                        .createScaledBitmap(b, (int) x, (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(path);
                int orientation = exif
                        .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                if (orientation != ExifInterface.ORIENTATION_UNDEFINED) {
                    b = rotateBitmap(b, orientation);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Logger.out("bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap
                    .createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap
                            .getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}
