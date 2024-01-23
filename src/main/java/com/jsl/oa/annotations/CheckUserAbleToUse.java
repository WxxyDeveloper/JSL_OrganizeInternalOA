package com.jsl.oa.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>检查用户是否可用</h1>
 * <hr/>
 * 用于检查用户是否可用
 *
 * @version v1.1.0
 * @since v1.1.0
 * @see com.jsl.oa.aspect.AnnotationsAspect
 * @author xiao_lfeng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserAbleToUse {
    /**
     * <h2>是否启用</h2>
     * <hr/>
     * 用于指定是否启用<br/>
     * 请注意，禁用后任何用户权限校验不校验用户是否启用
     *
     * @return {@link Boolean}
     */
    boolean isCheckEnable() default true;

    /**
     * <h2>是否删除</h2>
     * <hr/>
     * 用于指定是否删除<br/>
     * 请注意，禁用后任何用户权限校验不校验用户是否删除
     *
     * @return {@link Boolean}
     */
    boolean isCheckDelete() default true;

    /**
     * <h2>是否锁定</h2>
     * <hr/>
     * 用于指定是否锁定<br/>
     * 请注意，禁用后任何用户权限校验不校验用户是否锁定
     *
     * @return {@link Boolean}
     */
    boolean isCheckLock() default true;

    /**
     * <h2>是否过期</h2>
     * <hr/>
     * 用于指定是否过期<br/>
     * 请注意，禁用后任何用户权限校验不校验用户是否过期
     *
     * @return {@link Boolean}
     */
    boolean isCheckExpire() default true;
}
