package com.jsl.oa.controllers;

import com.jsl.oa.services.ReviewService;
import com.jsl.oa.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 审核控制器
 *
 * @author xiangZr-hhh
 * @version 1.1.0
 * @since 1.1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * @Description: 获取我的审核列表
     * @Date: 2024/4/11
     * @Param request:
     **/
    @GetMapping("/review/getMyReview")
    public BaseResponse getUserReview(@RequestParam Long projectId, HttpServletRequest request) {
        return reviewService.getUserReview(projectId, request);
    }

}


