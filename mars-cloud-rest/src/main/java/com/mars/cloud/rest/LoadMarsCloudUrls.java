package com.mars.cloud.rest;

import com.alibaba.fastjson.JSONArray;
import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.helper.ZookeeperHelper;
import com.mars.cloud.util.ServerListUtil;

import java.util.List;

/**
 * 读取注册中心的服务
 */
public class LoadMarsCloudUrls {

    private static ZookeeperHelper zookeeperHelper = new ZookeeperHelper();


    /**
     * 读取注册中心的服务
     * @throws Exception
     */
    public static void loadServices() throws Exception {
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

            for(String serverName : list){
                String path = CloudConstant.SERVER_NODE.replace("{serverName}",serverName);
                List<String> list2 = zookeeperHelper.getChildren(path);
                for(String method : list2){
                    String path2 = CloudConstant.API_SERVER_NODE.replace("{serverName}",serverName).replace("{method}",method);
                    String data = zookeeperHelper.getData(path2);
                    if(data != null && !data.equals("")){
                        ServerListUtil.add(path2, JSONArray.parseArray(data));
                    }
                }
            }
        } catch (Exception e){
            throw new Exception("从注册中心读取接口失败",e);
        } finally {
            zookeeperHelper.closeConnection();
        }
    }
}
