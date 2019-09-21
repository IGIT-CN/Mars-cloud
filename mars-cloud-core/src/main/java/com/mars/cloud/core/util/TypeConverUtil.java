package com.mars.cloud.core.util;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.enums.DataType;


/**
 * 类型转化工具类
 */
public class TypeConverUtil {

    /**
     * 将obj转化成cls类型
     *
     * @param obj 对象
     * @param cls 类型
     * @param <T> 类型
     * @return 结果
     * @throws Exception 异常
     */
    public static <T> T conver(Object obj, Class<T> cls) throws Exception {
        try {
            String val = obj.toString();

            String typeName = cls.getSimpleName().toUpperCase();
            switch (typeName) {
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
                    throw new Exception("不支持将返回数据转化成" + cls.getName() + "类型，请使用String.class或者其他对象");
                case DataType.STRING:
                    return (T) val;
                default:
                    JSONObject jsonObject = JSONObject.parseObject(val);
                    return jsonObject.toJavaObject(cls);
            }
        } catch (Exception e) {
            throw new Exception("请求成功，但是将返回数据转化成" + cls.getName() + "时失败", e);
        }
    }
}
