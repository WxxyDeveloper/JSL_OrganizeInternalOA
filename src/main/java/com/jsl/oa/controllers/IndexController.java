package com.jsl.oa.controllers;

import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


/**
 * IndexController类提供了JSL-OA系统的主入口，处理根路径"/"的GET请求。
 *
 * @author xiao_lfeng
 * @version v1.0.0-SNAPSHOT
 * @since v1.0.0-SNAPSHOT
 */
@Slf4j
@RestController
public class IndexController {

    /**
     * 使用@Value注解，Spring会自动注入Maven构建时间。
     */
    @Value("${maven.timestamp}")
    private String timestamp;

    /**
     * 处理根路径"/"的GET请求，返回一个包含欢迎信息和服务器状态的成功响应。
     * 在处理请求时，会记录一个info级别的日志信息。
     *
     * @return 一个包含欢迎信息和服务器状态的成功响应
     */
    @RequestMapping("/")
    public BaseResponse index() {
        HashMap<String, String> newMap = new HashMap<>();
        newMap.put("timestamp", "构建时间：" + timestamp);
        return ResultUtil.success("欢迎使用JSL-OA系统，服务器处于正常状态", newMap);
    }
}
