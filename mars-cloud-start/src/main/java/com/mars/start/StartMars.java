package com.mars.start;

import com.mars.mj.init.InitJdbc;
import com.mars.start.base.BaseStartMars;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.impl.*;
import com.mars.startlist.StartAddClass;
import com.mars.startlist.StartFeign;
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
    public static void start(Class<?> clazz, String[] args) {

        Map<Integer, StartMap> startList = new HashMap<>();

        startList.put(0, new StartCoreServlet());
        startList.put(1, new StartConfig());
        startList.put(2, new StartLoadClass());
        startList.put(3, new StartAddClass());
        startList.put(4, new StartBeans());
        startList.put(5, new StartJDBC());
        startList.put(6, new StartFeign());
        startList.put(7, new StartBeanObject());
        startList.put(8, new StartController());
        startList.put(9, new StartInter());
        startList.put(10, new HasStart());
        startList.put(11, new StartRegistered());
        startList.put(12, new StartMarsTimer());
        startList.put(13, new StartLoadAfter());
        startList.put(14, new StartExecuteTimer());

        BaseStartMars.setStartList(startList);
        if (args != null && args[0] != null) {
            BaseStartMars.start(clazz, new InitJdbc(), args[0]);
        } else {
            BaseStartMars.start(clazz, new InitJdbc(), null);
        }
    }

    /**
     * 启动Mars框架
     *
     * @param clazz 类
     */
    public static void start(Class<?> clazz) {
        start(clazz,null);
    }

}
