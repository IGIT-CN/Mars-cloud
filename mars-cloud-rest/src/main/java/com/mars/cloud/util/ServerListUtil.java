package com.mars.cloud.util;

import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.model.UrlListModel;
import com.mars.core.constant.MarsSpace;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务列表
 */
public class ServerListUtil {

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
     * 将接口缓存在本地
     * @param key
     * @param value
     */
    public static void add(String key, UrlListModel value){
        Map<String,UrlListModel> serverList = getAll();

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

    public static void countUp(String key){

    }

    /**
     * 获取服务接口列表
     * @return
     */
    public static Map<String,UrlListModel> getAll(){
        Object obj = constants.getAttr("serverList");

        if(obj != null){
            Map<String, UrlListModel> serverList = (Map<String,UrlListModel>)obj;
            return serverList;
        }

        return null;
    }
}
