package studio.archangel.toolkitv2.util;

//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;


//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

import java.util.ArrayList;
import studio.archangel.toolkitv2.util.text.DateProvider;

/**
 * 测试数据提供者
 * Created by Michael on 2014/9/16.
 */
public class Tester {
    /**
     * 一段纯文本，用来从其中获得字符串
     */
    static String raw_string = "伟大的刺客，刺客组织历史上的传奇人物，年轻时桀骜不驯，高傲自负，在试图从圣殿骑士团大团长手中夺取神器时，因一时冲动导致任务失败，并且引发了圣殿骑士攻击刺客组织的总部——“Masyaf”。因此被降职为新手，并被赋予任务以保住性命以及赎回自己的阶级。阿泰尔直到80多岁时，才重返马西亚夫夺回属于自己的权力，一直掌管刺客组织和研究金苹果，92岁时于马西亚夫的密室图书馆中孤身一人守护金苹果，安详地死在椅子上。文艺复兴时期的佛罗伦萨贵族，原本只是名纨绔子弟，风流倜傥，因为目睹了背叛和父兄被杀，而在年轻时被复仇心驱使。在叔叔和其他正义之士的教导下学会了忍耐和宽容，并开始展现自己的各种天分，继承了父亲的刺客衣钵，到38岁时正式成为刺客大师，变得正义而公正。50岁时，艾吉奥找到了马西亚夫的图书馆，来到阿泰尔端坐的遗骨面前，褪下袖剑，告别自己的刺客生涯，和妻子索菲娅回到佛罗伦萨，65岁时在圣母百花大教堂门前接触了一名年轻人后死去，享年65岁。";
    static String raw_string_en = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm ";
    /**
     * 为了避免重复计算长度，把测试字符串库的长度存起来
     */
    static int raw_string_length = raw_string.length();
    static int raw_string_en_length = raw_string_en.length();
    static final int default_image_width = 200;
    static final int default_image_height = 200;
    /**
     * 获得测试图片的网页地址——搜狗壁纸...
     */
    static String url = "http://www.poocg.com/works/tjwork/page/";
    //    static String url = "http://bizhi.sogou.com/newly/jingpin";
    static ArrayList<String> url_list = new ArrayList<String>();

    /**
     * 获得随机字符串
     *
     * @param length_min 最小长度
     * @param length_max 最大长度
     * @return 随机字符串，长度取值区间为[{@code length_min},{@code length_max}]
     */
    public static String getString(int length_min, int length_max) {
        StringBuilder sb = new StringBuilder();
        int length = (int) (Math.random() * (length_max + 1 - length_min) + length_min);
        for (int i = 0; i < length; i++) {
            int x = (int) (Math.random() * raw_string_length);
            sb = sb.append(raw_string.charAt(x));
        }
        return sb.toString();
    }

    /**
     * 获得随机英文字符串
     *
     * @param length_min 最小长度
     * @param length_max 最大长度
     * @param cap        大写
     * @return 随机字符串，长度取值区间为[{@code length_min},{@code length_max}]
     */
    public static String getStringEn(int length_min, int length_max, boolean cap) {
        StringBuilder sb = new StringBuilder();
        int length = (int) (Math.random() * (length_max + 1 - length_min) + length_min);
        for (int i = 0; i < length; i++) {
            int x = (int) (Math.random() * raw_string_en_length);
            sb = sb.append(raw_string_en.charAt(x));
        }
        if (cap) {
            return sb.toString().toUpperCase();
        } else {
            return sb.toString();
        }
    }

    /**
     * 获得随机日期
     *
     * @param format 日期格式
     * @param start  开始日期
     * @param end    结束日期
     * @return 随机日期，取值区间为[{@code length_min},{@code length_max}]
     */
    public static String getDate(String format, String start, String end) {
        long l1 = DateProvider.getDate(format, start);
        long l2 = DateProvider.getDate(format, end);
        long l = (long) (Math.min(l1, l2) + Math.random() * (Math.abs(l2 - l1) + 1));
        return DateProvider.getDate(format, l);
    }

    /**
     * 获得随机整数
     *
     * @param min
     * @param max
     * @return 随机整数，取值区间为[{@code min},{@code max}]
     */
    public static int getInt(int min, int max) {
        return (int) (min + Math.random() * (max + 1 - min));
    }

    /**
     * 获得随机长整数
     *
     * @param min
     * @param max
     * @return 随机整数，取值区间为[{@code min},{@code max}]
     */
    public static int getLong(long min, long max) {
        return (int) (min + Math.random() * (max + 1 - min));
    }

