package com.mars.junit;

import com.mars.cloud.registered.Registered;
import com.mars.mvc.load.LoadController;
import com.mars.mybatis.init.InitJdbc;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

/**
 * junit
 */
public abstract class MarsJunit {

    /**
     * 加载项目启动的必要数据
     *
     * @param packName 包名
     */
    public void init(String packName, String suffix) {
        List<StartList> list = new ArrayList<>();
        list.add(new LoadCloud());
        MarsJunitStart.start(new InitJdbc(), packName, this, list, suffix);
    }

    /**
     * 加载项目启动的必要数据
     *
     * @param packName 包名
     */
    public void init(String packName) {
        init(packName, null);
    }

    /**
     * 单测开始前
     */
    @Before
    public abstract void before();

    /**
     * 注册接口
     */
    class LoadCloud implements StartList {
        @Override
        public void load() throws Exception {
            LoadController.loadContrl();
            Registered.register();
        }
    }
}
