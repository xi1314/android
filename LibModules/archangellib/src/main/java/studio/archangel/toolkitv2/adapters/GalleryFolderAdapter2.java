package studio.archangel.toolkitv2.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

import studio.archangel.toolkitv2.AngelApplication;
import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.models.DeviceGalleryItem;
import studio.archangel.toolkitv2.util.Logger;

/**
 * Created by Michael on 2015/4/9.
 */
public class GalleryFolderAdapter2 extends RecyclerView.Adapter<GalleryFolderAdapter2.ViewHolder> {
    public ArrayList<DeviceGalleryItem> data;
    OnItemClickListener listener;

    public GalleryFolderAdapter2(ArrayList<DeviceGalleryItem> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_folder, parent, false);
        ViewHolder vh = new ViewHolder(view, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceGalleryItem t = data.get(position);
        holder.v_layout.setVisibility(t.is_file ? View.GONE : View.VISIBLE);
        holder.box.setVisibility(t.is_selected ? View.VISIBLE : View.GONE);
        holder.mask.setVisibility((t.is_file && t.is_selected) ? View.VISIBLE : View.GONE);
        Object res;
        if (t.is_file) {
//                    c.setViewValue(ImageView.class, t.path, R.id.item_gallery_folder_image);
            res = Uri.parse(t.path);
        } else {
            res = t.getCover();
            holder.tv_name.setText(t.name);
            holder.tv_count.setText(t.des);
        }
        ImageRequest request = getRequest(holder.iv_image, res);
        Logger.out(res);
        DraweeController controller = Fresco.newDraweeControllerBuilder()

//                PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(holder.iv_image.getController())
                .setImageRequest(request)
                .build();
        holder.iv_image.setController(controller);
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

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_count;
        public SimpleDraweeView iv_image;
        public View v_layout;
        public ImageView box, mask;
        OnItemClickListener listener;

        public ViewHolder(View view, OnItemClickListener l) {
            super(view);
            listener = l;
            v_layout = view.findViewById(R.id.item_gallery_folder_layout);
            iv_image = (SimpleDraweeView) view.findViewById(R.id.item_gallery_folder_image);
            box = (ImageView) view.findViewById(R.id.item_gallery_folder_box);
            mask = (ImageView) view.findViewById(R.id.item_gallery_folder_mask);
            tv_name = (TextView) view.findViewById(R.id.item_gallery_folder_name);
            tv_count = (TextView) view.findViewById(R.id.item_gallery_folder_count);
            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(getLayoutPosition());
                    }
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}