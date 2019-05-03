package com.mars.cloud.core.watcher;

import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.helper.ZkHelper;
import com.mars.core.logger.MarsLogger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * 监视器
 */
public class ZkWatcher implements Watcher {

    private MarsLogger marsLogger = MarsLogger.getLogger(ZkWatcher.class);

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
        try {
            if (event.getState().equals(Event.KeeperState.SyncConnected)) {
                countDownLatch.countDown();
            }
            if (event.getState().equals(Event.KeeperState.Expired) || event.getState().equals(Event.KeeperState.Disconnected)) {
                for(int i=0;i<10;i++){
                    try {
                        ZkHelper.closeConnection();
                        ZkHelper.openConnection();
                        marsLogger.info("Zookeeper的Session失效，已重连");
                        break;
                    } catch (Exception e){
                        if(i >= 9){
                            marsLogger.error("Zookeeper的Session失效，重连失败",e);
                        }
                    }
                }
            }
            if(event.getType().equals(Event.EventType.NodeCreated)) {
                String path = event.getPath();
                String path2 = path.replace(CloudConstant.BASE_SERVER_NODE+"/","");
                int len = path2.split("/").length;
                if(!path.equals(CloudConstant.BASE_SERVER_NODE) && len >= 2){
                    marsLogger.info("接口[" + ZkHelper.getData(path) + "]注册成功");
                }
            }
        } catch (Exception e){

        }
    }
}
