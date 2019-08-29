package com.mars.cloud.core.cache;

import com.mars.cloud.core.model.UrlListModel;
import com.mars.core.logger.MarsLogger;

import java.util.List;

/**
 * 接口缓存管理
 */
public class CacheManager {

    private static MarsLogger marsLogger = MarsLogger.getLogger(CacheManager.class);


    /**
     * 从缓存读数据
     * @param serverName
     * @return
     */
    public static UrlListModel getUrlListModel(String serverName){
        return CacheDAO.getUrlListModel(serverName);
    }

    /**
     * 往缓存加数据
     * @param path
     */
    public static void addUrlListModel(String path){

        marsLogger.info(path);
    }

    /**
     * 往缓存加数据
     * @param urlListModel
     */
    public static void addUrlListModel(String serverName,UrlListModel urlListModel){
        List<String> urls = urlListModel.getUrls();
        if(urls == null){
            return;
        }
        for(String url : urls){
            CacheDAO.addApi(serverName,url);
        }
    }

    /**
     * 删除缓存
     * @param path
     */
    public static void deleteUrlListModel(String path){
        marsLogger.info(path);

        CacheDAO.deleteApi(null,null);
    }

    /**
     * 更新缓存
     * @param path
     */
    public static void changeUrlListModel(String path){
        marsLogger.info(path);
        // TODO 暂时没有修改的情况
        CacheDAO.updateApi(null,null);
    }
}
