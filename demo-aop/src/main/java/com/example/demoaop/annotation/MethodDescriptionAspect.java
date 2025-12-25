package com.example.demoaop.annotation;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 实际执行自定义注解功能的切面
 */
@Aspect
@Component
@Slf4j
public class MethodDescriptionAspect {
    //拦截所有被 @MethodDescription 标注的方法
    @Around("@annotation(methodDescription)")
    public Object around(
            ProceedingJoinPoint joinPoint,
            MethodDescription methodDescription
    ) throws Throwable {

        //这里写需要执行的方法
        if (!methodDescription.enabled()) {
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 接口信息
        String desc = methodDescription.value();
        String methodName = method.getName();

        // 请求信息
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();

        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();

        // 参数
        Map<String, Object> params = new HashMap<>();
        if (methodDescription.logParams()) {
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < paramNames.length; i++) {
                params.put(paramNames[i], args[i]);
            }
        }

        log.info(
                "[API-START] desc={} | method={} | uri={} | params={}",
                desc,
                httpMethod,
                uri,
                toJson(params)
        );

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            log.error(
                    "[API-ERROR] desc={} | method={} | uri={} | params={} | error={}",
                    desc,
                    httpMethod,
                    uri,
                    toJson(params),
                    e.getMessage(),
                    e
            );

            throw e;
        }

        long cost = System.currentTimeMillis() - startTime;

        if (methodDescription.logResult()) {
            log.info(
                    "[API-END  ] desc={} | cost={}ms | result={}",
                    desc,
                    cost,
                    toJson(result)
            );

        } else {
            log.info(
                    "[API-END  ] desc={} | cost={}ms",
                    desc,
                    cost
            );

        }

        return result;
    }

    /** JSON 安全序列化 + 长度限制（生产级） */
    private String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        try {
            String json = JSON.toJSONString(obj);
            return limit(json, 2000); // 👈 统一限制长度
        } catch (Exception e) {
            return limit(String.valueOf(obj), 2000);
        }
    }

    /** 日志长度限制 */
    private String limit(String text, int maxLength) {
        if (text == null) {
            return null;
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...(truncated)";
    }

}