package com.jsl.oa.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 业务切面
 * <hr/>
 * 对业务进行切入，进行对内容操作的预处理
 *
 * @since v1.2.0
 * @version v1.2.0
 * @author xiao_lfeng
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BusinessAop {

    /**
     * 在控制器的所有方法执行前执行
     *
     * @param joinPoint 切入点提供对方法执行的信息
     */
    @Before("execution(* com.jsl.oa.controllers.*.*(..))")
    public void beforeController(@NotNull JoinPoint joinPoint) {
        // 从ServletRequest中获取用户信息
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            // 获取方法签名
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Class<?> declaringType = methodSignature.getDeclaringType();
            String methodName = methodSignature.getName();

            log.info(
                    "[CONTROL] 执行 {}:{} 接口 | 地址: [{}]{}",
                    declaringType.getName(),
                    methodName,
                    request.getMethod(),
                    request.getServletPath()
            );
        } else {
            throw new RuntimeException("无法获取信息");
        }
    }

    /**
     * 在服务的所有方法执行前执行
     *
     * @param joinPoint 切入点提供对方法执行的信息
     */
    @Before("execution(* com.jsl.oa.services.impl.*.*(..))")
    public void beforeService(@NotNull JoinPoint joinPoint) {
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?> declaringType = methodSignature.getDeclaringType();
        String methodName = methodSignature.getName();

        log.info("[SERVICE] 执行 {}:{} 业务", declaringType.getName(), methodName);
    }

    /**
     * 在DAO的所有方法执行前执行
     *
     * @param pjp 切入点提供对方法执行的信息
     */
    @Around("execution(* com.jsl.oa.dao.*.*(..))")
    public Object beforeDao(@NotNull ProceedingJoinPoint pjp) throws Throwable {
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Class<?> declaringType = methodSignature.getDeclaringType();
        String methodName = methodSignature.getName();
        Object[] args = pjp.getArgs();
        log.info("==>[DAO] 操作 {}:{} 记录", declaringType.getName(), methodName);
        if (args.length != 0) {
            log.debug("\t> 传入信息：{}", Arrays.toString(args));
        }
        Object result = pjp.proceed();
        log.info("<==[DAO] 返回数据类型 {}", declaringType.descriptorString());
        log.debug("\t> 传出信息：{}", result.toString());
        return result;
    }
}
