package com.mars.cloud.core.util;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.annotation.enums.RequestMetohd;
import com.mars.core.constant.MarsCloudConstant;
import com.mars.core.enums.DataType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型转化工具类
 */
public class TypeConverUtil {

    /**
     * 将obj转化成cls类型
     * @param obj
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T conver(Object obj,Class<T> cls) throws Exception {
        try{
            String val = obj.toString();

            String typeName = cls.getSimpleName().toUpperCase();
            switch (typeName){
                case DataType.INT:
                case DataType.INTEGER:
                case DataType.BYTE:
                case DataType.CHAR:
                case DataType.CHARACTER:
                case DataType.DOUBLE:
                case DataType.FLOAT:
                case DataType.LONG:
                case DataType.SHORT:
                case DataType.BOOLEAN:
                    throw new Exception("基础类型与包装器类型，请统一用String.class");
                case DataType.STRING:
                    return (T)val;
                default:
                    JSONObject jsonObject = JSONObject.parseObject(val);
                    return jsonObject.toJavaObject(cls);
            }
        } catch (Exception e){
            throw new Exception("请求成功，但是将返回数据转化成"+cls.getName()+"时失败",e);
        }
    }

    /**
     * 转化成Map
     * @param obj
     * @return
     * @throws Exception
     */
    public static Object conver(Object obj, RequestMetohd metohd) throws Exception {
        try {
            String typeName = obj.getClass().getSimpleName().toUpperCase();
            switch (typeName){
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
                    throw new Exception("传参必须是自定义对象或者Map，不可以是基础类型，包装器类型以及其他JDK自带的类型");
                default:
                    if(metohd.equals(RequestMetohd.GET)){
                        return toMap(obj);
                    } else{
                        return obj;
                    }
            }
        } catch (Exception e){
            throw new Exception("将参数转化成RestrRequest所需参数失败",e);
        }
    }

    /**
     * 转化成Map
     * @param obj
     * @return
     * @throws Exception
     */
    private static Map<String,Object> toMap(Object obj) throws Exception {

        Map<String,Object> params = new HashMap<>();

        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();

        for(Field f : fields){
            f.setAccessible(true);
            params.put(f.getName(),f.get(obj));
        }

        return params;
    }
}
