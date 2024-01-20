package com.jsl.oa.services;

import com.jsl.oa.model.doData.info.ProjectShowDO;
import com.jsl.oa.model.voData.ProjectInfoVO;
import com.jsl.oa.model.voData.business.info.ProjectShowVO;
import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public interface ProjectService {
    BaseResponse projectAdd(ProjectInfoVO projectAdd);

    BaseResponse projectEdit(ProjectInfoVO projectEdit);

    BaseResponse projectGetUserInCutting(Long uid);

    BaseResponse projectAddUserForCutting(Long uid, Long pid);

    BaseResponse getHeader(Integer id);

    BaseResponse addHeader(HttpServletRequest request, ProjectShowVO projectShowVO);

    BaseResponse delHeader(Integer id, HttpServletRequest request);

    BaseResponse editHeader(HttpServletRequest request, ProjectShowVO projectShowVO, Integer id);

    BaseResponse get();

    BaseResponse getByName(String name);

    BaseResponse projectDelete(Long id);
}
