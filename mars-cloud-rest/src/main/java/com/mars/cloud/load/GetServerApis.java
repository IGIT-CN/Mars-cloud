package com.mars.cloud.load;

import com.mars.cloud.core.cache.CacheManager;
import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.helper.ZkHelper;
import com.mars.cloud.core.model.UrlListModel;

import java.util.ArrayList;
import java.util.List;

public class GetServerApis {

    /**
     * 根据服务名和Controller的mapping名称,获取接口信息
     * @param serverName
     * @return
     */
    public static UrlListModel getUrls(String serverName,String methodName) throws Exception {
        UrlListModel urlListModel = getUrlsFromCache(serverName, methodName);
        if(urlListModel == null){
            urlListModel = getUrlsFromZookeeper(serverName, methodName);
        }
        return urlListModel;
    }

    /**
     * 从本地缓存读取接口信息
     * @param serverName
     * @return
     */
    private static UrlListModel getUrlsFromCache(String serverName, String methodName) throws Exception {
        return CacheManager.getUrlListModel(serverName+"->"+methodName);
    }

    /**
     * 从zookeeper读取接口信息，然后加入本地缓存
     * @param serverName
     * @return
     */
    private static UrlListModel getUrlsFromZookeeper(String serverName, String methodName) throws Exception {

        if(!ZkHelper.hasConnection()){
            ZkHelper.openConnection();
        }

        String path = CloudConstant.SERVER_NODE.replace("{serverName}",serverName).replace("{method}",methodName);
        List<String> urlNodes = ZkHelper.getChildren(path);

        List<String> urls = new ArrayList<>();
        for(String urlNode : urlNodes){
            urls.add(ZkHelper.getData(path+"/"+urlNode));
        }

        UrlListModel urlListModel = new UrlListModel();
        urlListModel.setUrls(urls);

        /* 将接口缓存下来 */
        CacheManager.addUrlListModel(serverName+"->"+methodName,urlListModel);

        return urlListModel;
    }
}
