package com.mars.junit;

import com.mars.cloud.core.config.MarsCloudConfig;
import com.mars.core.annotation.MarsTest;
import com.mars.core.util.MarsConfiguration;
import com.mars.jdbc.load.InitJdbc;
import com.mars.start.base.BaseJunit;
import com.mars.start.base.MarsJunitStart;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.impl.*;
import com.mars.startlist.StartAddClass;
import com.mars.startlist.StartFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * junit
 */
public abstract class MarsJunit {

    private Logger logger = LoggerFactory.getLogger(BaseJunit.class);

    /**
     * 加载项目启动的必要数据
     *
     * @param packName 包名
     */
    public void init(Class packName) {

        Map<Integer, StartMap> startList = new HashMap<>();
        startList.put(0, new StartLoadClass());
        startList.put(1, new StartAddClass());
        startList.put(2, new StartBeans());
        startList.put(3, new StartJDBC());
        startList.put(4, new StartFeign());
        startList.put(5, new StartBeanObject());
        startList.put(6, new HasStart());
        startList.put(7, new StartMarsTimer());
        startList.put(8, new StartLoadAfter());
        startList.put(9, new StartExecuteTimer());

        MarsJunitStart.setStartList(startList);
        MarsJunitStart.start(new InitJdbc(), packName, this);
    }

    /**
     * 获取配置信息
     * @return
     */
    public abstract MarsCloudConfig getMarsConfig();

    /**
     * 加载项目启动的必要数据
     *
     */
    public MarsJunit() {
        MarsConfiguration.loadConfig(getMarsConfig());

        MarsTest marsTest = this.getClass().getAnnotation(MarsTest.class);
        if(marsTest == null || marsTest.startClass() == null){
            logger.error("没有正确的配置MarsTest注解");
        } else {
            init(marsTest.startClass());
        }
    }
}
