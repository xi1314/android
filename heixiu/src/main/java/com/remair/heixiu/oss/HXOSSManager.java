package com.remair.heixiu.oss;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.HeadObjectRequest;
import com.alibaba.sdk.android.oss.model.HeadObjectResult;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.remair.heixiu.HXApp;
import java.io.IOException;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/10 18:58
 * 修改人：LiuJun
 * 修改时间：16/8/10 18:58
 * 修改备注：
 */
public class HXOSSManager {

    private volatile static OSS sOSS;


    /**
     * 获取URL使用这个OSS对象
     */
    public static OSS getOSS() {
        if (sOSS == null) {
            synchronized (HXOSSManager.class) {
                if (sOSS == null) {
                    ClientConfiguration conf = new ClientConfiguration();
                    conf.setConnectionTimeout(15 * 1000);//连接超时，默认15秒
                    conf.setSocketTimeout(15 * 1000);// socket超时，默认15秒
                    conf.setMaxConcurrentRequest(10);// 最大并发请求书，默认5个
                    sOSS = new OSSClient(HXApp
                            .getInstance(), "oss-cn-beijing.aliyuncs.com", HXOSSFCProvider
                            .getInstance(), conf);
                }
            }
        }
        return sOSS;
    }


    public static String getPublicURL(String bucketName, String objectKey) {
        return getOSS().presignPublicObjectURL(bucketName, objectKey);
    }


    public static String getConstrainedURL(String bucketName, String objectKey, long expiredTimeInSeconds) throws ClientException {
        return getOSS()
                .presignConstrainedObjectURL(bucketName, objectKey, expiredTimeInSeconds);
    }


    public static ObjectMetadata setMD5Metadata(@NonNull PutObjectRequest put, String contentMD5, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        if (!TextUtils.isEmpty(contentType)) {
            metadata.setContentType(contentType);
        }
        if (!TextUtils.isEmpty(contentMD5)) {
            metadata.setContentMD5(contentMD5);
        }
        put.setMetadata(metadata);
        return metadata;
    }


    public static PutObjectResult syncUpload(String bucketName, String objectKey, byte[] bytes, String contentType) throws ClientException, ServiceException {
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, bytes);
        setMD5Metadata(put, BinaryUtil.calculateBase64Md5(bytes), contentType);
        return getOSS().putObject(put);
    }


    public static PutObjectResult syncUpload(String bucketName, String objectKey, String filePath) throws ClientException, ServiceException, IOException {
        return syncUpload(bucketName, objectKey, filePath, null);
    }


    public static PutObjectResult syncUpload(String bucketName, String objectKey, String filePath, String contentType) throws ClientException, ServiceException, IOException {
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, filePath);
        setMD5Metadata(put, BinaryUtil
                .calculateBase64Md5(filePath), contentType);
        return getOSS().putObject(put);
    }


    public static OSSAsyncTask<PutObjectResult> asyncUpload(String bucketName, String objectKey, String filePath, OSSProgressCallback<PutObjectRequest> progressCallback, OSSCompletedCallback<PutObjectRequest, PutObjectResult> completedCallback) throws IOException {
        return asyncUpload(bucketName, objectKey, filePath, null, progressCallback, completedCallback);
    }


    public static OSSAsyncTask<PutObjectResult> asyncUpload(String imagePublic, String bucketName, String objectKey, String filePath, OSSCompletedCallback<PutObjectRequest, PutObjectResult> completedCallback) throws IOException {
        return asyncUpload(bucketName, objectKey, filePath, null, null, completedCallback);
    }


    public static OSSAsyncTask<PutObjectResult> asyncUpload(String bucketName, String objectKey, String filePath, String contentType, OSSProgressCallback<PutObjectRequest> progressCallback, OSSCompletedCallback<PutObjectRequest, PutObjectResult> completedCallback) throws IOException {
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, filePath);
        setMD5Metadata(put, BinaryUtil
                .calculateBase64Md5(filePath), contentType);
        if (progressCallback != null) {
            put.setProgressCallback(progressCallback);
        }
        return getOSS().asyncPutObject(put, completedCallback);
    }


    /**
     * 获取文件的元信息
     */
    public static OSSAsyncTask getHeadObject(String bucketName, String objectKey, OSSCompletedCallback<HeadObjectRequest, HeadObjectResult> callback) {
        HeadObjectRequest head = new HeadObjectRequest(bucketName, objectKey);
        return getOSS().asyncHeadObject(head, callback);
    }
}
