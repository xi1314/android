package studio.archangel.toolkitv2.interfaces;

//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import org.json.JSONException;
//import org.json.JSONObject;
//import studio.archangel.toolkitv2.util.Logger;

/**
 * 网络请求回调接口
 */
public abstract class NetCallBack {
}
//public abstract class NetCallBack extends RequestCallBack<String> {
//    /**
//     * json数据格式错误
//     */
//
//    @Override
//    public void onSuccess(ResponseInfo<String> responseInfo, JSONObject request_param) {
//        JSONObject jo;
//        try {
//            printRequestParameters(request_param);
//            jo = new JSONObject(responseInfo.result);
//            int status = jo.optInt(getReturnCodeName());
//            String json = jo.optString(getReturnDataName());
//            String readable_msg = jo.optString(getReturnMsgName());
////            Logger.out(status + " " + json);
//            onSuccess(status, json, readable_msg, request_param, this);
//        } catch (JSONException e) {
//            Logger.out(responseInfo.result);
//            e.printStackTrace();
//            onFailure(new HttpException(), "数据格式错误", request_param);
//        }
//    }
//
//    protected void printRequestParameters(JSONObject request_param) {
//        try {
//            Logger.out(request_param.toString(4));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public abstract String getReturnCodeName();
//
//    public abstract String getReturnDataName();
//
//    public abstract String getReturnMsgName();
//
//    @Override
//    public void onFailure(HttpException error, String msg, JSONObject request_param) {
//        printRequestParameters(request_param);
//        Logger.out("error:" + error + "\nmsg:" + msg);
//    }
//
//    public abstract void onSuccess(int status, String json, String readable_msg, JSONObject request_param, NetCallBack netCallBack);
//
//}
