package com.mars.cloud.refresh;

import com.mars.cloud.core.cache.CacheApi;
import com.mars.core.annotation.MarsBean;
import com.mars.core.annotation.MarsTimer;

import java.util.List;
import java.util.Map;

/**
 * 刷新本地缓存的接口
 */
@MarsBean("refreshBean")
public class RefreshBean {

    private RefreshManager refreshManager = new RefreshManager();

    /**
     * 刷新本地缓存的接口
     */
    @MarsTimer(loop = 15000)
    public void RefreshCacheApi(){
        try {
            Map<String, List<String>> urlMap = refreshManager.refreshCacheApi();
            CacheApi.getCacheApi().save(urlMap);
        } catch (Exception e){
            /*
             * 如果出异常了，由于被捕获，所以程序不会停掉，15秒后会再执行一次，而且这个异常没有提示的意义
             * 所以这里什么都不干，让这个定时任务默默的就好
             */
        }
    }
}
