package com.jsl.oa.controllers;

import com.jsl.oa.services.ReviewService;
import com.jsl.oa.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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

//    审核服务
    private final ReviewService reviewService;

    /**
     * @Description: 获取审核记录列表
     * @Date: 2024/4/11
     * @Param request:
     **/
    @GetMapping("/review/getReviewRecords")
    public BaseResponse getUserReviewRecords(HttpServletRequest request) {
        log.info("请求接口[GET]: /review/getMyReview");
        return reviewService.getUserReview(request);
    }


    /**
     * @Description: 获取我的审核数据
     * @Date: 2024/4/12
     * @Param request:
     **/
    @GetMapping("/review/getMyReview")
    public BaseResponse getMyReview(HttpServletRequest request) {
        log.info("请求接口[GET]: /review/getMyReview");
        return reviewService.getUserPendingApprovalReview(request);
    }


}


