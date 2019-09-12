package com.mars.cloud.rpc.proxy;

import com.mars.cloud.core.annotations.MarsFeign;
import com.mars.cloud.request.MarsRestTemplate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

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

        if(marsFeign == null){
            throw new Exception("接口上缺少MarsFeign注解:["+cls.getName()+"."+method.getName()+"]");
        }

        Object param = null;
        if(args != null && args.length > 0){
            if (args.length > 1){
                throw new Exception("Feign的方法只允许有一个参数");
            }
            param = args[0];
        }

        return MarsRestTemplate.request(marsFeign.serverName(),method.getName(),param,method.getReturnType());
    }
}
