package com.jsl.oa.aspect;

import com.jsl.oa.annotations.CheckUserAbleToUse;
import com.jsl.oa.annotations.CheckUserHasPermission;
import com.jsl.oa.dao.PermissionDAO;
import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.doData.RoleDO;
import com.jsl.oa.model.doData.RoleUserDO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * <h1>注解切面</h1>
 * <hr/>
 * 用于注解的切面
 *
 * @author xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AnnotationsAspect {
    private final RoleDAO roleDAO;
    private final UserDAO userDAO;
    private final PermissionDAO permissionDAO;

    /**
     * <h2>检查用户是否有权限</h2>
     * <hr/>
     * 检查用户是否有权限
     *
     * @param pjp ProceedingJoinPoint对象
     * @return {@link Object}
     * @throws Throwable 异常
     */
    @Around("@annotation(com.jsl.oa.annotations.CheckUserHasPermission)")
    public Object checkUserHasPermission(@NotNull ProceedingJoinPoint pjp) throws Throwable {
        log.info("用户权限检查");
        // 获取 HttpServletRequest 对象
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // 获取注解方法
        CheckUserHasPermission checkUserHasPermission = getCheckUserHasPermission(pjp);
        // 获取注解值
        String permissionName = null;
        boolean permissionCheck = true;
        if (checkUserHasPermission != null) {
            permissionName = checkUserHasPermission.value();
            permissionCheck = checkUserHasPermission.isCheck();
        }

        // 获取用户信息
        Long userId = Processing.getAuthHeaderToUserId(request);
        if (userId != null) {
            // 检查用户是否允许继续执行
            BaseResponse checkUserAbleToNext = checkUserAbleToNext(userId, userDAO.userMapper);
            if (checkUserAbleToNext != null) {
                return checkUserAbleToNext;
            } else {
                if (permissionCheck) {
                    // 检查用户权限
                    List<String> getPermission = permissionDAO.getPermission(userId);
                    // 匹配权限
                    if (getPermission.contains(permissionName)) {
                        return pjp.proceed();
                    } else {
                        log.info("\t> 用户权限不足，检查是否是管理员");
                        // 检查用户是管理员
                        RoleUserDO roleUserDO = roleDAO.roleMapper.getRoleUserByUid(Processing.getAuthHeaderToUserId(request));
                        if (roleUserDO != null) {
                            RoleDO roleDO = roleDAO.roleMapper.getRoleByRoleName("admin");
                            if (roleUserDO.getRid().equals(roleDO.getId())) {
                                return pjp.proceed();
                            } else {
                                return ResultUtil.error(ErrorCode.NOT_PERMISSION);
                            }
                        } else {
                            return ResultUtil.error(ErrorCode.NOT_ADMIN);
                        }
                    }
                } else {
                    return pjp.proceed();
                }
            }
        } else {
            return ResultUtil.error(ErrorCode.TOKEN_NOT_EXIST);
        }
    }

    @Around("@annotation(com.jsl.oa.annotations.CheckUserAbleToUse)")
    public Object checkUserAbleToUse(ProceedingJoinPoint pjp) throws Throwable {
        log.info("检查用户是否有权限继续");
        // 获取 HttpServletRequest 对象
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // 获取注解方法
        CheckUserAbleToUse check = getCheckUserAbleToUse(pjp);
        // 获取注解值
        assert check != null;

        // 获取用户信息
        Long userId = Processing.getAuthHeaderToUserId(request);
        UserDO userDO = userDAO.userMapper.getUserById(userId);
        // 用户不存在
        if (userDO == null) {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
        if (check.isCheckEnable()) {
            // 用户是否被禁用
            if (!userDO.getEnabled()) {
                return ResultUtil.error(ErrorCode.USER_DISABLED);
            }
        }
        if (check.isCheckLock()) {
            // 用户是否被封禁
            if (!userDO.getAccountNoLocked()) {
                return ResultUtil.error(ErrorCode.USER_LOCKED);
            }
        }
        if (check.isCheckDelete()) {
            // 用户是否被删除
            if (userDO.getIsDelete()) {
                return ResultUtil.error(ErrorCode.USER_ALREADY_DELETE);
            }
        }
        if (check.isCheckExpire()) {
            // 用户是否过期
            if (!userDO.getAccountNoExpired()) {
                return ResultUtil.error(ErrorCode.USER_EXPIRED);
            }
        }
        return pjp.proceed();
    }

    private @Nullable CheckUserHasPermission getCheckUserHasPermission(@NotNull ProceedingJoinPoint joinPoint) {
        // 获取方法对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 获取方法上的注解
        return (method != null) ? method.getAnnotation(CheckUserHasPermission.class) : null;
    }

    private @Nullable CheckUserAbleToUse getCheckUserAbleToUse(@NotNull ProceedingJoinPoint joinPoint) {
        // 获取方法对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 获取方法上的注解
        return (method != null) ? method.getAnnotation(CheckUserAbleToUse.class) : null;
    }

    private @Nullable BaseResponse checkUserAbleToNext(Long userId, @NotNull UserMapper userMapper) {
        log.info("\t> 检查用户是否有权限继续");
        // 获取用户信息
        UserDO userDO = userMapper.getUserById(userId);
        // 用户不存在
        if (userDO == null) {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
        // 用户是否被禁用
        if (!userDO.getEnabled()) {
            return ResultUtil.error(ErrorCode.USER_DISABLED);
        }
        // 用户是否被封禁
        if (!userDO.getAccountNoLocked()) {
            return ResultUtil.error(ErrorCode.USER_LOCKED);
        }
        // 用户是否被删除
        if (userDO.getIsDelete()) {
            return ResultUtil.error(ErrorCode.USER_ALREADY_DELETE);
        }
        // 用户是否过期
        if (!userDO.getAccountNoExpired()) {
            return ResultUtil.error(ErrorCode.USER_EXPIRED);
        }
        return null;
    }
}
