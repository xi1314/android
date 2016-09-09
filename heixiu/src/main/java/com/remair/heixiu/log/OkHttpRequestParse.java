package com.remair.heixiu.log;

import com.apkfuns.logutils.Parser;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/25 18:02
 * 修改人：LiuJun
 * 修改时间：16/8/25 18:02
 * 修改备注：
 */
public class OkHttpRequestParse implements Parser<Request> {

    private static final Charset UTF8 = Charset.forName("UTF-8");


    @Override
    public Class<Request> parseClassType() {
        return Request.class;
    }


    @Override
    public String parseString(Request request) {
        if (request != null) {
            StringBuilder builder = new StringBuilder();

            builder.append(String
                    .format("method = %s" + LINE_SEPARATOR, request.method()));
            builder.append(String
                    .format("url = %s" + LINE_SEPARATOR, request.url()
                                                                .toString()));
            builder.append(String
                    .format("header = %s" + LINE_SEPARATOR, new HeaderParse()
                            .parseString(request.headers())));

            try {
                RequestBody requestBody = request.body();
                if (requestBody != null) {
                    Charset charset = UTF8;
                    MediaType mediaType = requestBody.contentType();
                    if (mediaType == null) {
                        return builder.toString();
                    } else {
                        charset = mediaType.charset(UTF8);
                        builder.append(String
                                .format("requestBody's contentType  = %s" +
                                        LINE_SEPARATOR, mediaType.toString()));
                    }
                    if (isText(mediaType)) {
                        builder.append(String
                                .format("requestBody's content  = %s" +
                                        LINE_SEPARATOR, bodyToString(request)));
                    } else {
                        Buffer buffer = new Buffer();
                        requestBody.writeTo(buffer);
                        if (isPlaintext(buffer)) {
                            builder.append(String
                                    .format("requestBody's content  = %s" +
                                            LINE_SEPARATOR, buffer
                                            .readString(charset)));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return builder.toString();
        }
        return null;
    }


    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")) {
                return true;
            }
        }
        return false;
    }


    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }


    /**
     * Returns true if the body in question probably contains human readable
     * text. Uses a small sample
     * of code points to detect unicode control characters commonly used in
     * binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) &&
                        !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}
