package com.mars.cloud.core.load;

import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.model.UrlListModel;
import com.mars.core.constant.MarsSpace;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务列表
 */
public class LoadServerList {

    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 是否有数据
     * @return
     */
    public static boolean hasData(){
        Object obj = constants.getAttr("serverList");
        return obj == null;
    }

    /**
     * 替换本地缓存
     * @param serverList
     */
    public static void replace(Map<String, UrlListModel> serverList){
        constants.setAttr("serverList",serverList);
    }

    /**
     * 将接口缓存在本地
     * @param key
     * @param value
     */
    public static void add(String key, UrlListModel value){
        Object obj = constants.getAttr("serverList");
        Map<String, UrlListModel> serverList = (Map<String,UrlListModel>)obj;

        if(serverList == null){
            serverList = new HashMap<>();
        }
        serverList.put(key,value);

        constants.setAttr("serverList",serverList);
    }

    /**
     * 获取服务接口列表
     * @param key 服务name + controller映射的value
     * @return
     */
    public static UrlListModel get(String key){
        return getAll().get(CloudConstant.BASE_SERVER_NODE+"/"+key);
    }

    /**
     * 获取服务接口列表
     * @return
     */
    public static Map<String,UrlListModel> getAll(){
        Object obj = constants.getAttr("serverList");
        Map<String, UrlListModel> serverList = (Map<String,UrlListModel>)obj;
        return serverList;
    }
}
