package com.mars.cloud.util;

import com.alibaba.fastjson.JSONArray;
import com.mars.cloud.core.constant.CloudConstant;
import com.mars.core.constant.EasySpace;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务列表
 */
public class ServerListUtil {

    private static EasySpace constants = EasySpace.getEasySpace();

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
    public static void add(String key, JSONArray value){
        Object obj = constants.getAttr("serverList");

        Map<String,JSONArray> serverList = new HashMap<>();

        if(obj != null){
            serverList = (Map<String,JSONArray>)obj;
        }
        serverList.put(key,value);

        constants.setAttr("serverList",serverList);
    }

    /**
     * 获取服务接口列表
     * @param key 服务name + controller映射的value
     * @return
     */
    public static JSONArray get(String key){
        Object obj = constants.getAttr("serverList");

        if(obj != null){
            Map<String,JSONArray> serverList = (Map<String,JSONArray>)obj;
            return serverList.get(CloudConstant.BASE_SERVER_NODE+"/"+key);
        }

        return null;
    }

    /**
     * 获取服务接口列表
     * @return
     */
    public static Map<String,JSONArray> getAll(){
        Object obj = constants.getAttr("serverList");

        if(obj != null){
            Map<String,JSONArray> serverList = (Map<String,JSONArray>)obj;
            return serverList;
        }

        return null;
    }
}
