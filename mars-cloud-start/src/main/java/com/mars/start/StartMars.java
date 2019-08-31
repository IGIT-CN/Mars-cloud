package com.mars.start;

import com.mars.cloud.registered.Registered;
import com.mars.junit.StartList;
import com.mars.mybatis.init.InitJdbc;
import com.mars.start.base.BaseStartMars;

import java.util.ArrayList;
import java.util.List;

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
        if (args != null && args[0] != null) {
            BaseStartMars.start(clazz, new InitJdbc(), args[0], initCloud());
        } else {
            BaseStartMars.start(clazz, new InitJdbc(), null, initCloud());
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


    /**
     * 加载cloud需要的数据
     *
     * @throws Exception 异常
     */
    private static List<StartList> initCloud() {
        List<StartList> startList = new ArrayList<>();

        StartList item = new StartList() {
            @Override
            public void load() throws Exception {
                Registered.register();
            }
        };

        startList.add(item);
        return startList;
    }
}
