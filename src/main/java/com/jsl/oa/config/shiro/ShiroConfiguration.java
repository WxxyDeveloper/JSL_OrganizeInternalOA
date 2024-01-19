package com.jsl.oa.config.shiro;

import com.jsl.oa.config.filter.CorsFilter;
import com.jsl.oa.config.filter.JwtFilter;
import com.jsl.oa.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
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
        filterChainDefinitionMap.put("/auth/**/**", "anon"); // 登录接口允许匿名访问
        filterChainDefinitionMap.put("/unauthorized", "anon"); // 未授权接口允许匿名访问
        filterChainDefinitionMap.put("/", "anon"); // 首页允许匿名访问
        filterChainDefinitionMap.put("/info/header-image/get", "anon"); // 信息接口允许匿名访问
        filterChainDefinitionMap.put("/**/**", "authc"); // 其他接口一律拦截(需要Token)

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        // 设置未登陆响应接口
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        // 添加JWT过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("authc", new JwtFilter()); // 配置自定义的JWT过滤器
        filters.put("anon", new CorsFilter()); // 配置自定义的CORS过滤器
        shiroFilterFactoryBean.setFilters(filters);
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
        return new MyRealm();
    }
}
