package com.jsl.oa.aspect;

import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * <h1>用户控制器切面</h1>
 * <hr/>
 * 用于用户控制器的切面
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author 筱锋xiao_lfeng
 */
@Aspect
@Component
public class UserControllerAspect {

    /**
     * <h1>用户控制器切面</h1>
     * <hr/>
     * 用于用户控制器的切面
     *
     * @since v1.0.0
     * @param pjp ProceedingJoinPoint对象
     * @return {@link Object}
     * @throws Throwable 异常
     */
    @Around("execution(* com.jsl.oa.controllers.UserController.*(..))")
    public Object controllerAround(ProceedingJoinPoint pjp) throws Throwable {
        // 获取HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // 时间戳检查
        if (checkTimestamp(request)) {
            // TODO: 2023/12/21 0001 后期固定业务（如：日志处理）
            return pjp.proceed();
        } else {
            return ResultUtil.error(ErrorCode.TIMESTAMP_ERROR);
        }

    }

    /**
     * <h1>时间戳检查</h1>
     * <hr/>
     * 用于检查时间戳是否合法，合法时间范围正负5秒
     *
     * @since v1.0.0
     * @param request HttpServletRequest对象
     * @return {@link Boolean}
     */
    public Boolean checkTimestamp(HttpServletRequest request) {
        // 获取请求头中的时间戳
        String getTimestamp = request.getHeader("Timestamp");
        // 判断是否为空
        if (getTimestamp == null || getTimestamp.isEmpty()) {
            return false;
        } else {
            if (getTimestamp.length() == 10) {
                getTimestamp += "000";
            }
        }
        // 获取当前时间戳
        long nowTimestamp = System.currentTimeMillis();

        // 时间误差允许前后五秒钟
        return nowTimestamp - Long.parseLong(getTimestamp) <= 5000 && nowTimestamp - Long.parseLong(getTimestamp) >= -5000;
    }
}
