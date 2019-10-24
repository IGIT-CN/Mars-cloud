package com.mars.cloud.request;

import com.mars.cloud.core.util.CloudHttpUtil;
import com.mars.cloud.load.GetServerApis;

import java.util.HashMap;

/**
 * 发起rest请求
 */
public class MarsRestTemplate {

    /**
     * 发起请求
     *
     * @param serverName serverName
     * @param methodName methodName
     * @return 结果
     * @throws Exception 异常
     */
    public static String request(String serverName, String methodName) throws Exception {
        return request(serverName, methodName, null);
    }

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
        String url = "";
        try {

            url = GetServerApis.getUrl(serverName, methodName);

            if(params == null){
                params = new HashMap<>();
            }

            return CloudHttpUtil.request(url, params);
        } catch (Exception e) {
            throw new Exception("发起请求出现异常,url:[" + url + "],", e);
        }
    }
}
