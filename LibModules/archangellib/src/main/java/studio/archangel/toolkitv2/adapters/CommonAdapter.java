/**
 *
 */
package studio.archangel.toolkitv2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import studio.archangel.toolkitv2.interfaces.OnCacheGeneratedListener;

/**
 * 通用适配器
 *
 * @param <T> 要显示的实体类型
 * @author Michael
 */
public class CommonAdapter<T> extends ArrayAdapter<T> {
    /**
     * 缓存监听器
     *
     * @see studio.archangel.toolkitv2.interfaces.OnCacheGeneratedListener
     */
    protected OnCacheGeneratedListener<T> l;
    /**
     * 上下文
     */
    protected Context context;
    /**
     * 与此适配器绑定的布局的资源ID
     */
    int layout_id;
//    HashMap<String, Bitmap> bitmap_cache;

    /**
     * 初始化
     *
     * @param context     上下文
     * @param list        实体数据列表
     * @param item_layout 布局的资源ID
     */
    public CommonAdapter(Context context, List<T> list, int item_layout) {
        super(context, 0, list);
        this.context = context;
        layout_id = item_layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        final Activity activity = (Activity) getContext();
        final T item = getItem(position);
        View rowView = convertView;
//        Logger.out("rowView==null:" + rowView == null);
        CommonAdapterViewCache cache;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            LayoutInflater inflater = activity.getLayoutInflater();

            rowView = inflater.inflate(layout_id, null);
            cache = new CommonAdapterViewCache(rowView);
            rowView.setTag(cache);
        } else {
            cache = (CommonAdapterViewCache) rowView.getTag();
        }
        if (l != null) {
            l.onCacheGenerated(cache, item, position);
        }
        return rowView;
    }

}
