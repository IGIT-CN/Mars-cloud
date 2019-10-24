package com.mars.cloud.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mars.core.enums.DataType;



/**
 * 类型转化工具类
 */
public class TypeConverUtil {

    /**
     * 将val转化成cls类型
     *
     * @param val 对象
     * @param cls 类型
     * @return 结果
     * @throws Exception 异常
     */
    public static Object conver(String val, Class<?> cls) throws Exception {
        try {
            String typeName = cls.getSimpleName().toUpperCase();
            switch (typeName) {
                case DataType.INT:
                case DataType.INTEGER:
                    return Integer.parseInt(val);
                case DataType.BYTE:
                    return Byte.parseByte(val);
                case DataType.DOUBLE:
                    return Double.parseDouble(val);
                case DataType.FLOAT:
                    return Float.parseFloat(val);
                case DataType.LONG:
                    return Long.parseLong(val);
                case DataType.SHORT:
                    return Short.parseShort(val);
                case DataType.BOOLEAN:
                    return Boolean.parseBoolean(val);
                case DataType.CHAR:
                case DataType.CHARACTER:
                case DataType.STRING:
                    return val;
                default:
                    return converToObject(val,cls);
            }
        } catch (Exception e) {
            throw new Exception("请求成功，但是将返回数据转化成" + cls.getName() + "时失败", e);
        }
    }

    /**
     * 转化成对应的Object
     * @param val 源字符串
     * @param cls 转化后的类型
     * @return
     */
    private static Object converToObject(String val, Class<?> cls){
        if(JSON.isValidObject(val)){
            JSONObject jsonObject = JSONObject.parseObject(val);
            return jsonObject.toJavaObject(cls);
        } else if(JSON.isValidArray(val)){
            JSONArray jsonArray = JSONArray.parseArray(val);
            return jsonArray.toJavaList(cls);
        }
        return null;
    }
}