    /**
     * 获得随机浮点数
     *
     * @param min
     * @param max
     * @return 随机浮点数，取值区间为[{@code min},{@code max}]
     */
    public static float getFloat(float min, float max) {
        return (float) (min + Math.random() * (max + 1 - min));
    }

    /**
     * 获得随机布尔值
     *
     * @return 随机true或false
     */
    public static boolean getBoolean() {
        return Math.random() < 0.5f;
    }

    //
//    /**
//     * 获得随机图片地址
//     *
//     * @param min 最小值
//     * @param max 最大值
//     * @return 一堆随机图片的地址
//     */
//    public static ArrayList<String> getImages(int min, int max) {
//        ArrayList<String> urls = new ArrayList<String>();
//        int count = getInt(min, max);
//        for (int i = 0; i < count; i++) {
//            urls.add(getImage());
//        }
//        return urls;
//    }
//    public static void loadImages() {
//        if (url_list.isEmpty()) {
//
//
//            OkHttpClient client = new OkHttpClient();
//            String s = url + Tester.getInt(1, 5);
//
//            RequestBody body = new FormEncodingBuilder()
//                    .add("key", "{}")
//                    .build();
//            Request request = new Request.Builder()
//                    .url(s)
//                    .post(body)
//                    .build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    String html = null;
//                    try {
//                        html = new String(response.toString().getBytes(), "utf-8");
////                            html = new String(responseInfo.result.toString().getBytes(), "gb2312");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    Logger.out("loadImages:" + html);
////                        int start = html.indexOf("<div class=\"TheWorksList TheWorksList-innerpage\">");
////                        html = html.substring(start, html.length());
////                        html = html.substring(0, html.indexOf("<div class=\"jp_more\""));
//                    Document doc = Jsoup.parse(html);
//                    Elements es = doc.select("img[src$=.jpg]");
//                    for (Element e : es) {
//                        url_list.add(e.attr("src"));
//                    }
//                }
//            });
//            try {
//                HttpUtils http = new HttpUtils();
//                http.send(HttpRequest.HttpMethod.GET, url + Tester.getInt(1, 5), null, new RequestCallBack<Object>() {
//                    @Override
//                    public void onSuccess(ResponseInfo<Object> responseInfo, JSONObject data) {
//                        String html = null;
//                        try {
//                            html = new String(responseInfo.result.toString().getBytes(), "utf-8");
////                            html = new String(responseInfo.result.toString().getBytes(), "gb2312");
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                        Logger.out("loadImages:" + html);
////                        int start = html.indexOf("<div class=\"TheWorksList TheWorksList-innerpage\">");
////                        html = html.substring(start, html.length());
////                        html = html.substring(0, html.indexOf("<div class=\"jp_more\""));
//                        Document doc = Jsoup.parse(html);
//                        Elements es = doc.select("img[src$=.jpg]");
//                        for (Element e : es) {
//                            url_list.add(e.attr("src"));
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg, JSONObject data) {
//
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        String path = "";

//        try {
//            path = url_list.GET((int) (Math.random() * url_list.size()));
//        } catch (Exception e) {
//        }

//    }

    /**
     * 获得随机图片地址
     *
     * @param width  宽度
     * @param height 高度
     * @return 指定尺寸的随机图片的地址
     */
    public static String getImage(int width, int height) {
        return "http://lorempixel.com/" + width + "/" + height + "/";
    }

    /**
     * @return 随机图片的地址。尺寸是默认尺寸
     */
    public static String getImage() {
        return url_list.get(Tester.getInt(0, url_list.size() - 1));
//        return getImage(default_image_width, default_image_height);
    }
//    /**
//     * 获得随机图片地址
//     *
//     * @return {@link studio.archangel.toolkitv2.util.Tester#url} 中的图片
//     */
//    public static String getImage() {
//        if (url_list.isEmpty()) {
//            try {
//                HttpUtils http = new HttpUtils();
//                http.send(HttpRequest.HttpMethod.GET, url, null, new RequestCallBack<Object>() {
//                    @Override
//                    public void onSuccess(ResponseInfo<Object> responseInfo, JSONObject data) {
//                        String html = null;
//                        try {
//                            html = new String(responseInfo.result.toString().getBytes(), "gb2312");
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                        int start = html.indexOf("<div class=\"jp-item\">");
//                        html = html.substring(start, html.length());
//                        html = html.substring(0, html.indexOf("<div class=\"jp_more\""));
//                        Document doc = Jsoup.parse(html);
//                        Elements es = doc.select("img[src]");
//                        for (Element e : es) {
//                            url_list.add(e.attr("src"));
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg, JSONObject data) {
//
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        String path = "";
//
//        try {
//            path = url_list.GET((int) (Math.random() * url_list.size()));
//        } catch (Exception e) {
//        }
//
//        return path;
//    }
}
