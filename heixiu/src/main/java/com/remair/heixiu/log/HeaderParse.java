package com.remair.heixiu.log;

import com.apkfuns.logutils.Parser;
import okhttp3.Headers;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/24 16:44
 * 修改人：LiuJun
 * 修改时间：16/8/24 16:44
 * 修改备注：
 */
public class HeaderParse implements Parser<Headers> {
    @Override
    public Class<Headers> parseClassType() {
        return Headers.class;
    }
    @Override
    public String parseString(Headers headers) {
        StringBuilder builder = new StringBuilder(
                headers.getClass().getSimpleName() + " [" + LINE_SEPARATOR);
        for (String name : headers.names()) {
            builder.append(String
                    .format("%s = %s" + LINE_SEPARATOR, name, headers
                            .get(name)));
        }
        return builder.append("]").toString();
    }
}
