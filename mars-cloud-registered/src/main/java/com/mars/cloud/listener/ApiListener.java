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

        /**
         * 防止GC不回收，
         * 当run里面每循环一次，这个map引用的对象都会变
         * 而之前引用的对象将会因为没有变量引用它，而被回收
         *
         * 如果写在run里面，run里面是个死循环，这个方法永远不会执行结束
         * 所以栈帧一直存在，导致里面局部变量一直存在，创建的对象一直被引用这而不会被回收
         *
         * 以上都是我猜的，为了安全起见
         */
        private Map<String,UrlListModel> urlListModelMap;

        @Override
        public void run() {
            while (true){
                try {
                    synchronized (this) {
                        wait(10000);
                        urlListModelMap = LoadCloudApis.init();
                        if(urlListModelMap != null){
                            LoadServerList.replace(urlListModelMap);
                        }
                    }
                } catch (Exception e){
                    continue;
                }
            }
        }
    }
}
