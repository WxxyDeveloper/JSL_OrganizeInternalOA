package com.jsl.oa.aspect;

import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.exception.library.NotLoginException;
import com.jsl.oa.exception.library.UserCanntUse;
import com.jsl.oa.model.dodata.UserDO;
import com.jsl.oa.utils.Processing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 检查用户权限切面
 * <hr/>
 * 检查用户能否正常使用，在用户使用之前进行可用性检查
 *
 * @since v1.2.0
 * @version v1.2.0
 * @author xiao_lfeng
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CheckUserAbleToUseAspect {

    private final UserDAO userDAO;

    @Around("@annotation(com.jsl.oa.annotations.NeedPermission)")
    public Object checkUse(ProceedingJoinPoint pjp) throws Throwable {
        // 从ServletRequest中获取用户信息
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes != null) {
            // 获取用户
            Long getUserId = Processing.getAuthHeaderToUserId(servletRequestAttributes.getRequest());
            if (getUserId == null) {
                throw new NotLoginException("用户信息不存在");
            }

            // 获取用户详细信息
            UserDO getUser = userDAO.getUserById(getUserId);
            if (getUser != null) {
                // 用户是否被禁用
                if (!getUser.getEnabled()) {
                    throw new UserCanntUse("用户未启用");
                }
                // 用户是否被封禁
                if (!getUser.getAccountNoLocked()) {
                    throw new UserCanntUse("用户被封禁");
                }
                // 用户是否被删除
                if (getUser.getIsDelete()) {
                    throw new UserCanntUse("用户被删除");
                }
                // 用户是否过期
                if (!getUser.getAccountNoExpired()) {
                    throw new UserCanntUse("用户已过期");
                }

                return pjp.proceed();
            } else {
                throw new NotLoginException("用户信息不存在");
            }
        } else {
            throw new RuntimeException("无法获取信息");
        }
    }
}
