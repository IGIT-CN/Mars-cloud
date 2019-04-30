package com.mars.cloud.core.load;

import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.helper.ZkHelper;
import com.mars.cloud.core.model.UrlListModel;
import com.mars.core.logger.MarsLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取注册中心的服务
 */
public class LoadCloudApis {

    private static MarsLogger marsLogger = MarsLogger.getLogger(LoadCloudApis.class);

    /**
     * 读取注册中心的服务
     * @throws Exception
     */
    public static void loadServiceApis() throws Exception {
        if (LoadServerList.hasData()){
            Map<String,UrlListModel> urlListModelMap = init();
            LoadServerList.replace(urlListModelMap);
        }
    }

    /**
     * 加载数据
     * @throws Exception
     */
    public static Map<String,UrlListModel> init() throws Exception {
        try {

            /* 打开zookeeper连接 */
            ZkHelper.openConnection();

            /* 获取服务接口列表 */
            List<String> list = ZkHelper.getChildren(CloudConstant.BASE_SERVER_NODE);

            Map<String,UrlListModel> urlListModelMap = new HashMap<>();

            /* 遍历服务接口根目录下所有的serverName */
            for(String serverName : list) {

                String path = CloudConstant.BASE_SERVER_NODE + "/" + serverName;

                /* 获取所有serverName对应的接口集群列表 */
                List<String> nodes = ZkHelper.getChildren(path);

                if(nodes == null || nodes.size() < 1){
                    /* 如果此节点下没有存储数据的子节点，那么就把他删掉 */
                    ZkHelper.deleteNode(path);
                } else {
                    List<String> urls = new ArrayList<>();
                    /* 将接口集群列表的每个节点的数据拿出来 */
                    for (String str : nodes) {
                        String data = ZkHelper.getData(path + "/" + str);
                        urls.add(data);
                    }

                    if (!urls.isEmpty()) {
                        /* 将接口列表存入本地缓存 */
                        UrlListModel urlListModel = LoadServerList.get(serverName);
                        if(urlListModel == null){
                            urlListModel = new UrlListModel();
                        }
                        urlListModel.setUrls(urls);
                        urlListModelMap.put(path,urlListModel);
                    }
                }
            }
            return urlListModelMap;
        } catch (Exception e){
            marsLogger.error("从注册中心读取接口失败.............",e);
            throw new Exception("从注册中心读取接口失败",e);
        }
    }
}
