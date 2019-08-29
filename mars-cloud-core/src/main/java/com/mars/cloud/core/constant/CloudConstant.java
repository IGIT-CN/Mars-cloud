package com.mars.cloud.core.constant;

/**
 * 常量
 */
public class CloudConstant {

    /**
     * 存储api接口的根目录
     */
    public static final String BASE_SERVER_NODE = "/serviceApiList";

    /**
     * 存储api接口的serverName目录
     */
    public static final String SERVER_NODE = BASE_SERVER_NODE + "/{serverName}->{method}";

    /**
     * 存储api接口的节点
     */
    public static final String API_SERVER_NODE = SERVER_NODE + "/{ip}-{port}";

    /**
     * 本地缓存
     */
    public static final String CACHE_APIS = "cacheApis-";

}
