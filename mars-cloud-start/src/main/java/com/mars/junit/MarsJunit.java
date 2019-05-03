package com.mars.junit;

import com.mars.cloud.registered.Registered;
import com.mars.mvc.load.LoadController;
import com.mars.mybatis.init.InitJdbc;

import java.util.ArrayList;
import java.util.List;

/**
 * junit
 */
public abstract class MarsJunit {

    /**
     * 加载项目启动的必要数据
     * @param packName
     */
    public void init(String packName){
        List<StartList> list = new ArrayList<>();
        list.add(new LoadCloud());
        MarsJunitStart.start(new InitJdbc(),packName,this,list);
    }

    /**
     * 单测开始前
     */
    public abstract void before();


    /**
     * 注册接口
     */
    class LoadCloud implements StartList {
        @Override
        public void load() throws Exception {
            LoadController.loadContrl();
            Registered.register(0);
        }
    }
}
