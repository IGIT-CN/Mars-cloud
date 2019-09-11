package com.mars.cloud.core.annotations;

import com.mars.core.annotation.enums.RequestMetohd;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsAPI {

    /**
     * 接口名称
     * @return
     */
    RequestMetohd method() default RequestMetohd.GET;

}
