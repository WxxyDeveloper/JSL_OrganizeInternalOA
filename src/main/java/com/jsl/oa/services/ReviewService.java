package com.jsl.oa.services;


import com.jsl.oa.model.vodata.ReviewAddVO;
import com.jsl.oa.model.vodata.ReviewUpdateResultVO;
import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public interface ReviewService {

    BaseResponse getUserPendingApprovalReview(Integer page, Integer pageSize, HttpServletRequest request);

    BaseResponse getUserReview(Integer page, Integer pageSize, HttpServletRequest request);

    BaseResponse addReview(ReviewAddVO reviewAddVO, HttpServletRequest request);

    BaseResponse updateReviewResult(ReviewUpdateResultVO reviewUpdateResultVOVO, HttpServletRequest request);

    BaseResponse searchReview(String content, HttpServletRequest request, Integer page, Integer pageSize);

    BaseResponse searchReviewRecords(String content,
                                     Short statue,
                                     HttpServletRequest request,
                                     Integer page,
                                     Integer pageSize);
}
