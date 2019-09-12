package com.mars.cloud.request;

import com.mars.cloud.core.util.CloudHttpUtil;
import com.mars.cloud.core.util.TypeConverUtil;
import com.mars.cloud.load.GetServerApis;

import java.util.HashMap;

/**
 * 发起rest请求
 */
public class MarsRest {

    /**
     * 发起请求
     *
     * @param serverName serverName
     * @param methodName methodName
     * @param params     params
     * @return 结果
     * @throws Exception 异常
     */
    public static String request(String serverName, String methodName, Object params) throws Exception {
        return request(serverName, methodName, params, String.class);
    }

    /**
     * 发起请求
     *
     * @param serverName serverName
     * @param methodName methodName
     * @return 结果
     * @throws Exception 异常
     */
    public static String request(String serverName, String methodName) throws Exception {
        return request(serverName, methodName, null, String.class);
    }

    /**
     * 发起请求
     *
     * @param serverName serverName
     * @param methodName methodName
     * @return 结果
     * @throws Exception 异常
     */
    public static <T> T request(String serverName, String methodName, Class<T> cls) throws Exception {
        return request(serverName, methodName, null, cls);
    }

    /**
     * 发起请求
     *
     * @param serverName serverName
     * @param methodName methodName
     * @param params     params
     * @param cls        cls
     * @param <T>        泛型
     * @return 结果
     * @throws Exception 异常
     */
    public static <T> T request(String serverName, String methodName, Object params, Class<T> cls) throws Exception {
        String url = "";
        try {

            url = GetServerApis.getUrl(serverName, methodName);

            if(params == null){
                params = new HashMap<>();
            }

            String result = CloudHttpUtil.request(url, params);

            return TypeConverUtil.conver(result, cls);
        } catch (Exception e) {
            throw new Exception("发起请求出现异常,url:[" + url + "],", e);
        }
    }
}
