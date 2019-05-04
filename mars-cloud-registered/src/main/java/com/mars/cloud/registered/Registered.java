package com.mars.cloud.registered;

import com.mars.cloud.core.constant.CloudConstant;
import com.mars.cloud.core.helper.ZkHelper;
import com.mars.cloud.core.util.CloudConfigUtil;
import com.mars.cloud.core.util.CloudUtil;
import com.mars.cloud.listener.ApiListener;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.logger.MarsLogger;
import com.mars.mvc.model.MarsMappingModel;

import java.util.Map;

/**
 * 注册接口
 */
public class Registered {

    private static MarsLogger marsLogger = MarsLogger.getLogger(Registered.class);

    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 发布注册接口
     *
     * @param state 0 第一次注册，1 离线后重新注册
     * @throws Exception 异常
     */
    public static void register(int state) throws Exception {
        try {

            /* 打开zookeeper连接 */
            ZkHelper.openConnection();

            marsLogger.info("接口注册中.......");

            /* 获取本服务的名称 */
            String serverName = CloudConfigUtil.getCloudName();
            /* 获取本服务IP */
            String ip = CloudUtil.getLocalIp();
            /* 获取本服务端口 */
            String port = CloudUtil.getPort();

            /* 将本服务的接口发布注册到zookeeper */
            Map<String, MarsMappingModel> maps = getControllers();

            /* 注册接口 */
            for(String methodName : maps.keySet()){

                String node = CloudConstant.API_SERVER_NODE
                        .replace("{serverName}",serverName)
                        .replace("{method}",methodName)
                        .replace("{ip}",ip)
                        .replace("{port}",port);

                /* 将本服务的接口已写入zookeeper */
                ZkHelper.createNodes(node,CloudUtil.getLocalHost()+"/"+methodName);

                marsLogger.info("接口[" + serverName + "->" + methodName + "]注册成功");
            }
            if(state == 0){
                /* 如果是第一次注册，就启动接口监听器，进行轮询 */
                ApiListener.startListener();
            }
        } catch (Exception e){
            throw new Exception("注册与发布接口失败",e);
        }
    }

    /**
     * 获取所有的controller对象
     * @return 所有的controller对象
     */
    private static Map<String,MarsMappingModel> getControllers() {
        Map<String,MarsMappingModel> controlObjects = null;
        Object obj = constants.getAttr(MarsConstant.CONTROLLER_OBJECTS);
        if(obj != null) {
            controlObjects = (Map<String,MarsMappingModel>)obj;
        }
        return controlObjects;
    }
}
