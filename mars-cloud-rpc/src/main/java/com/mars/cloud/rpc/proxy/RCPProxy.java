package com.mars.cloud.rpc.proxy;

import com.mars.cloud.core.annotations.MarsAPI;
import com.mars.cloud.core.annotations.MarsRPC;
import com.mars.cloud.request.MarsRest;
import com.mars.core.annotation.enums.RequestMetohd;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 用于实现RPC的代理类
 */
public class RCPProxy  implements MethodInterceptor {

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
        Object result = null;

        MarsRPC marsRPC = cls.getAnnotation(MarsRPC.class);

        MarsAPI marsAPI = method.getAnnotation(MarsAPI.class);

        if(marsRPC == null || marsAPI == null){
            throw new Exception("方法或者接口上缺少MarsRPC或者MarsAPI注解:["+cls.getName()+"."+method.getName()+"]");
        }
        if(marsAPI.method().equals(RequestMetohd.GET)){
            result = MarsRest.get(marsRPC.name(),method.getName(),method.getReturnType());
        } else if(marsAPI.method().equals(RequestMetohd.POST)){
            result = MarsRest.post(marsRPC.name(),method.getName(),method.getReturnType());
        }

        return result;
    }
}
