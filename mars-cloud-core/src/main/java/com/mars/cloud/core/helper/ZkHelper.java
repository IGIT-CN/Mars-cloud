package com.mars.cloud.core.helper;

import com.alibaba.fastjson.JSONObject;
import com.mars.cloud.core.util.CloudConfigUtil;
import com.mars.core.logger.MarsLogger;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.zookeeper.*;

import java.util.List;

/**
 * zookeeper帮助类
 */
public class ZkHelper {

    private static MarsLogger marsLogger = MarsLogger.getLogger(ZkHelper.class);

    /**
     * zookeeper对象
     */
    private static ZkClient zkClient;

    /**
     * zookeeper连接
     */
    private static ZkConnection zkConnection;

    /**
     * session超时时间
     */
    private static int sessionTimeout = 100000;

    /**
     * 注册中心地址
     */
    private static String registeds;


    /**
     * 初始化
     *
     * @throws Exception
     */
    private static void init() throws Exception {
        JSONObject config = CloudConfigUtil.getCloudConfig();

        registeds = config.getString("register");
        Object configTimeOut = config.get("sessionTimeout");
        if (configTimeOut != null) {
            sessionTimeout = Integer.parseInt(configTimeOut.toString());
        }
    }

    /**
     * 打开连接
     *
     * @throws Exception
     */
    public static void openConnection() throws Exception {
        try {
            if (registeds == null) {
                init();
            }

            if (zkConnection == null || !zkConnection.getZookeeperState().isConnected()) {
                zkConnection = new ZkConnection(registeds, sessionTimeout);
                zkClient = new ZkClient(zkConnection, 30000);
                marsLogger.info("连接zookeeper成功");
            }
        } catch (Exception e) {
            throw new Exception("连接zookeeper失败", e);
        }
    }


    /**
     * 创建节点
     *
     * @param path
     * @param data
     * @return
     */
    public static String createNodes(String path, String data) {
        String[] pa = path.split("/");
        StringBuffer pat = new StringBuffer();
        for(int i=1;i<pa.length;i++){
            pat.append("/");
            pat.append(pa[i]);
            if(i < pa.length-1){
                createNode(pat.toString(),"blank",CreateMode.PERSISTENT);
            } else {
                createNode(pat.toString(),data,CreateMode.EPHEMERAL);
            }
        }
        return "ok";
    }

    /**
     * 创建节点
     *
     * @param path
     * @param data
     * @return
     */
    public static String createNode(String path, String data,CreateMode createMode) {
        boolean stat = zkClient.exists(path);
        if (!stat) {
            return zkClient.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,createMode);
        } else {
            setData(path, data);
            return "ok";
        }
    }

    /**
     * 获取路径下所有子节点
     *
     * @param path
     * @return
     */
    public static List<String> getChildren(String path) {
        List<String> children = zkClient.getChildren(path);
        return children;
    }

    /**
     * 获取节点上面的数据
     *
     * @param path
     * @return
     */
    public static String getData(String path) {
        boolean stat = zkClient.exists(path);
        if (stat) {
            String data = zkClient.readData(path);
            return data;
        }
        return null;
    }

    /**
     * 设置节点信息
     *
     * @param path
     * @param data
     * @return
     */
    public static void setData(String path, String data) {
        if (zkClient.exists(path)) {
            zkClient.writeData(path, data,-1);
        }
    }

    /**
     * 删除节点
     *
     * @param path
     */
    public static void deleteNode(String path) {
        if (zkClient.exists(path)) {
            zkClient.delete(path, -1);
        }
    }

    /**
     * 节点是否存在
     * @param path
     * @return
     */
    public static boolean exists(String path) {
        return zkClient.exists(path);
    }

    /**
     * 获取某个路径下孩子的数量
     *
     * @param path
     * @return
     */
    public static Integer getChildrenNum(String path) {
        return zkClient.getChildren(path).size();
    }

    /**
     * 关闭连接
     *
     */
    public static void closeConnection() {
        if (zkClient != null) {
            zkClient.close();
        }
    }
}
