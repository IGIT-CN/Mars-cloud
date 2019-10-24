package com.mars.cloud.core.cache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地接口缓存
 */
public class CacheApi {

    /**
     * 缓存
     */
    private Map<String, List<String>> urls = new ConcurrentHashMap<>();

    private static CacheApi cacheApi = new CacheApi();

    public static CacheApi getCacheApi(){
        return cacheApi;
    }

    /**
     * 插入缓存
     * @param key
     * @param urlList
     */
    public void set(String key,List<String> urlList){
        urls.put(key,urlList);
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public List<String> get(String key){
        return urls.get(key);
    }

    /**
     * 删除缓存
     * @param key
     * @return
     */
    public List<String> remove(String key){
        return urls.remove(key);
    }
}
