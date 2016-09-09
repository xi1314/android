package com.remair.heixiu.net;

import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.ForbidMsgBean;

/**
 * 项目名称：Android
 * 类描述：服务器返回错误
 * 创建人：LiuJun
 * 创建时间：16/8/25 11:02
 * 修改人：LiuJun
 * 修改时间：16/8/25 11:02
 * 修改备注：
 */
public class ApiException extends RuntimeException {

    public static final String TRUE = "200";//操作成功
    public static final String FALSE = "406";//失败
    public static final String BUG = "500";//服务器异常
    public static final String MISS_PARAMS = "401";//缺失参数
    public static final String NONE_USER = "402";//缺失用户
    public static final String WRONG_REQUE = "501";//请求类型错误
    public static final String LOGIN_FAILURE = "600";//会话失效，请重新登陆！
    public static final String LOGIN_FORBIND = "601";//已经被封号
    private String mCode;
    private String mErrMsg;
    private ForbidMsgBean mForbidReasonBean;


    public ApiException(String code, String message) {
        super("code: " + code + "  msg: " + message);
        mCode = code;
        if (LOGIN_FORBIND.equals(mCode)) {
            forbidReason(message);
            return;
        }
        mErrMsg = message;
    }


    //特殊处理封号JSON
    private void forbidReason(String json) {
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            mForbidReasonBean = gson.fromJson(json, ForbidMsgBean.class);
            mErrMsg = mForbidReasonBean.txt;
        }
    }


    public ForbidMsgBean getForbidReasonBean() {
        return mForbidReasonBean;
    }


    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     */
    @Nullable
    private static String getApiExceptionMessage(String code, String msg) {
        String message;
        switch (code) {
            case MISS_PARAMS:
                message = HXApp.getInstance().getString(R.string.miss_params_code_401);
                break;
            case WRONG_REQUE:
                message = HXApp.getInstance().getString(R.string.wrong_reque_code_501);
                break;
            case LOGIN_FAILURE:
                message = HXApp.getInstance().getString(R.string.login_expire);
                break;
            default:
                message = msg;
        }
        return message;
    }


    public String getCode() {
        return mCode;
    }


    public String getErrMsg() {
        return getApiExceptionMessage(mCode, mErrMsg);
    }
}
