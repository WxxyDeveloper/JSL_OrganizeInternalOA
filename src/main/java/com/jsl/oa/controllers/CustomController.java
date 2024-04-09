package com.jsl.oa.controllers;

import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ResultUtil;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CustomController类实现ErrorController接口，处理错误和未授权请求。
 *
 * @author xiao_lfeng
 * @since v1.0.0-SNAPSHOT
 * @version 1.0.0-SNAPSHOT
 */
@RestController
public class CustomController implements ErrorController {

    /**
     * 处理错误请求，返回一个包含错误信息的ResponseEntity。
     *
     * @return 一个包含错误信息的ResponseEntity
     */
    @RequestMapping("/error")
    public ResponseEntity<BaseResponse> handleError() {
        return ResultUtil.error("PageNotFound", 404, "请求资源不存在");
    }

    /**
     * 处理未授权请求，返回一个包含错误信息的ResponseEntity。
     *
     * @return 一个包含错误信息的ResponseEntity
     */
    @RequestMapping("/unauthorized")
    public ResponseEntity<BaseResponse> handleUnauthorized() {
        return ResultUtil.error("Unauthorized", 401, "未授权");
    }
}
