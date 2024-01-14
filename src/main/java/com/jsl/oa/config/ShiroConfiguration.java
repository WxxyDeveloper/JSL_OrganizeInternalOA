package com.jsl.oa.config;

import com.jsl.oa.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ShiroConfiguration {

    private final UserService userService;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 配置过滤器规则
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/auth/**", "anon"); // 登录接口允许匿名访问

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        // 设置登录接口
        shiroFilterFactoryBean.setLoginUrl("/unauthorized");
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(MyRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }

    @Bean
    public MyRealm myRealm() {
        return new MyRealm(userService);
    }
}
