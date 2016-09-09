package studio.archangel.toolkitv2.activities;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.soundcloud.android.crop.Crop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import studio.archangel.toolkitv2.AngelApplication;
import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.image.FileInfo;
import studio.archangel.toolkitv2.util.image.ImageProvider;
import studio.archangel.toolkitv2.util.text.Notifier;

//import com.soundcloud.android.crop.Crop;

/**
 * Created by Michael on 2015/4/28.
 */
public class PickImageActivity extends AngelActivity {
    public static final int mode_take_a_photo = 20001;
    public static final int mode_select_from_gallery = 20002;
    public static final int mode_select_multiple_from_gallery = 20003;
    File photo;
    String cropped_file;
    CropMode mode;
    float crop_ratio;
    int color_main = -1;
    static CropMode[] crop_mode_array = new CropMode[] { CropMode.none,
            CropMode.square, CropMode.specific, CropMode.unspecific };
    private Context mContext;

    public enum CropMode {
        none(0), square(1), specific(2), unspecific(3);
        public int value;


        CropMode(int value) {
            this.value = value;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        Intent it = getIntent();
        Bundle extras = it.getExtras();
        String file_name = null;
        int pick_mode_code;
        if (extras == null) {
            Notifier.showNormalMsg(mContext, "数据错误");
            finish();
            return;
        } else {
            pick_mode_code = extras.getInt("mode", mode_take_a_photo);
            file_name = extras.getString("file_name", null);
            color_main = extras.getInt("color_main", getResources()
                    .getColor(R.color.blue));
        }
        try {
            mode = crop_mode_array[extras.getInt("crop", 1)];
        } catch (Exception e) {
            mode = CropMode.square;
        }
        if (mode == CropMode.specific) {
            crop_ratio = extras.getFloat("crop_ratio", -1);
            if (crop_ratio == -1) {
                Notifier.showNormalMsg(mContext, "数据错误");
                finish();
                return;
            }
        }
        if (pick_mode_code == mode_take_a_photo) {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                if (file_name != null) {
                    photo = new File(file_name);
                } else {
                    photo = File.createTempFile("upload_image_" +
                            System.currentTimeMillis(), ".jpg", getExternalCacheDir());
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                try {
                    startActivityForResult(intent, mode_take_a_photo);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Notifier.showLongMsg(mContext, "调用相机失败");
                    finish();
                }
            } catch (IOException e) {
                Notifier.showLongMsg(mContext, "Cannot create file. Please check storage system.");
                e.printStackTrace();
            }
        } else if (pick_mode_code == mode_select_from_gallery) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            try {
                startActivityForResult(intent, mode_select_from_gallery);
            } catch (Exception e) {
                e.printStackTrace();
                Notifier.showLongMsg(mContext, "调用图库失败");
                finish();
            }
        } else if (pick_mode_code == mode_select_multiple_from_gallery) {
            Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
            int max = extras.getInt("max", -1);
            if (max == -1) {
                Notifier.showNormalMsg(mContext, "数据错误");
                finish();
                return;
            }
            intent.putExtra("max", max);
            try {
                startActivityForResult(intent, mode_select_multiple_from_gallery);
            } catch (Exception e) {
                e.printStackTrace();
                Notifier.showLongMsg(mContext, "调用图库失败");
                finish();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    void cropPicture(String path) {
        File f = new File(
                getDir("temp", Context.MODE_PRIVATE).getAbsolutePath() +
                        "/temp_" + System.currentTimeMillis() + ".jpg");
        cropped_file = f.getAbsolutePath();
        if (cropped_file.startsWith("content://")) {
            cropped_file = getRealPathFromURI(mContext, Uri
                    .parse(cropped_file));
        }
        //        Crop output = new Crop(Uri.fromFile(new File(path))).output(Uri.fromFile(f));
        Crop output = Crop.of(Uri.fromFile(new File(path)), Uri.fromFile(f));
        if (mode == CropMode.specific) {
            output.withMaxSize((int) (crop_ratio * 1080), 1080).start(this);
        } else if (mode == CropMode.square) {
            //output.withMaxSize((int) (crop_ratio * 1080), 1080).start(this);
            output.asSquare().start(this);
        } else if (mode == CropMode.unspecific) {
            //output.withMaxSize((int) (crop_ratio * 1080), 1080).start(this);
            output.start(this);
        }
    }


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver()
                            .query(contentUri, proj, null, null, null);
            if (cursor == null) {
                return null;
            }
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    void handlePickedImage(Uri uri) {
        Logger.out(uri);
        FileInfo info = ImageProvider.getSmallPic(getApplication(), uri);
        if (info == null) {
            return;
        }
        Logger.out(info.path);
        if (mode != CropMode.none) {
            cropPicture(info.path);
        } else {
            Intent it = new Intent();
            it.putExtra("file", info.path);
            it.putExtra("width", info.width);
            it.putExtra("height", info.height);
            setResult(AngelApplication.result_ok, it);
            finish();
        }
    }


    ArrayList<FileInfo> handlePickedImages(JSONArray ja) {
        Notifier.showNormalMsg(mContext, "处理中...");
        //        dialog = new AngelLoadingDialog(getSelf(), R.color.blue, true);
        //        dialog.setMainColor(color_main);
        ArrayList<FileInfo> list = new ArrayList<>();
        for (int i = 0; i < ja.length(); i++) {
            //            Uri uri = Uri.fromFile(new File(ja.optString(i)));
            Uri uri = Uri.parse(ja.optString(i));
            Logger.out(uri);
            FileInfo info = ImageProvider.getSmallPic(getApplication(), uri);
            Logger.out(info != null ? info.path : null);
            list.add(info);
        }
        //        dialog.dismiss();
        return list;
    }


    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "false");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1080);
        intent.putExtra("aspectY", 1920);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 60);
        intent.putExtra("outputY", 60);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mode_select_from_gallery &&
                resultCode == AngelApplication.result_ok) {
            Uri uri = data.getData();
            handlePickedImage(uri);
        } else if (requestCode == mode_take_a_photo &&
                resultCode == AngelApplication.result_ok) {
            Uri uri = Uri.fromFile(photo);
            handlePickedImage(uri);
        } else if (requestCode == mode_select_multiple_from_gallery &&
                resultCode == AngelApplication.result_ok) {
            if (data == null) {
                finish();
                return;
            }
            Bundle extras = data.getExtras();
            if (extras == null) {
                finish();
                return;
            }
            String image_data = extras.getString("list", null);
            if (image_data == null) {
                finish();
                return;
            }
            try {
                JSONArray ja = new JSONArray(image_data);
                ArrayList<FileInfo> list = handlePickedImages(ja);
                ja = new JSONArray();
                for (FileInfo info : list) {
                    ja.put(info.toJson());
                }
                Intent it = new Intent();
                it.putExtra("list", ja.toString());
                setResult(AngelApplication.result_ok, it);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == Crop.REQUEST_CROP &&
                resultCode == AngelApplication.result_ok) {

            if (data == null) {
                Notifier.showNormalMsg(mContext, "裁剪图片时发生了错误");
                finish();
                return;
            }
            Bundle extras = data.getExtras();
            if (extras == null) {
                Notifier.showNormalMsg(mContext, "裁剪图片时发生了错误");
                finish();
                return;
            }
            Intent it = new Intent();

            int width = extras.getInt("width", -1);
            int height = extras.getInt("height", -1);

            it.putExtra("file", cropped_file);
            it.putExtra("width", width);
            it.putExtra("height", height);

            setResult(AngelApplication.result_ok, it);
            finish();
        } else {
            finish();
        }
    }


    @Override
    public int getDefaultStatusBarColor() {
        return getResources().getColor(R.color.trans);
    }
}
