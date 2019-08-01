package com.lsfly.commons;

import java.lang.annotation.*;

/**
 * Created by huoquan on 2017/10/10.
 * AOP日志记录，自定义注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {
    //模块名
    String moduleName() default "";
    /**
     * 日志描述
     */
    String description()  default "";
}
