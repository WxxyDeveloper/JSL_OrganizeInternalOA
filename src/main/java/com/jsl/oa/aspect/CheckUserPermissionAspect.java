package com.jsl.oa.aspect;

import com.jsl.oa.annotations.NeedRoleGroup;
import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.exception.library.NotLoginException;
import com.jsl.oa.exception.library.PermissionDeniedException;
import com.jsl.oa.model.dodata.RoleDO;
import com.jsl.oa.utils.Processing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 检查用户权限切面
 * <hr/>
 * 检查访问的用户是否包含正确的访问权限，若用户有正确的访问权限则允许访问，若没有指定的权限将会返回错误的权限信息。
 *
 * @since v1.2.0
 * @version v1.2.0
 * @author xiao_lfeng
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CheckUserPermissionAspect {

    private final RoleDAO roleDAO;

    /**
     * 检查权限
     * <hr/>
     * 检查注解中填写的权限，只有当接口符合注解中的权限信息，才会实际进入业务，否则将会被拦截
     *
     * @param pjp {@link ProceedingJoinPoint}
     * @return {@link Object}
     */
    @Around("@annotation(com.jsl.oa.annotations.NeedRoleGroup)")
    public Object checkPermission(ProceedingJoinPoint pjp) throws Throwable {
        // 从ServletRequest中获取用户信息
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes != null) {
            // 获取用户
            Long getUserId = Processing.getAuthHeaderToUserId(servletRequestAttributes.getRequest());
            if (getUserId == null) {
                throw new NotLoginException("用户信息不存在");
            }
            // 获取方法签名
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            NeedRoleGroup checkAccountPermission = signature.getMethod().getAnnotation(NeedRoleGroup.class);
            String getRoleAtAnnotation = checkAccountPermission.value();

            // 获取用户所在权限组
            RoleDO getUserRole = roleDAO.getRoleNameByUid(getUserId);
            if (getUserRole != null) {
                if (getUserRole.getRoleName().equals(getRoleAtAnnotation)) {
                    return pjp.proceed();
                } else {
                    throw new PermissionDeniedException("用户组不匹配", getRoleAtAnnotation);
                }
            } else {
                throw new PermissionDeniedException("用户组不匹配", getRoleAtAnnotation);
            }
        } else {
            throw new RuntimeException("无法获取信息");
        }
    }
}
