package com.mars.cloud.listener;

import com.mars.cloud.core.helper.ZkHelper;
import com.mars.cloud.core.load.LoadCloudApis;
import com.mars.cloud.core.model.UrlListModel;
import com.mars.cloud.core.load.LoadServerList;
import com.mars.cloud.registered.Registered;
import com.mars.core.logger.MarsLogger;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 轮询更新本地API缓存
 */
public class ApiListener {

    private static MarsLogger marsLogger = MarsLogger.getLogger(ApiListener.class);

    /**
     * 开始监听
     */
    public static void startListener(){
        ApiListener apiListener = new ApiListener();
        apiListener.new Listener().doListener();
    }

    /**
     * 轮询监听，每隔10秒更新一下本地接口缓存
     */
    private class Listener {

        private Map<String,UrlListModel> urlListModelMap;

        public void doListener() {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        /* 检查是否处于连接状态 */
                        boolean result = ZkHelper.hasConnection();
                        if(!result){
                            marsLogger.info("zookeeper连接已断开，正在重新注册接口");
                            /* 不是连接状态，那就重新注册接口 */
                            Registered.register(1);
                            marsLogger.info("接口重新注册成功");
                        }
                        /* 从zk更新接口到本地 */
                        urlListModelMap = LoadCloudApis.init();
                        if(urlListModelMap != null){
                            LoadServerList.replace(urlListModelMap);
                        }
                    } catch (Exception e){
                        marsLogger.error("刷新本地缓存失败，即将重试刷新",e);
                    }
                }
            }, new Date(),10000);
        }
    }
}
