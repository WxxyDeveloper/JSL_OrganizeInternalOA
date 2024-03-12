package com.jsl.oa.controllers;

import com.jsl.oa.services.TagService;
import com.jsl.oa.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

/**
 * TagController
 * <hr/>
 * 用于处理标签相关的请求, 包括获取标签列表、编辑标签信息等
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0-SNAPSHOT
 */
@Slf4j
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/project/list")
    public BaseResponse getTagsProjectList(
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String limit,
            @RequestParam(required = false) String order
    ) {
        log.info("[Controller] 请求 getProjectList 接口");
        // 处理默认数据
        if (page == null || !Pattern.matches("^[0-9]+$", page) || Integer.parseInt(page) < 1) {
            page = "1";
        }
        if (limit == null || !Pattern.matches("^[0-9]+$", limit) || Integer.parseInt(limit) < 1) {
            limit = "20";
        }
        if ((!"asc".equals(order) && !"desc".equals(order))) {
            order = "id asc";
        } else {
            order = "id " + order;
        }
        // 业务操作
        return tagService.getTagsProjectList(Integer.valueOf(page), Integer.valueOf(limit), order);
    }
}
