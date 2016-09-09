package studio.archangel.toolkitv2.models;

import java.util.ArrayList;

import studio.archangel.toolkitv2.R;

/**
 * Created by Administrator on 2015/10/24.
 */
public class DeviceGalleryItem {
    public ArrayList<DeviceGalleryItem> items;
    public String name;
    public long date;
    public boolean is_file = false, is_selected = false;
    public String path = null;
    public String des;

    public DeviceGalleryItem() {
        this.items = new ArrayList<>();
    }

    public DeviceGalleryItem(boolean b, String p) {
        is_file = b;
        if (b) {
            path = p;
        } else {
            this.items = new ArrayList<>();
        }
    }

    public Object getCover() {
        return path == null ? R.color.white : path;
    }

}
