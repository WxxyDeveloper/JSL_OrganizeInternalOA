package com.jsl.oa.controllers;

import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ResultUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @RequestMapping("/")
    public BaseResponse index() {
        return ResultUtil.success("欢迎使用JSL-OA系统，服务器处于正常状态");
    }
}
