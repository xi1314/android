package studio.archangel.toolkitv2.util.text;

import android.content.Context;
import android.text.format.Formatter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import studio.archangel.toolkitv2.util.Util;

/**
 * Created by Michael on 2014/12/1.
 */
public class AmountProvider {
    /**
     * 单位：千
     */
    public static final int readable_unit_thousand = 1;
    /**
     * 单位：万
     */
    public static final int readable_unit_ten_thousand = 2;

    /**
     * 获取用户友好的数量字符串
     *
     * @param amount 数量
     * @param unit   单位
     * @return
     */
    public static String getReadableAmount(int amount, int unit, boolean use_letter) {
        int div = 1;
        String unit_text = use_letter ? "k" : "千";
        if (unit == readable_unit_thousand) {
            div = 1000;
        } else if (unit == readable_unit_ten_thousand) {
            div = 10000;
            unit_text = use_letter ? "w" : "万";
        }
        if (amount < div) {
            return String.valueOf(amount);
        } else {
            double i = amount * 1.0 / div;
            return String.format("%.1f" + unit_text, i);
        }
    }

    public static String getReadableAmount(int amount, int unit) {
        return getReadableAmount(amount, unit, true);
    }

    public static String getCurrencyString(float amount) {
        return getCurrencyString(amount, 2);
    }

    public static String getCurrencyString(float amount, int digits) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(digits);
        nf.setMinimumFractionDigits(digits);
        return nf.format(amount);
    }

    public static String getCurrencyString(String amount) {
        return getCurrencyString(amount, 2);
    }

    public static String getCurrencyString(String amount, int digits) {
        try {
            float i = Float.parseFloat(amount);
            return getCurrencyString(i);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取用户友好的数量字符串，以万为单位
     *
     * @param amount 数量
     * @return
     */
    public static String getReadableAmount(int amount) {
        return getReadableAmount(amount, readable_unit_ten_thousand);
    }

    /**
     * 获取用户友好的文件大小
     *
     * @param size 文件大小
     * @return
     */
    public static String getReadableFileSize(long size) {
        return Util.getFileSize((float) size);
    }

    public static String getReadableFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 获取文件字节数组
     *
     * @param filePath 文件路径
     * @return
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据字节数组，生成文件
     *
     * @param bfile    字节数组
     * @param filePath 目标文件路径
     * @param fileName 文件名
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String getReadableDuration(int seconds) {
        if (seconds > 60 * 60) {
            int sec = seconds % 60;
            int min = seconds / 60;
            int hour = seconds / 60 / 60;
            return hour + ":" + min + "′" + sec + "″";
        } else if (seconds > 60) {
            int sec = seconds % 60;
            int min = seconds / 60;
            return min + "′" + sec + "″";
        } else {
            return seconds + "″";
        }
    }
}
