package com.mars.cloud.listener;

import com.mars.cloud.core.cache.CacheDAO;
import com.mars.cloud.registered.Registered;

/**
 * 监听器,由Watcher触发
 */
public class Listener {

    /**
     * 重新注册接口,在watcher里反射调用的
     * @throws Exception
     */
    public void reConnectionZookeeper() throws Exception {
        Registered.register();
        CacheDAO.clear();
    }
}
