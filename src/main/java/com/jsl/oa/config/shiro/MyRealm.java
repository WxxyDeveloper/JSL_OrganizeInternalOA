package com.jsl.oa.config.shiro;

import com.jsl.oa.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class MyRealm extends AuthorizingRealm {

    private final UserService userService;

    /**
     * 授权
     *
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(@NotNull PrincipalCollection principals) {
        return null;
    }


    /**
     * 认证
     *
     * @param authenticationToken 令牌
     * @return 认证信息
     * @throws AuthenticationException 认证异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        return null;
    }
}
