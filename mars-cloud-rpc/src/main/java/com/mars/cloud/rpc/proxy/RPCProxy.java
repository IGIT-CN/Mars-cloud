package com.mars.cloud.rpc.proxy;

import com.mars.cloud.core.annotations.MarsFeign;
import com.mars.cloud.core.annotations.ResultType;
import com.mars.cloud.core.util.TypeConverUtil;
import com.mars.cloud.request.MarsRestTemplate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 用于实现RPC的代理类
 */
public class RPCProxy implements MethodInterceptor {

    private Enhancer enhancer;

    private Class<?> cls;

    /**
     * 获取代理对象
     * @param clazz  bean的class
     * @return 对象
     */
    public Object getProxy(Class<?> clazz) {
        cls = clazz;

        enhancer = new Enhancer();
        // 设置需要创建子类的类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        // 通过字节码技术动态创建子类实例
        return enhancer.create();
    }


    /**
     * 绑定代理
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        MarsFeign marsFeign = cls.getAnnotation(MarsFeign.class);

        check(marsFeign,method);

        Object param = getParam(args);

        String result = MarsRestTemplate.request(marsFeign.serverName(),method.getName(),param);

        return converResultData(result,method);
    }

    /**
     * 将返回值转化成指定类型
     * @param result
     * @param method
     * @return
     * @throws Exception
     */
    private Object converResultData(String result, Method method) throws Exception {
        Class returnType = method.getReturnType();
        if(returnType.equals(List.class)){
            ResultType resultType = method.getAnnotation(ResultType.class);
            if(resultType == null || resultType.type() == null){
                throw new Exception(method.getName()+"方法的返回类型是List，必须添加ResultType注解用来指定返回的类型");
            }
            returnType = resultType.type();
        }
        return TypeConverUtil.conver(result,returnType);
    }

    /**
     * 校验配置是否符合规则
     * @param marsFeign 注解
     * @param method 方法
     * @throws Exception 异常
     */
    private void check(MarsFeign marsFeign, Method method) throws Exception {
        if(marsFeign == null){
            throw new Exception("接口上缺少MarsFeign注解:["+cls.getName()+"."+method.getName()+"]");
        }
        if(method.getReturnType().getName().equals(String.class)){
            throw new Exception("MarsFeign的方法暂时只可以返回String类型");
        }
    }

    /**
     * 获取请求参数
     * @param args 参数
     * @return 参数
     * @throws Exception 异常
     */
    private Object getParam(Object[] args) throws Exception {
        if(args != null && args.length > 0){
            if (args.length > 1){
                throw new Exception("Feign的方法只允许有一个参数");
            }
            return args[0];
        }
        return null;
    }
}
