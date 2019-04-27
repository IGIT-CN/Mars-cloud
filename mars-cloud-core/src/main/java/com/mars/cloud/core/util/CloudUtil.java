package com.mars.cloud.core.util;

import com.mars.core.util.ConfigUtil;

import java.net.*;
import java.util.Enumeration;

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
        getLocalIp();
        initLocal();
        return localHost;
    }

    /**
     * 获取本机在局域网的IP
     * @return
     * @throws Exception
     */
    public static String getLocalIp() throws Exception {
        if(ip == null){
            ip = getLocalHostLANAddress().getHostAddress();
        }
        return ip;
    }

    /**
     * 初始化local参数
     * @throws Exception
     */
    private static void initLocal() throws Exception {
        if(port == null){
            port = ConfigUtil.getConfig().getString("port");
        }
        if(protocol == null){
            Object proto = CloudConfigUtil.getCloudConfig("protocol");
            if(proto == null){
                protocol = "http";
            } else {
                protocol = proto.toString();
            }
            if(!protocol.equals("http") && !protocol.equals("https")){
                protocol = null;
                throw new Exception("Mars-cloud目前只支持http和https协议");
            }
        }
        if(localHost == null){
            localHost = protocol+"://"+ip+":"+port;
        }
    }

    /**
     * 正确的IP拿法，即优先拿site-local地址
     * @return
     * @throws UnknownHostException
     */
    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

}
