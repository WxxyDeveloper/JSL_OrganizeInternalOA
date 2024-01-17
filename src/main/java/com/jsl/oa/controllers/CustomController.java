package com.jsl.oa.controllers;

import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ResultUtil;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomController implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<BaseResponse> handleError() {
        return ResultUtil.error("PageNotFound", 404, "请求资源不存在");
    }

    @RequestMapping("/unauthorized")
    public ResponseEntity<BaseResponse> handleUnauthorized() {
        return ResultUtil.error("Unauthorized", 401, "未授权");
    }
}
