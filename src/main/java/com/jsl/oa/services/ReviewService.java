package com.jsl.oa.services;


import com.jsl.oa.model.vodata.ReviewAddVO;
import com.jsl.oa.model.vodata.ReviewUpdateResultVO;
import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public interface ReviewService {

    BaseResponse getUserPendingApprovalReview(HttpServletRequest request);

    BaseResponse getUserReview(HttpServletRequest request);

    BaseResponse addReview(ReviewAddVO reviewAddVO, HttpServletRequest request);

    BaseResponse updateReviewResult(ReviewUpdateResultVO reviewUpdateResultVOVO, HttpServletRequest request);
}
