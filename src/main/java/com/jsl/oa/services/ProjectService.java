package com.jsl.oa.services;

import com.jsl.oa.model.vodata.ProjectEditVO;
import com.jsl.oa.model.vodata.ProjectInfoVO;
import com.jsl.oa.model.vodata.ProjectWorkVO;
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

    BaseResponse getByName(String name);

    BaseResponse projectDelete(HttpServletRequest request, List<Long> id);

    BaseResponse projectAdd(HttpServletRequest request, ProjectInfoVO projectVO);

    BaseResponse workGet(
            HttpServletRequest request,
            List<String> tags,
            List<String> isFinish,
            Integer is,
            Integer page,
            Integer pageSize);

    BaseResponse projectWorkAdd(HttpServletRequest request, ProjectWorkVO projectWorkVO);

    BaseResponse tGet(List<String> tags, List<String> isFinish, Integer page, Integer pageSize);

    BaseResponse projectFileGet(HttpServletRequest request, Long projectId);

    BaseResponse getById(Integer id);

    BaseResponse getWorkById(Integer id);

    BaseResponse projectPrincipalGet();

    /**
     * 从项目 id 获取项目的详细信息
     * <hr/>
     * 根据项目所属 id 获取项目的详细信息，根据用户所属角色组的不同返回不同的内容
     *
     * @param request   获取请求体
     * @param projectId 项目 id
     * @return 根据用户所属角色组的不同返回不同的内容
     */
    BaseResponse getProjectById(HttpServletRequest request, Long projectId);
}
