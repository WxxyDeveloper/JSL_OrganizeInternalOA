package com.jsl.oa.services;

import com.jsl.oa.model.dodata.ProjectChildDO;
import com.jsl.oa.model.dodata.ProjectModuleDO;
import com.jsl.oa.model.vodata.ProjectEditVO;
import com.jsl.oa.model.vodata.ProjectInfoVO;
import com.jsl.oa.model.vodata.ProjectChildAddVO;
import com.jsl.oa.model.vodata.ProjectModuleAddVO;
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

    BaseResponse projectChildAdd(HttpServletRequest request, ProjectChildAddVO projectChildAddVO);

    BaseResponse tGet(List<String> tags, List<String> isFinish, Integer page, Integer pageSize);

    BaseResponse projectFileGet(HttpServletRequest request, Long projectId);

    BaseResponse getProjectModuleById(Integer id);

    BaseResponse getModuleById(Integer id);

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

    BaseResponse getById(Integer id);

    BaseResponse getPrincipalProject(Integer page, Integer pageSize, HttpServletRequest request);

    BaseResponse getParticipateProject(Integer page, Integer pageSize, HttpServletRequest request);

    BaseResponse projectChildDelete(HttpServletRequest request, List<Long> id);

    BaseResponse projectModuleDelete(HttpServletRequest request, List<Long> id);

    BaseResponse projectGetName(String name, HttpServletRequest request);



    BaseResponse projectModuleAdd(HttpServletRequest request, ProjectModuleAddVO projectModuleAddVO);

    BaseResponse projectChildGetName(String name, HttpServletRequest request);

    BaseResponse projectChildGetById(Integer id, HttpServletRequest request);

    BaseResponse projectModuleGetName(String projectName, String childName, HttpServletRequest request);

    BaseResponse projectModuleGetById(Integer id, HttpServletRequest request);

    BaseResponse projectModuleEdit(HttpServletRequest request, ProjectModuleDO projectModuleAddVO, Long id);

    BaseResponse projectChildEdit(HttpServletRequest request, ProjectChildDO projectChildAddVO, Long id);
}
