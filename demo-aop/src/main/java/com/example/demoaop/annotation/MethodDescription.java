package com.example.demoaop.annotation;


import java.lang.annotation.*;

/**
 * 声明自定义注解
 */
@Target(ElementType.METHOD)          // 注解可以作用在方法上
@Retention(RetentionPolicy.RUNTIME) // 表示该注解在运行时仍然存在，这是能被 AOP 读取的前提。
@Documented
public @interface MethodDescription {
    /** 接口业务说明 */
    String value();

    /** 是否开启日志 */
    boolean enabled() default true;

    /** 是否打印参数 */
    boolean logParams() default true;

    /** 是否打印返回值 */
    boolean logResult() default true;

}
