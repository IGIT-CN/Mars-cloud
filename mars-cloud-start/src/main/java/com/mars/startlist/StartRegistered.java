package com.mars.startlist;

import com.mars.cloud.core.util.CloudConfigUtil;
import com.mars.cloud.registered.Registered;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

/**
 * 注册接口
 */
public class StartRegistered implements StartMap {

    /**
     * 注册接口
     *
     * @param startParam 参数
     * @throws Exception 异常
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        Object object = CloudConfigUtil.getCloudConfig("gateWay");
        if(object != null && (object.toString().equals("yes") || object.toString().equals("true"))){
            return;
        }
        Registered.register();
    }
}
