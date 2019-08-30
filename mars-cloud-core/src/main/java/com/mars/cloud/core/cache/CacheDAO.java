package com.mars.cloud.core.cache;

import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.model.UrlListModel;
import com.mars.core.constant.MarsSpace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口缓存基础操作
 */
public class CacheDAO {

    /**
     * 本地缓存
     */
    private static MarsSpace marsSpace = MarsSpace.getEasySpace();

    /**
     * 获取接口
     * @param serverName
     * @return
     */
    public static UrlListModel getUrlListModel(String serverName){
        Object apis =  getApiCache().get(getKey(serverName));
        if(apis != null){
            return (UrlListModel)apis;
        }
        return null;
    }

    /**
     * 删除接口
     * @param serverName
     */
    public static void deleteApi(String serverName,String url) {
        UrlListModel urlListModel = getUrlListModel(serverName);
        if (urlListModel == null) {
            return;
        }
        List<String> urlList = urlListModel.getUrls();
        if (urlList == null) {
            return;
        }
        if(url.indexOf("/") > -1){
            if (urlList.contains(url)) {
                urlList.remove(url);
            }
        } else {
            List<String> removeUrl = new ArrayList<>();
            for(String item : urlList){
                if(item.indexOf(url) > -1){
                    removeUrl.add(item);
                }
            }
            urlList.removeAll(removeUrl);
        }

    }

    /**
     * 添加接口
     * @param serverName
     * @param url
     */
    public static void addApi(String serverName,String url){
        UrlListModel urlListModel = getUrlListModel(serverName);

        if(urlListModel == null){
            urlListModel = new UrlListModel();
        }

        List<String> urls = urlListModel.getUrls();
        if(urls == null){
            urls = new ArrayList<>();
        }

        if(!urls.contains(url)){
            urls.add(url);
        }
        urlListModel.setUrls(urls);

        getApiCache().put(getKey(serverName),urlListModel);
    }

    /**
     * 清理本地缓存
     */
    public static void clear(){
        marsSpace.remove(CloudConstant.CACHE_APIS);
    }

    /**
     * 获取接口集合
     * @return
     */
    private static Map<String,Object> getApiCache(){
        Map<String,Object> apisMap = null;

        Object api = marsSpace.getAttr(CloudConstant.CACHE_APIS);
        if(api == null){
            apisMap = new HashMap<>();
            marsSpace.setAttr(CloudConstant.CACHE_APIS,apisMap);
        } else {
            apisMap = (Map<String,Object>)api;
        }
        return apisMap;
    }

    /**
     * 获取key
     * @param serverName
     * @return
     */
    private static String getKey(String serverName){
        return CloudConstant.CACHE_APIS+serverName;
    }
}
