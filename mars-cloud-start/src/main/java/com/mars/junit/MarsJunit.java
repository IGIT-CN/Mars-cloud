package com.mars.junit;

import com.mars.core.annotation.MarsTest;
import com.mars.mj.init.InitJdbc;
import com.mars.start.base.MarsJunitStart;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.impl.*;
import com.mars.startlist.StartAddClass;
import com.mars.startlist.StartFeign;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

/**
 * junit
 */
public abstract class MarsJunit {

    /**
     * 加载项目启动的必要数据
     *
     * @param packName 包名
     */
    public void init(Class packName, String suffix) {

        Map<Integer, StartMap> startList = new HashMap<>();
        startList.put(0, new StartConfig());
        startList.put(1, new StartLoadClass());
        startList.put(2, new StartAddClass());
        startList.put(3, new StartBeans());
        startList.put(4, new StartJDBC());
        startList.put(5, new StartFeign());
        startList.put(6, new StartBeanObject());
        startList.put(7, new HasStart());
        startList.put(8, new StartMarsTimer());
        startList.put(9, new StartLoadAfter());
        startList.put(10, new StartExecuteTimer());

        if(suffix != null && suffix.equals("")){
            suffix = null;
        }

        MarsJunitStart.setStartList(startList);
        MarsJunitStart.start(new InitJdbc(), packName, this, suffix);
    }

    /**
     * 加载项目启动的必要数据
     *
     */
    public MarsJunit() {
        MarsTest marsTest = this.getClass().getAnnotation(MarsTest.class);
        init(marsTest.testClass(), marsTest.config());
    }
}
