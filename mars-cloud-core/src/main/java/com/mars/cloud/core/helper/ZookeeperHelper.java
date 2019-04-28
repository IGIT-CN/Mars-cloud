package com.mars.cloud.core.helper;

import com.alibaba.fastjson.JSONObject;
import com.mars.cloud.core.util.CloudConfigUtil;
import com.mars.core.logger.MarsLogger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper帮助类
 */
public class ZookeeperHelper implements Watcher {

    private MarsLogger marsLogger = MarsLogger.getLogger(ZookeeperHelper.class);

    /**
     * zookeeper对象
     */
    private ZooKeeper zooKeeper;

    /**
     * session超时时间
     */
    private int sessionTimeout = 100000;

    /**
     * 注册中心地址
     */
    private String registeds;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 初始化
     *
     * @throws Exception
     */
    private void init() throws Exception {
        JSONObject config = CloudConfigUtil.getCloudConfig();

        registeds = config.getString("register");
        Object configTimeOut = config.get("sessionTimeout");
        if (configTimeOut != null) {
            sessionTimeout = Integer.parseInt(configTimeOut.toString());
        }
        countDownLatch = new CountDownLatch(1);
    }

    /**
     * 打开连接
     *
     * @throws Exception
     */
    public void openConnection() throws Exception {
        try {
            if (registeds == null) {
                init();
            }
            zooKeeper = new ZooKeeper(registeds, sessionTimeout, this);
            countDownLatch.await();
            marsLogger.info("连接zookeeper成功");
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
     * @throws Exception
     */
    public String createNodes(String path, String data) throws Exception {
        String[] pa = path.split("/");
        StringBuffer pat = new StringBuffer();
        for(int i=1;i<pa.length;i++){
            pat.append("/");
            pat.append(pa[i]);
            if(i < pa.length-1){
                createNode(pat.toString(),"init");
            } else {
                createNode(pat.toString(),data);
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
     * @throws Exception
     */
    public String createNode(String path, String data) throws Exception {
        Stat stat = zooKeeper.exists(path, true);
        if (stat == null) {
            return zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
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
     * @throws KeeperException
     * @throws InterruptedException
     */
    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(path, true);
        return children;
    }

    /**
     * 获取节点上面的数据
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getData(String path) throws KeeperException, InterruptedException {
        if (zooKeeper.exists(path, true) != null) {
            byte[] data = zooKeeper.getData(path, true, null);
            if (data == null) {
                return "";
            }
            return new String(data);
        }
        return "";
    }

    /**
     * 设置节点信息
     *
     * @param path
     * @param data
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public Stat setData(String path, String data) throws KeeperException, InterruptedException {
        if (zooKeeper.exists(path, true) != null) {
            Stat stat = zooKeeper.setData(path, data.getBytes(), -1);
            return stat;
        }
        return null;
    }

    /**
     * 删除节点
     *
     * @param path
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void deleteNode(String path) throws InterruptedException, KeeperException {
        if (zooKeeper.exists(path, true) != null) {
            zooKeeper.delete(path, -1);
        }
    }

    /**
     * 获取创建时间
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getCTime(String path) throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists(path, true);
        return String.valueOf(stat.getCtime());
    }

    /**
     * 获取某个路径下孩子的数量
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public Integer getChildrenNum(String path) throws KeeperException, InterruptedException {
        int childenNum = zooKeeper.getChildren(path, true).size();
        return childenNum;
    }

    /**
     * 关闭连接
     *
     * @throws InterruptedException
     */
    public void closeConnection() throws InterruptedException {
        if (zooKeeper != null) {
            zooKeeper.close();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            countDownLatch.countDown();
        }
    }
}
