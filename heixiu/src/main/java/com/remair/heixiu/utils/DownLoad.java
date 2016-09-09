package com.remair.heixiu.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

/**
 * Created by wsk on 16/6/10.
 */
public class DownLoad {

    /**
     * @param urlStr http://f.txt
     * @param path /mnt/sdcard/f.txt
     */
    public static void to(final String urlStr, final String path, final String name, final JSONObject jsonObject) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(path);
                if (f.exists()) {
                    f.delete();
                }
                if (!f.exists()) {
                    f.mkdirs();
                }

                File picFile = new File(f, name);
                InputStream is = null;
                OutputStream os = null;
                try {
                    // 构造URL
                    URL url = new URL(urlStr);
                    // 打开连接
                    URLConnection con = url.openConnection();
                    // 输入流
                    is = con.getInputStream();
                    // 1K的数据缓冲
                    byte[] bs = new byte[1024];
                    // 读取到的数据长度
                    int len = -1;
                    // 输出的文件流
                    os = new FileOutputStream(picFile);
                    // 开始读取
                    while ((len = is.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                    SharedPreferenceUtil
                            .putString(jsonObject.getString("gift_id"),
                                    path + "/" + name);
                    // 完毕，关闭所有链接
                    os.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
