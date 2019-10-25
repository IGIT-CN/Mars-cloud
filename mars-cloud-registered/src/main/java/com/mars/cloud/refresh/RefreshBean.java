package com.mars.cloud.refresh;

import com.mars.cloud.core.cache.CacheApi;
import com.mars.core.annotation.MarsBean;
import com.mars.core.annotation.MarsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 刷新本地缓存的接口
 */
@MarsBean("RefreshBean")
public class RefreshBean {

    private Logger logger = LoggerFactory.getLogger(RefreshBean.class);

    private RefreshManager refreshManager = new RefreshManager();

    /**
     * 刷新本地缓存的接口
     */
    @MarsTimer(loop = 10000)
    public void RefreshCacheApi(){
        try {
            logger.info("开始刷新本地缓存的接口........................");

            Map<String, List<String>> urlMap = refreshManager.refreshCacheApi();
            CacheApi.getCacheApi().save(urlMap);

            logger.info("本地缓存的接口刷新完成........................");
        } catch (Exception e){
            logger.info("本地缓存的接口刷新失败，10秒后将再次刷新........................");
        }
    }
}
