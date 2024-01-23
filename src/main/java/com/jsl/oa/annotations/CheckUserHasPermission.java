package com.jsl.oa.annotations;

import java.lang.annotation.*;

/**
 * <h1>检查用户是否有权限</h1>
 * <hr/>
 * 用于检查用户是否有权限
 *
 * @version v1.1.0
 * @since v1.1.0
 * @author xiao_lfeng
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserHasPermission {
    /**
     * <h2>权限名称</h2>
     * <hr/>
     * 用于指定权限名称
     *
     * @return {@link String}
     */
    String value() default "";

    /**
     * <h2>是否检查</h2>
     * <hr/>
     * 用于指定是否检查<br/>
     * 请注意，该方法只会禁止检查权限，但是不会禁止检查用户是否允许继续执行
     * @since v1.1.0
     * @return {@link Boolean}
     */
    boolean isCheck() default true;
}
