package com.mars.cloud.rest;

import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.helper.ZookeeperHelper;
import com.mars.cloud.model.UrlListModel;
import com.mars.cloud.util.ServerListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取注册中心的服务
 */
public class LoadCloudApis {

    private static ZookeeperHelper zookeeperHelper = new ZookeeperHelper();


    /**
     * 读取注册中心的服务
     * @throws Exception
     */
    public static void loadServiceApis() throws Exception {
        if (ServerListUtil.hasData()){
            init();
        }
    }

    /**
     *
     * @throws Exception
     */
    private static void init() throws Exception {
        try {
            /* 打开zookeeper连接 */
            zookeeperHelper.openConnection();

            /* 获取服务接口列表 */
            List<String> list = zookeeperHelper.getChildren(CloudConstant.BASE_SERVER_NODE);

            /* 遍历服务接口根目录下所有的serverName */
            for(String serverName : list) {

                String path = CloudConstant.BASE_SERVER_NODE + "/" + serverName;

                /* 获取所有serverName对应的接口集群列表 */
                List<String> nodes = zookeeperHelper.getChildren(path);

                List<String> urls = new ArrayList<>();

                /* 将接口集群列表的每个节点的数据拿出来 */
                for (String str : nodes) {
                    String data = zookeeperHelper.getData(path + "/" + str);
                    urls.add(data);
                }

                if (!urls.isEmpty()) {
                    /* 将接口列表存入本地缓存 */
                    UrlListModel urlListModel = new UrlListModel();
                    urlListModel.setUrls(urls);
                    ServerListUtil.add(path, urlListModel);
                }
            }
        } catch (Exception e){
            throw new Exception("从注册中心读取接口失败",e);
        } finally {
            zookeeperHelper.closeConnection();
        }
    }
}
