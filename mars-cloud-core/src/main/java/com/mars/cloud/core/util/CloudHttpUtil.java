package com.mars.cloud.core.util;

import com.mars.core.constant.MarsCloudConstant;
import com.mars.core.util.SerializableUtil;
import okhttp3.*;

import java.util.Map;

/**
 * http请求工具类
 */
public class CloudHttpUtil {

    /**
     * 发起请求，以序列化方式传递数据
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String request(String url, Object params,String requestType) throws Exception {

        /* 将参数序列化成byte[] */
        byte[] param = SerializableUtil.serialization(params);

        OkHttpClient okHttpClient = new OkHttpClient();

        /* 发起post请求 将数据传递过去 */
        MediaType formData = MediaType.parse("multipart/form-data");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),param);
        MultipartBody body = new MultipartBody.Builder()
                .setType(formData)
                .addFormDataPart(MarsCloudConstant.PARAM,"params",fileBody)
                .addFormDataPart(MarsCloudConstant.REQUEST_TYPE,requestType)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();

        return okCall(okHttpClient,request);
    }

    /**
     * 发起get请求
     * @param strUrl 链接
     * @param params 参数
     * @return 响应结果
     */
    public static String get(String strUrl, Map<String,Object> params) throws Exception {
        String url = strUrl+"?"+getParams(params);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        return okCall(okHttpClient,request);
    }

    /**
     * 组装参数
     * @param params
     * @return
     */
    private static String getParams(Map<String,Object> params){
        StringBuffer stringBuffer = new StringBuffer();
        if(params != null){
            for(String key : params.keySet()){
                stringBuffer.append(key);
                stringBuffer.append("=");
                stringBuffer.append(params.get(key));
                stringBuffer.append("&");
            }
        }
        return stringBuffer.substring(0,stringBuffer.length()-1);
    }

    /**
     * 开始请求
     * @param okHttpClient
     * @param request
     * @return
     * @throws Exception
     */
    private static String okCall(OkHttpClient okHttpClient,Request request) throws Exception {
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();

        int code = response.code();
        if(code != 200){
            return "500";
        }
        return response.body().string();
    }
}
