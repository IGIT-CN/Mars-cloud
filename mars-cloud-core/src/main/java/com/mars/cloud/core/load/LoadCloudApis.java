package com.mars.cloud.core.load;

import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.helper.ZkHelper;
import com.mars.cloud.core.model.UrlListModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取注册中心的服务
 */
public class LoadCloudApis {

    /**
     * 读取注册中心的服务
     * @throws Exception 异常
     */
    public static void loadServiceApis() throws Exception {
        if (LoadServerList.hasData()){
            Map<String,UrlListModel> urlListModelMap = init();
            LoadServerList.replace(urlListModelMap);
        }
    }

    /**
     * 加载数据
     * @return 结果
     * @throws Exception 异常
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

                if(nodes != null || !nodes.isEmpty()) {
                    List<String> urls = new ArrayList<>();
                    /* 将接口集群列表的每个节点的数据拿出来 */
                    for (String str : nodes) {
                        String data = ZkHelper.getData(path + "/" + str);
                        urls.add(data);
                    }

                    /* 将接口列表存入本地缓存 */
                    if (!urls.isEmpty()) {
                        /* 先取原有对象，为了保持轮询 */
                        UrlListModel urlListModel = LoadServerList.get(serverName);
                        if(urlListModel == null){
                            urlListModel = new UrlListModel();
                        }
                        /* 只更新接口列表，不更新轮询下标 */
                        urlListModel.setUrls(urls);
                        urlListModelMap.put(path,urlListModel);
                    }
                }
            }
            return urlListModelMap;
        } catch (Exception e){
            throw new Exception("从注册中心读取接口失败",e);
        }
    }
}
