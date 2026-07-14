package com.springbootLearning.annotation;

import java.lang.annotation.*;


/**
 * 日志记录注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogRequired {
    String value() default ""; // 注解默认传值字段名
    /**
     * 日志描述
     */
    String description() default ""; // 允许传入自定义的方法描述文本
}
