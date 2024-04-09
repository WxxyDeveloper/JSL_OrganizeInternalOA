package com.jsl.oa.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


/**
 * 这是一个Shiro的Realm，它负责身份验证和授权。
 * 它扩展了AuthorizingRealm类，该类提供了默认的授权和身份验证实现。
 * <p>
 * doGetAuthenticationInfo()方法负责验证用户，
 * doGetAuthorizationInfo()方法负责检索用户的授权信息。
 *
 * @author jsl
 */
public class MyRealm extends AuthorizingRealm {

    /**
     * 这将负责验证用户。
     * 它接受一个AuthenticationToken作为参数，该参数表示用户的��据，
     * 并返回一个AuthenticationInfo对象，该对象包含用户的身份验证详细信息。
     * <p>
     * 在这种实现中，该方法总是返回null，表示身份验证不受支持。
     * 这可以在子类中被重写以支持身份验证。
     *
     * @param authenticationToken 用户的��据
     * @return null，表示身份验证不受支持
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        return null;
    }

    /**
     * 这将负责检索用户的授权信息。
     * 它接受一个PrincipalCollection作为参数，该参数表示用户的主体，
     * 并返回一个AuthorizationInfo对象，该对象包含用户的授权详细信息。
     * <p>
     * 在这种实现中，该方法总是返回null，表示授权不受支持。
     * 这可以在子类中被重写以支持授权。
     *
     * @param principals 用户的主体
     * @return null，表示授权不受支持
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

}
