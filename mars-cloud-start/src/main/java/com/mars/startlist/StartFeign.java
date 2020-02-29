package com.mars.startlist;

import com.mars.cloud.feign.load.LoadCloudFeign;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

/**
 * 加载Feign
 */
public class StartFeign implements StartMap {

    /**
     * 加载Feign
     * @param startParam 参数
     * @throws Exception 异常
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        LoadCloudFeign.LoadCloudFeign();
    }
}
