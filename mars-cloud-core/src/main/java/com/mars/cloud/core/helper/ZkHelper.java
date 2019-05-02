package com.mars.cloud.core.helper;

import com.alibaba.fastjson.JSONObject;
import com.mars.cloud.core.util.CloudConfigUtil;
import com.mars.cloud.core.watcher.ZkWatcher;
import com.mars.core.logger.MarsLogger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper帮助类
 */
public class ZkHelper {

    private static MarsLogger marsLogger = MarsLogger.getLogger(ZkHelper.class);

    /**
     * zk对象
     */
    private static ZooKeeper zooKeeper;

    /**
     * session超时时间
     */
    private static int sessionTimeout = 100000;

    /**
     * 注册中心地址
     */
    private static String registeds;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

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
            if(sessionTimeout <= 30000){
                sessionTimeout = 30000;
            }
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

            if (zooKeeper == null || !zooKeeper.getState().isConnected()) {
                zooKeeper = new ZooKeeper(registeds,sessionTimeout,new ZkWatcher(countDownLatch));
                countDownLatch.await();
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
    public static String createNodes(String path, String data) throws Exception {
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
    public static String createNode(String path, String data,CreateMode createMode) throws Exception {
        Stat stat = zooKeeper.exists(path,true);
        if (stat == null) {
            return zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,createMode);
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
    public static List<String> getChildren(String path) throws Exception {
        List<String> children = zooKeeper.getChildren(path,true);
        return children;
    }

    /**
     * 获取节点上面的数据
     *
     * @param path
     * @return
     */
    public static String getData(String path) throws Exception {
        Stat stat = zooKeeper.exists(path,true);
        if (stat != null) {
            byte[] data = zooKeeper.getData(path,true,stat);
            if(data != null){
                return new String(data);
            }
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
    public static void setData(String path, String data) throws Exception {
        if (zooKeeper.exists(path,true) != null) {
            zooKeeper.setData(path, data.getBytes(),-1);
        }
    }

    /**
     * 删除节点
     *
     * @param path
     */
    public static void deleteNode(String path) throws Exception {
        if (zooKeeper.exists(path,true) != null) {
            zooKeeper.delete(path, -1);
        }
    }

    /**
     * 节点是否存在
     * @param path
     * @return
     */
    public static boolean exists(String path) throws Exception {
        return zooKeeper.exists(path,true) == null;
    }

    /**
     * 获取某个路径下孩子的数量
     *
     * @param path
     * @return
     */
    public static Integer getChildrenNum(String path) throws Exception {
        return zooKeeper.getChildren(path,true).size();
    }

    /**
     * 关闭连接
     *
     */
    public static void closeConnection() throws Exception {
        if (zooKeeper != null) {
            zooKeeper.close();
            zooKeeper = null;
        }
    }
}
