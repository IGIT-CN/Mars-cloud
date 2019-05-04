package com.mars.cloud.core.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * 监视器
 */
public class ZkWatcher implements Watcher {

    private CountDownLatch countDownLatch;

    public ZkWatcher(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    /**
     * 监视zk的状态
     * @param event
     */
    @Override
    public void process(WatchedEvent event) {
        if (event.getState().equals(Event.KeeperState.SyncConnected)) {
            countDownLatch.countDown();
        }
    }
}
