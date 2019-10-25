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
     * @param urls
     */
    public void save(Map<String, List<String>> urls){
        this.urls = urls;
    }

    /**
     * 插入缓存
     * @param key
     * @param urls
     */
    public void set(String key, List<String> urls){
        this.urls.put(key, urls);
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
    public void remove(String key){
        urls.remove(key);
    }
}
