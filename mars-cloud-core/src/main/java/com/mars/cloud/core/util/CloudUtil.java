package com.mars.cloud.core.util;

import com.mars.core.util.MarsAddressUtil;

/**
 * 工具类
 */
public class CloudUtil {

    /**
     * 本机局域网IP
     */
    private static String ip;

    /**
     * 本服务的端口号
     */
    private static String port;

    /**
     * 本服务的请求协议，只有http和https可选
     */
    private static String protocol;

    /**
     * 本机接口的完整请求前缀
     */
    private static String localHost;

    /**
     * 获取本机接口的完整请求前缀
     * @return localhost
     * @throws Exception 异常
     */
    public static String getLocalHost() throws Exception {
        if(localHost == null){
            getLocalIp();
            getPort();
            getProtocol();
            localHost = protocol+"://"+ip+":"+port;
        }
        return localHost;
    }

    /**
     * 获取本机在局域网的IP
     * @return ip
     * @throws Exception 异常
     */
    public static String getLocalIp() throws Exception {
        ip = MarsAddressUtil.getLocalIp();
        return ip;
    }

    /**
     * 获取端口号
     * @return 端口号
     */
    public static String getPort() {
        port = MarsAddressUtil.getPort();
        return port;
    }

    /**
     * 初始化protocol
     * @throws Exception 异常
     */
    public static void getProtocol() throws Exception {
        if(protocol == null){
            Object proto = CloudConfigUtil.getCloudConfig("protocol");
            if(proto == null){
                protocol = "http";
            } else if(proto.equals("http") || proto.equals("https")) {
                protocol = proto.toString();
            } else {
                protocol = null;
                throw new Exception("Mars-cloud目前只支持http和https协议");
            }
        }
    }
}
