package com.mars.startlist;

import com.mars.cloud.util.CloudParamAndResult;
import com.mars.netty.par.factory.ParamAndResultFactory;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

/**
 * 指定用来处理参数和返回的对象实例
 */
public class StartLoadParamAndResult implements StartMap {

    @Override
    public void load(StartParam startParam) throws Exception {
        ParamAndResultFactory.setBaseParamAndResult(new CloudParamAndResult());
    }
}
