package com.jsl.oa.services;

import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;


public interface ModuleService {
    BaseResponse getByProjectId(Integer projectId, HttpServletRequest request);

    BaseResponse getBySysId(Integer sysId, HttpServletRequest request);

    BaseResponse deleteById(HttpServletRequest request, Long id);
}
