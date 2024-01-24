package com.jsl.oa.controllers;

import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author xiaofeng
 */
@Slf4j
@RestController
public class IndexController {
    @Value("${maven.timestamp}")
    private String timestamp;

    @RequestMapping("/")
    public BaseResponse index() {
        log.info("请求接口[GET]: /");
        HashMap<String, String> newMap = new HashMap<>();
        newMap.put("timestamp", "构建时间：" + timestamp);
        return ResultUtil.success("欢迎使用JSL-OA系统，服务器处于正常状态", newMap);
    }
}
