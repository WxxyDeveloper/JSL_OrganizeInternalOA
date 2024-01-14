package com.jsl.oa.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

@Getter
@RequiredArgsConstructor
public class JwtToken implements AuthenticationToken {

    private final String token;
    private final String username;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}