package com.mars.cloud.core.cache;

import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.helper.ZkHelper;
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
     *
     * @param serverName
     * @return
     */
    public static UrlListModel getUrlListModel(String serverName) {
        return CacheDAO.getUrlListModel(serverName);
    }

    /**
     * 往缓存加数据
     *
     * @param path
     */
    public static void addUrlListModel(String path) throws Exception {
        marsLogger.info("往本地缓存中添加了:" + path);

        String serverName = getServerName(path);
        if (serverName == null) {
            return;
        }

        String url = getUrl(path);
        if (url == null) {
            return;
        }
        CacheDAO.addApi(serverName, url);
    }

    /**
     * 往缓存加数据
     *
     * @param urlListModel
     */
    public static void addUrlListModel(String serverName, UrlListModel urlListModel) {
        List<String> urls = urlListModel.getUrls();
        if (urls == null) {
            return;
        }
        for (String url : urls) {
            CacheDAO.addApi(serverName, url);
        }
    }

    /**
     * 删除缓存
     *
     * @param path
     */
    public static void deleteUrlListModel(String path) throws Exception {
        marsLogger.info("从本地缓存中删除了:" + path);
        String serverName = getServerName(path);
        if (serverName == null) {
            return;
        }
        String url = getUrl(path);
        if (url == null) {
            return;
        }
        CacheDAO.deleteApi(serverName, url);
    }

    /**
     * 更新缓存
     *
     * @param path
     */
    public static void changeUrlListModel(String path) {
        String serverName = getServerName(path);
        if (serverName == null) {
            return;
        }
        // TODO 暂时不需要这个
    }

    /**
     * 获取服务名称
     *
     * @param path
     * @return
     */
    private static String getServerName(String path) {
        String serverName = path.replace(CloudConstant.BASE_SERVER_NODE + "/", "");
        if (serverName != null && !serverName.equals(CloudConstant.BASE_SERVER_NODE) && serverName.indexOf("/") > -1) {
            return serverName.substring(0, serverName.lastIndexOf("/"));
        }
        return null;
    }

    /**
     * 获取节点下的数据
     *
     * @param path
     * @return
     * @throws Exception
     */
    private static String getUrl(String path) throws Exception {
        if (path.split("/").length == 4) {
            String url = ZkHelper.getData(path);
            if (url != null) {
                return url;
            }
            url = path.substring(path.lastIndexOf("/")+1);
            return url.replace("-", ":");
        }
        return null;
    }
}
