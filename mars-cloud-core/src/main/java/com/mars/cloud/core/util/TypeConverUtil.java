package com.mars.cloud.core.util;

import com.alibaba.fastjson.JSONObject;
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

        String val = obj.toString();

        String typeName = cls.getSimpleName().toUpperCase();
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
               throw new Exception("基础类型与包装器类型，请统一用String.class");
            default:
                JSONObject jsonObject = JSONObject.parseObject(val);
                return jsonObject.toJavaObject(cls);
        }
    }


    /**
     * 转化成Map
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String,Object> conver(Object obj) throws Exception {
        try {
            Map<String,Object> params = new HashMap<>();

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
                    params.put(MarsCloudConstant.PARAM,obj);
                    break;
                default:
                    params = toMap(obj);
                    break;
            }
            return params;
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
