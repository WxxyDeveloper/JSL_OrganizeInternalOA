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
 * @author xiao_lfeng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAbleToUse { }
