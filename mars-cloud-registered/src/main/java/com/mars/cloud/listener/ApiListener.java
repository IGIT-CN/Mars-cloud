package com.mars.cloud.listener;

import com.mars.cloud.core.load.LoadCloudApis;
import com.mars.cloud.core.model.UrlListModel;
import com.mars.cloud.core.load.LoadServerList;
import com.mars.core.logger.MarsLogger;

import java.util.Date;
import java.util.Map;

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
        apiListener.new Listener().start();
    }


    /**
     * 轮询监听，每隔10秒更新一下本地接口缓存
     */
    private class Listener extends Thread {
        @Override
        public void run() {

            while (true){
                try {
                    synchronized (this) {
                        wait(10000);
                        Map<String,UrlListModel> urlListModelMap = LoadCloudApis.init();
                        LoadServerList.replace(urlListModelMap);
                    }
                } catch (Exception e){
                    continue;
                }
            }
        }
    }
}
