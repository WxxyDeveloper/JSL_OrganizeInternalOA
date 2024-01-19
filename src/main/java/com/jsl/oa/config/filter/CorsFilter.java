package com.jsl.oa.config.filter;

import lombok.extern.slf4j.Slf4j;
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

        // 允许跨域请求
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("CORS过滤器放行异常", e);
        }
    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {
        Filter.super.destroy();
    }

}
