package com.mars.cloud.core.watcher;

import com.mars.cloud.core.cache.CacheManager;
import com.mars.cloud.core.helper.ZkHelper;
import com.mars.core.logger.MarsLogger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

/**
 * 监视器
 */
public class ZkWatcher implements Watcher {

    private static MarsLogger marsLogger = MarsLogger.getLogger(ZkWatcher.class);

    private CountDownLatch countDownLatch;

    public ZkWatcher(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    /**
     * 监视zk的状态
     *
     * @param event 事件
     */
    @Override
    public void process(WatchedEvent event) {
        try {
            switch (event.getState()) {
                case Expired:
                    /* 超时重连 */
                    reConnectionZookeeper();
                    break;
                case SyncConnected:
                    /* 连接成功 */
                    countDownLatch.countDown();
                    break;
            }

            /*
             * 节点变动监听
             * 这里调用getData方法是因为zk的watcher只有一次，一旦执行了就没了
             * 所以每次执行watcher以后，都要重新监听这个节点
             */
            switch (event.getType()) {
                case NodeDeleted:
                    ZkHelper.getData(event.getPath());
                    CacheManager.deleteUrlListModel(event.getPath());
                    break;
                case NodeChildrenChanged:
                case NodeDataChanged:
                    ZkHelper.getData(event.getPath());
                    CacheManager.changeUrlListModel(event.getPath());
                    break;
                case NodeCreated:
                    ZkHelper.getData(event.getPath());
                    CacheManager.addUrlListModel(event.getPath());
                    break;
            }
        } catch (Exception e) {
            marsLogger.error("处理zookeeper的通知时出错", e);
        }
    }

    /**
     * 重连zookeeper
     */
    private void reConnectionZookeeper() {
        while (true) {
            try {
                marsLogger.info("zookeeper连接已断开，正在重新连接并注册接口");

                Class cls = Class.forName("com.mars.cloud.reconnection.ReConnection");
                Method method = cls.getMethod("reConnectionZookeeper");
                method.invoke(cls.getDeclaredConstructor().newInstance());

                return;
            } catch (Exception e) {
                marsLogger.error("zookeeper重连失败，即将重试", e);
                continue;
            }
        }
    }
}
