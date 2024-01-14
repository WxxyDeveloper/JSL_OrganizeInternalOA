package com.jsl.oa.config;

import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

@RequiredArgsConstructor
public class MyRealm extends AuthorizingRealm {

    private final UserService userService;

    /**
     * 授权
     * @param principalCollection 令牌
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证
     * @param authenticationToken 令牌
     * @return 认证信息
     * @throws AuthenticationException 认证异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String username = jwtToken.getUsername();

        // 从数据库获取用户信息
        UserDO userDO = userService.getUserInfoByUsername(username);
        if (userDO == null) {
            throw new UnknownAccountException("用户不存在");
        } else if (!userDO.getAccountNoLocked()) {
            throw new LockedAccountException("用户已被锁定");
        } else if (!userDO.getEnabled()) {
            throw new DisabledAccountException("用户已被禁用");
        } else if (!userDO.getAccountNoExpired()) {
            throw new ExpiredCredentialsException("用户已过期");
        }

        return new SimpleAuthenticationInfo(username, jwtToken.getCredentials(), getName());
    }
}
