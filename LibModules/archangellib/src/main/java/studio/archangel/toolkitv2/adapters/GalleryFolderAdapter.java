package studio.archangel.toolkitv2.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import studio.archangel.toolkitv2.AngelApplication;
import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.interfaces.OnCacheGeneratedListener;
import studio.archangel.toolkitv2.models.DeviceGalleryItem;
import studio.archangel.toolkitv2.util.Logger;

/**
 * Created by Michael on 2015/4/9.
 */
public class GalleryFolderAdapter extends CommonAdapter<DeviceGalleryItem> {

    public GalleryFolderAdapter(Context context, List<DeviceGalleryItem> list, int layout_id) {
        super(context, list, layout_id);
        init();
    }

    public GalleryFolderAdapter(Context context, List<DeviceGalleryItem> list) {
        this(context, list, R.layout.item_gallery_folder);
    }


    void init() {
        l = new OnCacheGeneratedListener<DeviceGalleryItem>() {

            @Override
            public void onCacheGenerated(CommonAdapterViewCache c, final DeviceGalleryItem t, int position) {
                c.getView(R.id.item_gallery_folder_layout).setVisibility(t.is_file ? View.GONE : View.VISIBLE);
                c.getView(R.id.item_gallery_folder_box).setVisibility(t.is_selected ? View.VISIBLE : View.GONE);
                c.getView(R.id.item_gallery_folder_mask).setVisibility((t.is_file && t.is_selected) ? View.VISIBLE : View.GONE);
//                Logger.out(t.is_selected + " " + c.getView(R.id.item_gallery_folder_mask).getVisibility());
                Object res;
                if (t.is_file) {
//                    c.setViewValue(ImageView.class, t.path, R.id.item_gallery_folder_image);
                    res = Uri.parse(t.path);
                } else {
                    res = t.getCover();
//                    c.setViewValue(ImageView.class, t.getCover(), R.id.item_gallery_folder_image);
//                    c.setViewValue(ImageView.class, t.items.size() != 0 ? t.items.get(0).path : R.color.white, R.id.item_gallery_folder_image);
                    c.setViewValue(TextView.class, t.name, R.id.item_gallery_folder_name);
                    c.setViewValue(TextView.class, t.des, R.id.item_gallery_folder_count);
                }
                SimpleDraweeView iv = (SimpleDraweeView) c.getView(R.id.item_gallery_folder_image);
                ImageRequest request = getRequest(iv, res);
                Logger.out(res);
                DraweeController controller = Fresco.newDraweeControllerBuilder()

//                PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setOldController(iv.getController())
                        .setImageRequest(request)
                        .build();
                iv.setController(controller);
            }
        };
    }

    ImageRequest getRequest(SimpleDraweeView iv, Object res) {
//        int width=0,height=0;
        int width = iv.getWidth(), height = iv.getHeight();
        if (width == 0) {
            width = AngelApplication.screen_width / 3;
        }
        if (height == 0) {
            height = AngelApplication.screen_width / 3;
        }
        Logger.out(width + "x" + height);
        ResizeOptions options = new ResizeOptions(width, height);
        ImageRequest request;
        if (res instanceof Integer) {
            request = ImageRequestBuilder.newBuilderWithResourceId((Integer) res)
                    .setResizeOptions(options)
                    .build();
        } else {
            request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(res.toString()))
                    .setResizeOptions(options)
                    .build();
        }
        return request;
    }
}
