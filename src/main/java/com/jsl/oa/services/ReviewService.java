package com.jsl.oa.services;


import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public interface ReviewService {

    BaseResponse getUserPendingApprovalReview(HttpServletRequest request);

    BaseResponse getUserReview(HttpServletRequest request);

}
