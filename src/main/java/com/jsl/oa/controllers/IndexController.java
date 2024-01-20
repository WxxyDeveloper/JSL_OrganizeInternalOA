package com.jsl.oa.controllers;

import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaofeng
 */
@Slf4j
@RestController
public class IndexController {
    @RequestMapping("/")
    public BaseResponse index() {
        log.info("请求接口[GET]: /");
        return ResultUtil.success("欢迎使用JSL-OA系统，服务器处于正常状态");
    }
}
