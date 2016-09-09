package com.remair.heixiu.log;

import com.apkfuns.logutils.Parser;
import java.io.IOException;
import okhttp3.Response;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/24 16:43
 * 修改人：LiuJun
 * 修改时间：16/8/24 16:43
 * 修改备注：
 */
public class OkHttpResponseParse implements Parser<Response> {

    @Override
    public Class<Response> parseClassType() {
        return Response.class;
    }


    @Override
    public String parseString(Response response) {
        if (response != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(String
                    .format("code = %s" + LINE_SEPARATOR, response.code()));
            builder.append(String
                    .format("isSuccessful = %s" + LINE_SEPARATOR, response
                            .isSuccessful()));
            builder.append(String
                    .format("url = %s" + LINE_SEPARATOR, response.request()
                                                                 .url()));
            builder.append(String
                    .format("message = %s" + LINE_SEPARATOR, response
                            .message()));
            builder.append(String
                    .format("protocol = %s" + LINE_SEPARATOR, response
                            .protocol()));
            builder.append(String
                    .format("header = %s" + LINE_SEPARATOR, new HeaderParse()
                            .parseString(response.headers())));
            try {
                builder.append(String
                        .format("body = %s" + LINE_SEPARATOR, response.body()
                                                                      .string()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }
        return null;
    }
}
