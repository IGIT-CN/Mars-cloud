package com.mars.cloud.request;

import com.mars.cloud.rest.LoadCloudApis;
import com.mars.cloud.util.ServerListUtil;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RestRequest {

    public static Object request(String serverName, String methodName, Map<String, Object> params) throws Exception{
        String url = "";
        try {
            LoadCloudApis.loadServiceApis();


            List<String> urlList = ServerListUtil.get(serverName+"-"+methodName);

            url = getUrl(urlList);

            /* TODO(后面优化) */
        } catch (Exception e){
            throw new Exception("请求"+url+"失败",e);
        }

        return null;
    }

    /**
     * 根据负载均衡策略，从集群中获取一个连接
     * @param urlList
     * @return
     */
    private static String getUrl(List<String> urlList){
        /* TODO(先写个随机放着，后面完善) */
        Random random = new Random();
        int i = random.nextInt(urlList.size()-1);
        if(i<0){
            i = 0;
        }
        return urlList.get(i);
    }
}
