package com.mars.cloud.request;

import com.mars.cloud.core.util.CloudHttpUtil;
import com.mars.cloud.core.util.TypeConverUtil;
import com.mars.cloud.core.model.UrlListModel;
import com.mars.cloud.load.GetServerApis;
import com.mars.cloud.util.BalancingUtil;
import com.mars.core.annotation.enums.RequestMetohd;

import java.util.Map;

/**
 * 发起rest请求
 */
public class MarsRest {

    /**
     * 发起post请求
     * @param serverName serverName
     * @param methodName methodName
     * @param params params
     * @return 结果
     * @throws Exception 异常
     */
    public static String post(String serverName, String methodName, Object params) throws Exception {
        return request(serverName,methodName,RequestMetohd.POST,params,String.class);
    }

    /**
     * 发起get请求
     * @param serverName serverName
     * @param methodName methodName
     * @param params params
     * @return 结果
     * @throws Exception 异常
     */
    public static String get(String serverName, String methodName, Object params) throws Exception {
        return request(serverName,methodName,RequestMetohd.GET,params,String.class);
    }

    /**
     * 发起post请求
     * @param serverName serverName
     * @param methodName methodName
     * @param params params
     * @param cls cls
     * @param <T> 泛型
     * @return 结果
     * @throws Exception 异常
     */
    public static <T> T post(String serverName, String methodName, Object params,Class<T> cls) throws Exception {
        return request(serverName,methodName,RequestMetohd.POST,params,cls);
    }

    /**
     * 发起get请求
     * @param serverName serverName
     * @param methodName methodName
     * @param params params
     * @param cls cls
     * @param <T> 泛型
     * @return 结果
     * @throws Exception 异常
     */
    public static  <T> T  get(String serverName, String methodName, Object params,Class<T> cls) throws Exception {
        return request(serverName,methodName,RequestMetohd.GET,params,cls);
    }

    /**
     * 发起请求
     * @param serverName serverName
     * @param methodName methodName
     * @param params params
     * @param cls cls
     * @param <T> 泛型
     * @return 结果
     * @throws Exception 异常
     */
    private static <T> T request(String serverName, String methodName, RequestMetohd method, Object params,Class<T> cls) throws Exception{
        String url = "";
        try {

            UrlListModel urlList = GetServerApis.getUrls(serverName, methodName);

            url = getUrl(urlList);

            String result = null;

            if(method.equals(RequestMetohd.GET)){
                result = CloudHttpUtil.get(url, (Map<String,Object>)TypeConverUtil.conver(params,method));
            } else {
                result = CloudHttpUtil.request(url,TypeConverUtil.conver(params,method));
            }

            return TypeConverUtil.conver(result,cls);
        } catch (Exception e){
            throw new Exception("发起请求出现异常,url:["+url+"],",e);
        }
    }

    /**
     * 根据负载均衡策略，从集群中获取一个连接
     * @param urlList
     * @return
     */
    private static String getUrl(UrlListModel urlList) throws Exception {
        if(urlList == null || urlList.getUrls() == null || urlList.getUrls().size() < 1){
            throw new Exception("请求地址不正确，请检查serverName和methodName后再尝试");
        }
        return "http://"+ BalancingUtil.getUrl(urlList);
    }
}
