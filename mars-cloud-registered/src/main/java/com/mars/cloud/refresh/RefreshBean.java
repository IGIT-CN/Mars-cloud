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
@MarsBean("refreshBean")
public class RefreshBean {

    private Logger logger = LoggerFactory.getLogger(RefreshBean.class);

    private RefreshManager refreshManager = new RefreshManager();

    /**
     * 刷新本地缓存的接口
     */
    @MarsTimer(loop = 10000)
    public void RefreshCacheApi(){
        try {
            Map<String, List<String>> urlMap = refreshManager.refreshCacheApi();
            CacheApi.getCacheApi().save(urlMap);
        } catch (Exception e){
            logger.error("本地缓存的接口刷新失败，10秒后将再次刷新........................",e);
        }
    }
}
