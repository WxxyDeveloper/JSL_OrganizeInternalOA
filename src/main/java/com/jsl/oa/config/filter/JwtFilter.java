package com.jsl.oa.config.filter;

import com.google.gson.Gson;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.JwtUtil;
import com.jsl.oa.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <h1>JWT过滤器</h1>
 * <hr/>
 * 用于JWT的过滤器
 *
 * @author 筱锋xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
@Slf4j
@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * <h2>判断用户Token</h2>
     * <hr/>
     * 判断用户Token是否存在，如果存在则进行验证
     *
     * @param request     请求
     * @param response    响应
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
            log.info("请求令牌：" + token);
            return JwtUtil.verify(token);
        }
    }

    /**
     * <h2>访问被拒绝时</h2>
     * <hr/>
     * 当访问被拒绝时，会调用此方法
     *
     * @param request     请求
     * @param response    响应
     * @param mappedValue 映射值
     * @return {@link Boolean}
     * @throws Exception 异常
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 添加跨域禁止
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        // 程序执行
        try {
            // 尝试获取Authorization Header
            String token = getAuthzHeader(request);
            if (token == null || token.isEmpty()) {
                // 未提供Token，拒绝访问
                Gson gson = new Gson();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(gson.toJson(ResultUtil.error(ErrorCode.UNAUTHORIZED)));
                return false;
            } else {
                // 解析Bearer后面的令牌
                token = token.replace("Bearer ", "");
                System.out.println(token);
                if (JwtUtil.verify(token)) {
                    // Token验证通过
                    return true;
                } else {
                    // Token验证失败，抛出异常
                    throw new ExpiredCredentialsException("Token已过期");
                }
            }
        } catch (ExpiredCredentialsException e) {
            // 处理Token过期异常，返回自定义的JSON信息
            Gson gson = new Gson();
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(gson.toJson(ResultUtil.error(ErrorCode.TOKEN_EXPIRED)));
            return false;
        }
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
