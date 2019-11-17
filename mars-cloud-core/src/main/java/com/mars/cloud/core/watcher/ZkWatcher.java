package com.mars.cloud.core.watcher;

import com.mars.cloud.core.cache.CacheApi;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
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

            /* 注册中心的接口有了变化，就刷新本地缓存 */
            switch (event.getType()){
                case NodeChildrenChanged:
                case NodeCreated:
                case NodeDeleted:
                case NodeDataChanged:
                    refreshCacheApi();
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

                Class cls = Class.forName("com.mars.cloud.refresh.RefreshManager");
                Method method = cls.getMethod("reConnectionZookeeper");
                method.invoke(cls.getDeclaredConstructor().newInstance());

                return;
            } catch (Exception e) {
                marsLogger.error("zookeeper重连失败，即将重试", e);
                continue;
            }
        }
    }

    /**
     * 刷新本地API缓存
     */
    private void refreshCacheApi() {
        try {
            Class cls = Class.forName("com.mars.cloud.refresh.RefreshManager");
            Method method = cls.getMethod("refreshCacheApi");
            Object result =  method.invoke(cls.getDeclaredConstructor().newInstance());
            if(result != null){
                Map<String, List<String>> urlMap = (Map<String, List<String>>)result;
                CacheApi.getCacheApi().save(urlMap);
            }
            return;
        } catch (Exception e) {
            /*
             * 如果出异常了，由于被捕获，所以程序不会停掉
             * 而且还有一个定时任务在做补偿，15秒后会再刷新一次
             * 所以这里什么都不干，默默的就好
             */
        }
    }
}
