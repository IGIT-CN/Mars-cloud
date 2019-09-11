package com.mars.cloud.core.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsRPC {

    /**
     * 服务名称
     * @return
     */
    String name() default "";
}
