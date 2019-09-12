package com.mars.cloud.core.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

/**
 * 监视器
 */
public class ZkWatcher implements Watcher {

    private static Logger marsLogger = LoggerFactory.getLogger(ZkWatcher.class);

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
