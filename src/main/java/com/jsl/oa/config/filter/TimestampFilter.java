package com.jsl.oa.config.filter;

import com.google.gson.Gson;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 时间戳过滤器
 * <hr/>
 * 对前端发送的时间戳进行检查，当检查通过后将会进入通过过滤器，若检查不通过将会被当前拦截器拦截并返回 {@link ErrorCode} 内的 TIMESTAMP_ERROR
 * 信息。另外，该过滤器仅对 OPTION 请求不进行时间戳检查，其他请求都将会检查处理。
 *
 * @since v1.2.0
 * @version v1.2.0
 * @author xiao_lfeng
 */
@Slf4j
public class TimestampFilter implements Filter {
    private final Gson gson = new Gson();

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        res.setContentType("application/json;charset=UTF-8");
        // 获取当前时间戳
        long nowTimestamp = System.currentTimeMillis();
        if (!req.getMethod().equals("OPTIONS")) {
            String getTimestamp = req.getHeader("Timestamp");
            log.info("[FILTER] 获取到的时间戳为 {} | 当前时间戳 {}", getTimestamp, nowTimestamp);
            if (getTimestamp == null || getTimestamp.isEmpty()) {
                res.setStatus(200);
                res.getWriter().write(gson.toJson(ResultUtil.error(ErrorCode.TIMESTAMP_ERROR)));
            } else {
                // 秒与毫秒转换
                if (getTimestamp.length() == 10) {
                    getTimestamp += "000";
                }
                // 时间误差允许前后五秒钟
                if (nowTimestamp - Long.parseLong(getTimestamp) <= 10000
                        && nowTimestamp - Long.parseLong(getTimestamp) >= -10000) {
                    chain.doFilter(req, res);
                } else {
                    res.setStatus(200);
                    res.getWriter().write(gson.toJson(ResultUtil.error(ErrorCode.TIMESTAMP_ERROR)));
                }
            }
        } else {
            log.info("[FILTER] 预执行请求[OPTION]，不进行时间戳检查");
            res.setStatus(200);
            res.getWriter().write(gson.toJson(ResultUtil.success("Option成功")));
        }
    }
}
