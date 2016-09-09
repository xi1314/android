/**
 *
 */
package studio.archangel.toolkitv2.adapters;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import studio.archangel.toolkitv2.interfaces.Constructable;
import studio.archangel.toolkitv2.util.image.ImageProvider;


/**
 * 通用适配器的View缓存
 *
 * @author Michael
 */
public class CommonAdapterViewCache {
    private View v;
//    HashMap<String, Bitmap> cache;

    public CommonAdapterViewCache() {
    }

    public CommonAdapterViewCache(View base) {
        this();
        this.v = base;
    }

//    public CommonAdapterViewCache(View base, HashMap<String, Bitmap> c) {
//        this();
//        this.v = base;
//        cache = c;
//    }

    /**
     * 为布局中的View赋值
     *
     * @param view_class 要赋值的View的class
     * @param value      值
     * @param view_id    要赋值的View的id
     */
    public void setViewValue(Class<? extends View> view_class, final Object value, int view_id) {
        if (view_class.getName().equalsIgnoreCase("android.widget.TextView")) {
            TextView tv = (TextView) v.findViewById(view_id);
            tv.setText("" + value);
        } else if (view_class.getName().equalsIgnoreCase("org.sufficientlysecure.htmltextview.HtmlTextView")) {
            //HtmlTextView htv = (HtmlTextView) v.findViewById(view_id);
            //htv.setHtmlFromString("" + value, true);
        } else if (view_class.getName().equalsIgnoreCase("com.facebook.drawee.view.SimpleDraweeView")) {
            SimpleDraweeView iv = (SimpleDraweeView) v.findViewById(view_id);
            ImageProvider.load(iv, value);
        } else if (view_class.getName().equalsIgnoreCase("android.widget.ImageView")) {
            final ImageView iv = (ImageView) v.findViewById(view_id);
            ImageProvider.load(iv, value);
        } else if (view_class.getName().equalsIgnoreCase("android.widget.RatingBar")) {
            RatingBar rb = (RatingBar) v.findViewById(view_id);
            rb.setRating(Float.valueOf("" + value));
        } else if (Constructable.class.isAssignableFrom(view_class)) {
            Constructable con = (Constructable) v.findViewById(view_id);
            con.setValue((Message) value);
        }
    }

    /**
     * 获取布局中的目标View
     *
     * @param view_id 要获取的View的id
     * @return 目标View
     */
    public View getView(int view_id) {
        return v.findViewById(view_id);
    }
}