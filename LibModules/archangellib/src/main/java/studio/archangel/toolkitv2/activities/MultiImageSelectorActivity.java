package studio.archangel.toolkitv2.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import studio.archangel.toolkitv2.AngelApplication;
import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.adapters.GalleryFolderAdapter2;
import studio.archangel.toolkitv2.models.DeviceGalleryItem;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelActionBar;
import studio.archangel.toolkitv2.views.AngelLoadingDialog;

/**
 * Created by Michael on 2015/4/28.
 */
public class MultiImageSelectorActivity extends AngelActivity {
    int max = 9;
    int color_main = -1;

    static final int msg_finish = 1001;
    static final int msg_error = 1002;
    ArrayList<DeviceGalleryItem> image_data, current, selected;
    GalleryFolderAdapter2 adapter;
    //    GalleryFolderAdapter adapter;
    //    SlideUpInAnimationAdapter adapter_wrapper;
    //    GridView grid;
    RecyclerView grid;
    Context mContext;
    boolean is_in_subfolder = false;
    String title = "选择图片";
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msg_finish: {
                    angelDialog
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    Notifier.showNormalMsg(mContext, "扫描本地图片完成");
                                    adapter.notifyDataSetChanged();
                                }
                            });
                    angelDialog.dismiss();
                    break;
                }
                case msg_error: {
                    angelDialog.dismiss();
                    Notifier.showLongMsg(mContext, "扫描本地图片失败...");
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_multi_image_selector);
        mContext=getApplication();
        grid = (RecyclerView) findViewById(R.id.act_multi_image_selector_grid);
        Util.setupActionBar(this, title);
        Intent it = getIntent();
        Bundle extras = it.getExtras();
        if (extras == null) {
            Notifier.showNormalMsg(mContext, "must pass parameters.");
            finish();
            return;
        }
        final AngelActionBar aab = getAngelActionBar();
        aab.setRightText("完成");
        aab.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected.isEmpty()) {
                    Notifier.showNormalMsg(mContext, "至少选择一张哦~");
                    return;
                }
                JSONArray ja = new JSONArray();
                for (DeviceGalleryItem item : selected) {
                    ja.put(item.path);
                }
                Intent data = new Intent();
                data.putExtra("list", ja.toString());
                setResult(AngelApplication.result_ok, data);
                finish();
            }
        });
        max = extras.getInt("max", max);
        color_main = extras
                .getInt("color_main", getResources().getColor(R.color.blue));
        image_data = new ArrayList<>();
        current = new ArrayList<>();
        selected = new ArrayList<>();
        adapter = new GalleryFolderAdapter2(current);
        //        adapter_wrapper = new SlideUpInAnimationAdapter(adapter);
        //        adapter_wrapper.setAbsListView(grid);
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        grid.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int space = Util.getPX(3);
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;

                // Add top margin only for the first item to avoid double space between items
                //                if (parent.getChildLayoutPosition(view) == 0)
                outRect.top = space;
            }
        });
        grid.setLayoutManager(manager);
        grid.setAdapter(adapter);
        adapter.setListener(new GalleryFolderAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                DeviceGalleryItem t = current.get(position);
                if (t.is_file) {
                    if (selected.contains(t)) {
                        t.is_selected = false;
                        selected.remove(t);
                    } else {
                        if (selected.size() >= max) {
                            Notifier.showNormalMsg(mContext,
                                    "一次最多只能选择" + max + "张哦~");
                            return;
                        }
                        t.is_selected = true;
                        selected.add(t);
                    }
                    adapter.notifyItemChanged(position);
                    //                    Util.refreshItemInCollectionView(grid, t);
                    //                    adapter.notifyDataSetChanged();
                    //                    Intent it = new Intent(getSelf(), GalleryActivity.class);
                    //                    ArrayList<String> urls = new ArrayList<>();
                    //                    urls.add(folder.path);
                    //                    it.putExtra("urls", urls);
                    //                    it.putExtra("scale_type", ImageView.ScaleType.FIT_CENTER);
                    //                    startActivity(it);
                } else {
                    current.clear();
                    current.addAll(t.items);
                    //                    adapter_wrapper.reset();
                    adapter.notifyDataSetChanged();
                    is_in_subfolder = true;
                    getAngelActionBar().setTitleText(t.name);
                }
            }
        });
        //        grid.setAdapter(adapter_wrapper);
        //        adapter.setOnItemCheckedListener(new GalleryFolderAdapter.onItemCheckedListener() {
        //            @Override
        //            public void onChecked(DeviceGalleryItem t, boolean new_status) {
        //                if (!t.is_file) {
        //                    return ;
        //                }
        //                if (selected.contains(t)) {
        //                    t.is_selected = false;
        //                    selected.remove(t);
        //                } else {
        //                    if (selected.size() >= max) {
        //                        Notifier.showNormalMsg(mContext, "一次最多只能选择" + max + "张哦~");
        //                        return;
        //                    }
        //                    t.is_selected = true;
        //                    selected.add(t);
        //                    adapter.notifyDataSetChanged();
        //                }
        //            }
        //        });
        //        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //                DeviceGalleryItem t = current.get(position);
        //                if (t.is_file) {
        //                    if (selected.contains(t)) {
        //                        t.is_selected = false;
        //                        selected.remove(t);
        //                    } else {
        //                        if (selected.size() >= max) {
        //                            Notifier.showNormalMsg(mContext, "一次最多只能选择" + max + "张哦~");
        //                            return;
        //                        }
        //                        t.is_selected = true;
        //                        selected.add(t);
        //                    }
        //                    Util.refreshItemInCollectionView(grid, t);
        ////                    adapter.notifyDataSetChanged();
        ////                    Intent it = new Intent(mContext, GalleryActivity.class);
        ////                    ArrayList<String> urls = new ArrayList<>();
        ////                    urls.add(folder.path);
        ////                    it.putExtra("urls", urls);
        ////                    it.putExtra("scale_type", ImageView.ScaleType.FIT_CENTER);
        ////                    startActivity(it);
        //                } else {
        //                    current.clear();
        //                    current.addAll(t.items);
        ////                    adapter_wrapper.reset();
        //                    adapter.notifyDataSetChanged();
        //                    is_in_subfolder = true;
        //                    getAngelActionBar().setTitleText(t.name);
        //
        //                }
        //            }
        //        });
        //        loadLocalUrls();
        getImages();
    }


    @Override
    public void onBackPressed() {
        if (is_in_subfolder) {
            is_in_subfolder = false;
            for (DeviceGalleryItem item : current) {
                item.is_selected = false;
            }
            selected.clear();
            current.clear();
            current.addAll(image_data);
            //            adapter_wrapper.reset();
            adapter.notifyDataSetChanged();
            getAngelActionBar().setTitleText(title);
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private void loadLocalUrls() {
        Notifier.showLongMsg(mContext, "正在扫描本地图片...请稍候");
        angelDialog = new AngelLoadingDialog(getSelf(), R.color.blue, true);
        angelDialog.setMainColor(color_main);
        Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.Media._ID };
        Cursor cursor = null;
        try {
            cursor = getContentResolver()
                    .query(externalContentUri, projection, null, null, null);

            int columnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media._ID);

            String imageId;
            Uri imageUri;
            while (cursor.moveToNext()) {
                imageId = cursor.getString(columnIndex);
                imageUri = Uri
                        .withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId);

                image_data
                        .add(new DeviceGalleryItem(true, imageUri.toString()));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String[] projection = { MediaStore.Images.Media._ID };
                Cursor cursor = null;
                try {
                    cursor = getContentResolver()
                            .query(externalContentUri, projection, null, null, null);

                    int columnIndex = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media._ID);

                    String imageId;
                    Uri imageUri;
                    while (cursor.moveToNext()) {
                        imageId = cursor.getString(columnIndex);
                        imageUri = Uri
                                .withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId);

                        image_data.add(new DeviceGalleryItem(true, imageUri
                                .toString()));
                    }
                    current.addAll(image_data);
                    //                    Message msg = new Message();
                    //                    msg.what = msg_finish;
                    //                    msg.obj = map;
                    //                    handler.sendMessage(msg);
                    handler.sendEmptyMessage(msg_finish);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }).start();
    }


    void getImages() {
        Notifier.showLongMsg(mContext, "正在扫描本地图片...请稍候");
        angelDialog = new AngelLoadingDialog(getSelf(), R.color.blue, true);
        angelDialog.setMainColor(color_main);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    HashMap<String, DeviceGalleryItem> map = new HashMap<>();
                    Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver mContentResolver = getSelf()
                            .getContentResolver();

                    //只查询jpeg和png的图片
                    Cursor mCursor = mContentResolver.query(mImageUri, null,
                            MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                    MediaStore.Images.Media.MIME_TYPE +
                                    "=?", new String[] {
                                    "image/jpeg" }, MediaStore.Images.Media.DATE_MODIFIED);
                    //                            new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                    if (mCursor == null) {
                        return;
                    }

                    while (mCursor.moveToNext()) {
                        //获取图片的路径
                        try {
                            String path = mCursor.getString(mCursor
                                    .getColumnIndex(MediaStore.Images.Media.DATA));
                            if (path.startsWith("content://")) {
                                path = PickImageActivity
                                        .getRealPathFromURI(mContext, Uri
                                                .parse(path));
                            } else {
                                path = "file://" + path;
                            }
                            Logger.out(path);
                            //获取该图片的父路径名
                            if (path == null) {
                                continue;
                            }
                            File file = new File(path).getParentFile();
                            String parentName = file.getName();

                            //根据父路径名将图片放入到mGruopMap中
                            DeviceGalleryItem f = new DeviceGalleryItem(true, path);

                            try {
                                f.date = (new File(path.replace("file://", "")))
                                        .lastModified();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!map.containsKey(parentName)) {
                                DeviceGalleryItem folder = new DeviceGalleryItem(false, "");
                                folder.name = parentName;
                                folder.date = file.lastModified();
                                folder.items.add(f);
                                map.put(parentName, folder);
                            } else {
                                map.get(parentName).items.add(f);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //通知Handler扫描图片完成
                    image_data.addAll(map.values());
                    Collections
                            .sort(image_data, new Comparator<DeviceGalleryItem>() {
                                @Override
                                public int compare(DeviceGalleryItem lhs, DeviceGalleryItem rhs) {
                                    if (lhs.items.size() > rhs.items.size()) {
                                        return -1;
                                    } else if (lhs.items.size() <
                                            rhs.items.size()) {
                                        return 1;
                                    } else {
                                        return 0;
                                    }
                                }
                            });
                    for (DeviceGalleryItem item : image_data) {
                        Collections
                                .sort(item.items, new Comparator<DeviceGalleryItem>() {
                                    @Override
                                    public int compare(DeviceGalleryItem lhs, DeviceGalleryItem rhs) {
                                        if (lhs.date > rhs.date) {
                                            return -1;
                                        } else if (lhs.date < rhs.date) {
                                            return 1;
                                        } else {
                                            return 0;
                                        }
                                    }
                                });
                        item.path = item.items.get(0).path;
                        item.des = item.items.size() + "张";
                    }
                    current.addAll(image_data);
                    //                    Message msg = new Message();
                    //                    msg.what = msg_finish;
                    //                    msg.obj = map;
                    //                    handler.sendMessage(msg);
                    handler.sendEmptyMessage(msg_finish);
                    mCursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(msg_error);
                }
            }
        }).start();
    }


    @Override
    public int getDefaultStatusBarColor() {
        return getResources().getColor(R.color.trans);
    }
}
