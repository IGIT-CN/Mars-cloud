package com.mars.cloud.util;

import com.alibaba.fastjson.JSON;
import com.mars.cloud.core.constant.CloudConstant;
import com.mars.core.constant.MarsConstant;
import com.mars.core.enums.DataType;
import com.mars.core.util.SerializableUtil;
import com.mars.netty.par.base.BaseParamAndResult;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.server.server.request.model.MarsFileUpLoad;

import java.lang.reflect.Method;

public class CloudParamAndResult implements BaseParamAndResult {

    /**
     * 获取参数
     * @param method 方法
     * @param request 请求
     * @param response 响应
     * @return 参数
     * @throws Exception 异常
     */
    @Override
    public Object[] getParam(Method method, HttpMarsRequest request, HttpMarsResponse response) throws Exception {
        try {
            Class[] paramTypes = method.getParameterTypes();
            if(paramTypes == null || paramTypes.length < 1){
                return null;
            }
            Object[] params = new Object[paramTypes.length];
            for(int i = 0;i<paramTypes.length;i++){
                Class cls = paramTypes[i];
                params[i] = getObject(cls,request);
            }
            return params;
        } catch (Exception e){
            throw new Exception("参数注入异常",e);
        }
    }

    /**
     * 返回数据
     * @param response 响应
     * @param resultObj 返回的数据
     * @throws Exception 异常
     */
    @Override
    public void result(HttpMarsResponse response, Object resultObj) throws Exception {
        if(!isNotObject(resultObj)) {
            resultObj = JSON.toJSONString(resultObj);
        } else if(resultObj != null && resultObj.toString().equals(MarsConstant.VOID)) {
            throw new Exception("API的返回类型不可以为void");
        }
        // 后期改成序列化后 返回
        response.send(String.valueOf(resultObj));
    }

    /**
     * 获取参数并反序列化
     * @param cls 类型
     * @param request 请求
     * @return 对象
     * @throws Exception 异常
     */
    private Object getObject(Class cls, HttpMarsRequest request) throws Exception {
        MarsFileUpLoad marsFileUpLoad = request.getFile(CloudConstant.PARAM);
        if(marsFileUpLoad == null){
            return null;
        }
        byte[] bytes = marsFileUpLoad.getBytes();
        return SerializableUtil.deSerialization(bytes, cls);
    }

    /**
     * 判断是否是对象
     * @param result
     * @return
     */
    private boolean isNotObject(Object result){
        if(result == null){
            return true;
        }
        String fieldTypeName = result.getClass().getSimpleName().toUpperCase();
        switch (fieldTypeName){
            case DataType.INT:
            case DataType.INTEGER:
            case DataType.BYTE:
            case DataType.STRING:
            case DataType.CHAR:
            case DataType.CHARACTER:
            case DataType.DOUBLE:
            case DataType.FLOAT:
            case DataType.LONG:
            case DataType.SHORT:
            case DataType.BOOLEAN:
            case DataType.DATE:
                return true;
        }
        return false;
    }
}
