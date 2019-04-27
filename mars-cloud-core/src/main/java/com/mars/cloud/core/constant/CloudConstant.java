package com.mars.cloud.core.constant;

/**
 * 常量
 */
public class CloudConstant {

    /**
     * 存储api接口的节点前缀
     */
    public static final String BASE_SERVER_NODE = "/serviceApiList";

    /**
     * 存储api接口的节点二级前缀
     */
    public static final String SERVER_NODE = BASE_SERVER_NODE + "/{serverName}";

    /**
     * 存储api接口的节点
     */
    public static final String API_SERVER_NODE = SERVER_NODE + "/{method}";

}
