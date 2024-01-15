package com.jsl.oa.config;

import com.google.gson.Gson;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.JwtUtil;
import com.jsl.oa.utils.ResultUtil;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * <h1>JWT过滤器</h1>
 * <hr/>
 * 用于JWT的过滤器
 *
 * @since v1.1.0
 * @version v1.1.0
 * @author 筱锋xiao_lfeng
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * <h2>判断用户Token</h2>
     * <hr/>
     * 判断用户Token是否存在，如果存在则进行验证
     *
     * @param request 请求
     * @param response 响应
     * @param mappedValue 映射值
     * @return {@link Boolean}
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 判断是否存在Authorization Header
        String token = getAuthzHeader(request);
        if (token == null || token.isEmpty()) {
            return false; // 未提供Token，拒绝访问
        } else {
            // 解析Bearer后面的令牌
            token = token.replace("Bearer ", "");
            System.out.println(token);
            return JwtUtil.verify(token);
        }
    }

    /**
     * <h2>访问被拒绝时</h2>
     * <hr/>
     * 当访问被拒绝时，会调用此方法
     *
     * @param request 请求
     * @param response 响应
     * @param mappedValue 映射值
     * @return {@link Boolean}
     * @throws Exception 异常
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, @NotNull ServletResponse response, Object mappedValue) throws Exception {
        Gson gson = new Gson();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(gson.toJson(ResultUtil.error(ErrorCode.UNAUTHORIZED)));
        return false;
    }

    /**
     * <h2>获取Authorization Header</h2>
     * <hr/>
     * 用于获取Authorization Header
     *
     * @param request 请求
     * @return {@link String}
     */
    @Override
    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        return httpRequest.getHeader("Authorization");
    }
}
