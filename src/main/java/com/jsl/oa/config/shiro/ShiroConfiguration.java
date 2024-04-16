package com.jsl.oa.config.shiro;

import com.jsl.oa.config.filter.CorsFilter;
import com.jsl.oa.config.filter.JwtFilter;
import com.jsl.oa.config.filter.TimestampFilter;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 这是一个Shiro的Realm，它负责身份验证和授权。
 * 它扩展了AuthorizingRealm类，该类提供了默认的授权和身份验证实现。
 *
 * @since v1.0.0-SNAPSHOT
 * @version v1.0.0-SNAPSHOT
 * @author xiao_lfeng
 */
@Configuration
@RequiredArgsConstructor
public class ShiroConfiguration {

    /**
     * 配置Shiro过滤器工厂Bean，设置安全管理器、过滤器规则以及自定义过滤器。
     * 此方法用于配置Shiro的安全管理器，定义URL模式的过滤器链，并添加自定义的JWT和CORS过滤器来处理认证和跨域请求。
     *
     * @param securityManager 安全管理器，由Shiro过滤器工厂Bean使用。
     * @return 配置好的ShiroFilterFactoryBean实例。
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 配置过滤器规则
        Map<String, String> filterChainDefinitionMap = setFilterChain();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        // 设置未登录响应接口
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        // 添加JWT过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        // 配置自定义的JWT过滤器
        filters.put("authc", new JwtFilter());
        // 配置自定义的CORS过滤器
        filters.put("anon", new CorsFilter());
        // 配置自定义的时间戳检查
        filters.put("time", new TimestampFilter());

        shiroFilterFactoryBean.setFilters(filters);
        return shiroFilterFactoryBean;
    }

    /**
     * 创建并配置安全管理器，使用自定义领域。
     * 此方法设置安全管理器使用自定义领域进行认证和授权。
     *
     * @param realm 自定义领域，由安全管理器使用。
     * @return 配置好的DefaultWebSecurityManager实例。
     */
    @Bean
    public DefaultWebSecurityManager securityManager(MyRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }

    /**
     * 创建用于认证和授权的自定义领域实例。
     * 此方法为应用提供一个包含用户认证和授权逻辑的自定义领域。
     *
     * @return MyRealm的一个实例。
     */
    @Bean
    public MyRealm myRealm() {
        return new MyRealm();
    }

    /**
     * 定义URL模式和相应过滤器的过滤器链。
     * 此静态方法将URL模式映射到过滤器名称，指定哪些URL可以匿名访问，哪些需要认证。它允许对应用程序不同部分的访问进行细粒度控制。
     *
     * @return URL模式到过滤器名称的映射。
     */
    @NotNull
    private static Map<String, String> setFilterChain() {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/auth/**/**", "anon, time");
        filterChainDefinitionMap.put("/unauthorized", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/info/header-image/get", "anon");
        filterChainDefinitionMap.put("/info/header-user/get", "anon");
        filterChainDefinitionMap.put("/project/header/get", "anon");
        filterChainDefinitionMap.put("/project/get", "anon");
        filterChainDefinitionMap.put("/project/get/custom", "anon");
        filterChainDefinitionMap.put("/project/add", "anon");
        filterChainDefinitionMap.put("/project/work/add", "anon");
        filterChainDefinitionMap.put("/tags/project/list", "anon");
        filterChainDefinitionMap.put("/module/add", "anon");
        filterChainDefinitionMap.put("/**/**", "authc, time");

        return filterChainDefinitionMap;
    }
}
