package com.jsl.oa.controllers;

import com.jsl.oa.model.vodata.ReviewAddVO;
import com.jsl.oa.model.vodata.ReviewUpdateResultVO;
import com.jsl.oa.services.ReviewService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

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
    public BaseResponse getUserReviewRecords(
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            HttpServletRequest request) {
        log.info("请求接口[GET]: /review/getMyReview");
        return reviewService.getUserReview(page, pageSize, request);
    }


    /**
     * @Description: 获取我的审核数据
     * @Date: 2024/4/12
     * @Param request:
     **/
    @GetMapping("/review/getMyReview")
    public BaseResponse getMyReview(@RequestParam Integer page,
                                    @RequestParam Integer pageSize,
                                    HttpServletRequest request) {
        log.info("请求接口[GET]: /review/getMyReview");
        return reviewService.getUserPendingApprovalReview(page, pageSize, request);
    }


    /**
     * @Description: 新增审核申请
     * @Date: 2024/4/12
     * @Param null:
     **/
    @PostMapping("/review/add")
    public BaseResponse addReview(@RequestBody @Validated ReviewAddVO reviewAddVO,
                                  @NotNull BindingResult bindingResult,
                                  HttpServletRequest request) {
        log.info("请求接口[POST]: /review/add");

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR);
        }

        return reviewService.addReview(reviewAddVO, request);
    }


    @PutMapping("/review/updateReview")
    public BaseResponse updateReview(@RequestBody @Validated ReviewUpdateResultVO reviewUpdateResultVOVO,
                                     @NotNull BindingResult bindingResult,
                                     HttpServletRequest request) {
        log.info("请求接口[PUT]: /review/updateReview");

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR);
        }

        return reviewService.updateReviewResult(reviewUpdateResultVOVO, request);
    }


}


