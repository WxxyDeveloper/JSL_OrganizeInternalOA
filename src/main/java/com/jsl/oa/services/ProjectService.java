package com.jsl.oa.services;

import com.jsl.oa.model.voData.*;
import com.jsl.oa.model.voData.business.info.ProjectShowVO;
import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProjectService {
    BaseResponse projectAdd(HttpServletRequest request, ProjectInfoVO projectAdd);

    BaseResponse projectEdit(HttpServletRequest request, ProjectEditVO projectEdit, Long projectId);

    BaseResponse projectGetUserInCutting(Long uid);

    BaseResponse projectAddUserForCutting(HttpServletRequest request, Long uid, Long pid);

    BaseResponse getHeader(Integer id);

    BaseResponse addHeader(HttpServletRequest request, ProjectShowVO projectShowVO);

    BaseResponse delHeader(Integer id, HttpServletRequest request);

    BaseResponse editHeader(HttpServletRequest request, ProjectShowVO projectShowVO, Integer id);

    BaseResponse get(Integer listAll, HttpServletRequest request, List<String> tags, Integer isFinish);

    BaseResponse getByName(String name);

    BaseResponse projectDelete(HttpServletRequest request, Long id);

    BaseResponse addProjectCutting(HttpServletRequest request, ProjectCuttingAddVO projectCuttingAddVO);

    BaseResponse editProjectCutting(HttpServletRequest request, ProjectCuttingEditVO projectCuttingEditVO);

    BaseResponse projectToOtherUserForCutting(HttpServletRequest request, Long oldUid, Long pid, Long newUid);

    BaseResponse workget(Integer listAll, HttpServletRequest request, List<String> tags, Integer isFinish);

    BaseResponse projecWorktAdd(HttpServletRequest request, ProjectWorkVO projectWorkVO);

    BaseResponse tget(Integer id, List<String> tags, Integer isFinish);
}
