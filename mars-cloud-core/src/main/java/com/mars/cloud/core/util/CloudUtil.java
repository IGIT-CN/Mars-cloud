package com.mars.cloud.core.util;

import com.mars.core.util.ConfigUtil;
import com.mars.core.util.MarsUtil;

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
     * 本机接口的完整请求后缀
     */
    private static String localHost;


    /**
     * 获取本机接口的完整请求后缀
     * @return
     * @throws Exception
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
     * @return
     * @throws Exception
     */
    public static String getLocalIp() throws Exception {
        if(ip == null){
            ip = MarsUtil.getLocalHostLANAddress().getHostAddress();
        }
        return ip;
    }

    /**
     * 获取端口号
     * @return
     * @throws Exception
     */
    public static String getPort() {
        if(port == null){
            port = ConfigUtil.getConfig().getString("port");
        }
        return port;
    }

    /**
     * 初始化protocol
     * @throws Exception
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
