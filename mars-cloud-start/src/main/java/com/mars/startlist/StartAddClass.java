package com.mars.startlist;

import com.mars.cloud.refresh.RefreshBean;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.load.LoadHelper;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

import java.util.Set;

/**
 * 添加框架自有Bean
 */
public class StartAddClass implements StartMap {

    /**
     * 添加框架自有Bean
     * @param startParam
     * @throws Exception
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        Set<String> classList = LoadHelper.getSacnClassList();
        classList.add(RefreshBean.class.getName());

        MarsSpace.getEasySpace().setAttr(MarsConstant.SCAN_ALL_CLASS,classList);
    }
}
