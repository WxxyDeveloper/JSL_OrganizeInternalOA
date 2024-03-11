package com.jsl.oa.services;

import com.jsl.oa.model.voData.ProjectCuttingAddVO;
import com.jsl.oa.model.voData.ProjectCuttingEditVO;
import com.jsl.oa.model.voData.ProjectInfoVO;
import com.jsl.oa.model.voData.business.info.ProjectShowVO;
import com.jsl.oa.utils.BaseResponse;
import org.apache.catalina.LifecycleState;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProjectService {
    BaseResponse projectAdd(HttpServletRequest request, ProjectInfoVO projectAdd);

    BaseResponse projectEdit(HttpServletRequest request, ProjectInfoVO projectEdit);

    BaseResponse projectGetUserInCutting(Long uid);

    BaseResponse projectAddUserForCutting(HttpServletRequest request, Long uid, Long pid);

    BaseResponse getHeader(Integer id);

    BaseResponse addHeader(HttpServletRequest request, ProjectShowVO projectShowVO);

    BaseResponse delHeader(Integer id, HttpServletRequest request);

    BaseResponse editHeader(HttpServletRequest request, ProjectShowVO projectShowVO, Integer id);

    BaseResponse get(Integer listAll, HttpServletRequest request, List<String> tags);

    BaseResponse getByName(String name);

    BaseResponse projectDelete(HttpServletRequest request, Long id);

    BaseResponse addProjectCutting(HttpServletRequest request, ProjectCuttingAddVO projectCuttingAddVO);

    BaseResponse editProjectCutting(HttpServletRequest request, ProjectCuttingEditVO projectCuttingEditVO);

    BaseResponse projectToOtherUserForCutting(HttpServletRequest request, Long oldUid, Long pid, Long newUid);
}
