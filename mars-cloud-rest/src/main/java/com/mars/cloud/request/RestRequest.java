package com.mars.cloud.request;

import com.mars.cloud.core.util.CloudHttpUtil;
import com.mars.cloud.core.util.TypeConverUtil;
import com.mars.cloud.model.UrlListModel;
import com.mars.cloud.rest.LoadCloudApis;
import com.mars.cloud.util.LoadBalancingUtil;
import com.mars.cloud.util.ServerListUtil;
import com.mars.core.annotation.enums.RequestMetohd;

/**
 * 发起rest请求
 */
public class RestRequest {

    /**
     * 发起post请求
     * @param serverName
     * @param methodName
     * @param params
     * @return
     * @throws Exception
     */
    public static String post(String serverName, String methodName, Object params) throws Exception {
        return request(serverName,methodName,RequestMetohd.POST,params,String.class);
    }

    /**
     * 发起get请求
     * @param serverName
     * @param methodName
     * @param params
     * @return
     * @throws Exception
     */
    public static String get(String serverName, String methodName, Object params) throws Exception {
        return request(serverName,methodName,RequestMetohd.GET,params,String.class);
    }

    /**
     * 发起post请求
     * @param serverName
     * @param methodName
     * @param params
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T post(String serverName, String methodName, Object params,Class<T> cls) throws Exception {
        return request(serverName,methodName,RequestMetohd.POST,params,cls);
    }

    /**
     * 发起get请求
     * @param serverName
     * @param methodName
     * @param params
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public static  <T> T  get(String serverName, String methodName, Object params,Class<T> cls) throws Exception {
        return request(serverName,methodName,RequestMetohd.GET,params,cls);
    }

    /**
     * 发起请求
     * @param serverName
     * @param methodName
     * @param metohd
     * @param params
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T request(String serverName, String methodName, RequestMetohd metohd, Object params,Class<T> cls) throws Exception{
        String url = "";
        try {
            LoadCloudApis.loadServiceApis();

            UrlListModel urlList = ServerListUtil.get(serverName+"-"+methodName);

            url = getUrl(urlList);

            String result = null;

            if(metohd.equals(RequestMetohd.GET)){
                result = CloudHttpUtil.get(url, TypeConverUtil.conver(params));
            } else {
                result = CloudHttpUtil.request(url,params,"cloud");
            }

            if(result.equals("500")){
                throw new Exception("请求"+url+"失败");
            }

            return TypeConverUtil.conver(result,cls);
        } catch (Exception e){
            throw new Exception("请求"+url+"失败",e);
        }
    }


    /**
     * 根据负载均衡策略，从集群中获取一个连接
     * @param urlList
     * @return
     */
    private static String getUrl(UrlListModel urlList) {
        return LoadBalancingUtil.getUrl(urlList);
    }
}
