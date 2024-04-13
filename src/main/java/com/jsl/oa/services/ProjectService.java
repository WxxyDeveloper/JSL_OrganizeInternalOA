package com.jsl.oa.services;

import com.jsl.oa.model.vodata.*;
import com.jsl.oa.model.vodata.business.info.ProjectShowVO;
import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProjectService {

    BaseResponse projectEdit(HttpServletRequest request, ProjectEditVO projectEdit, Long projectId);

    BaseResponse getHeader(Integer id);

    BaseResponse addHeader(HttpServletRequest request, ProjectShowVO projectShowVO);

    BaseResponse delHeader(Integer id, HttpServletRequest request);

    BaseResponse editHeader(HttpServletRequest request, ProjectShowVO projectShowVO, Integer id);

    BaseResponse get(
                     HttpServletRequest request,
                     List<String> tags,
                     List<String> isFinish,
                     Integer page,
                     Integer pageSize);

    BaseResponse getByName(String name);

    BaseResponse projectDelete(HttpServletRequest request, List<Long> id);

    BaseResponse projectAdd(HttpServletRequest request, ProjectInfoVO projectVO);

    BaseResponse projectToOtherUserForCutting(HttpServletRequest request, Long oldUid, Long pid, Long newUid);

    BaseResponse workget(
                         HttpServletRequest request,
                         List<String> tags,
                         List<String> isFinish,
                         Integer is,
                         Integer page,
                         Integer pageSize);

    BaseResponse projecWorktAdd(HttpServletRequest request, ProjectWorkVO projectWorkVO);

    BaseResponse tget(List<String> tags, List<String> isFinish, Integer page, Integer pageSize);

    BaseResponse projectFileGet(HttpServletRequest request, Long projectId);

    BaseResponse getById(Integer id);

    BaseResponse getWorkById(Integer id);

    BaseResponse projectPrincipalGet();
}
