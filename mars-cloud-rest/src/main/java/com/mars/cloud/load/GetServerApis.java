package com.mars.cloud.load;

import com.mars.cloud.core.cache.CacheApi;
import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.helper.ZkHelper;
import com.mars.cloud.core.model.UrlListModel;
import com.mars.cloud.util.BalancingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取请求路径
 */
public class GetServerApis {

    private static Logger logger = LoggerFactory.getLogger(GetServerApis.class);

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
        List<String> urls = new ArrayList<>();

        String path = CloudConstant.SERVER_NODE.replace("{serverName}",serverName).replace("{method}",methodName);
        List<String> urlNodes = ZkHelper.getChildren(path);
        if(urlNodes == null || urlNodes.size() < 1){
            urls = CacheApi.getCacheApi().get(path);
            logger.error("注册中心没有发现接口地址，怀疑服务挂掉了，正在使用本地缓存补充，请及时检查: 服务名称["+serverName+"->"+methodName+"]");
        } else {
            for(String urlNode : urlNodes){
                urls.add(ZkHelper.getData(path+"/"+urlNode));
            }
            CacheApi.getCacheApi().set(path,urls);
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
