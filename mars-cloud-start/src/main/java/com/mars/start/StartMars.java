package com.mars.start;

import com.mars.cloud.core.config.MarsCloudConfig;
import com.mars.core.util.MarsConfiguration;
import com.mars.jdbc.load.InitJdbc;
import com.mars.start.base.BaseStartMars;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.impl.*;
import com.mars.startlist.StartAddClass;
import com.mars.startlist.StartFeign;
import com.mars.startlist.StartLoadParamAndResult;
import com.mars.startlist.StartRegistered;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动Mars框架
 *
 * @author yuye
 */
public class StartMars {

    /**
     * 启动Mars框架
     *
     * @param clazz 类
     */
    public static void start(Class<?> clazz, MarsCloudConfig marsCloudConfig) {

        Map<Integer, StartMap> startList = new HashMap<>();

        startList.put(0, new StartCoreServlet());
        startList.put(1, new StartLoadClass());
        startList.put(2, new StartAddClass());
        startList.put(3, new StartBeans());
        startList.put(4, new StartJDBC());
        startList.put(5, new StartFeign());
        startList.put(6, new StartBeanObject());
        startList.put(7, new StartMarsApi());
        startList.put(8, new StartLoadParamAndResult());
        startList.put(9, new StartInter());
        startList.put(10, new HasStart());
        startList.put(11, new StartRegistered());
        startList.put(12, new StartMarsTimer());
        startList.put(13, new StartLoadAfter());
        startList.put(14, new StartExecuteTimer());

        BaseStartMars.setStartList(startList);

        if(marsCloudConfig != null){
            MarsConfiguration.loadConfig(marsCloudConfig);
        }

        BaseStartMars.start(clazz,new InitJdbc());
    }
}
