package com.mars.cloud.load;

import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.helper.ZkHelper;
import com.mars.cloud.core.model.UrlListModel;
import com.mars.cloud.util.BalancingUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取请求路径
 */
public class GetServerApis {

    /**
     * 根据服务名和Controller的mapping名称,获取接口信息
     * @param serverName
     * @return
     */
    public static String getUrl(String serverName,String methodName) throws Exception {
        UrlListModel urlListModel = getUrlsFromZookeeper(serverName, methodName);
        return getUrlForList(urlListModel);
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
        if(urlNodes == null || urlNodes.size() < 1){
            return null;
        }

        List<String> urls = new ArrayList<>();
        for(String urlNode : urlNodes){
            urls.add(ZkHelper.getData(path+"/"+urlNode));
        }

        UrlListModel urlListModel = new UrlListModel();
        urlListModel.setUrls(urls);

        return urlListModel;
    }

    /**
     * 根据负载均衡策略，从集群中获取一个连接
     * @param urlList
     * @return
     */
    private static String getUrlForList(UrlListModel urlList) throws Exception {
        if(urlList == null || urlList.getUrls() == null || urlList.getUrls().size() < 1){
            throw new Exception("请求地址不正确，请检查serverName和methodName后再尝试");
        }
        return "http://"+ BalancingUtil.getUrl(urlList);
    }
}
