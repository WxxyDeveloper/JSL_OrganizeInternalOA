package com.jsl.oa.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CorsFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        // 请求头处理
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        // 设置请求头
        setHeader(response);

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("CORS过滤器放行异常", e);
        }
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
        Filter.super.destroy();
    }

    protected static void setHeader(@NotNull HttpServletResponse response) {
        // 允许跨域请求
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //允许请求方式
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        //需要放行header头部字段 如需鉴权字段，自行添加，如Authorization
        response.setHeader("Access-Control-Allow-Headers", "*");
    }

}
