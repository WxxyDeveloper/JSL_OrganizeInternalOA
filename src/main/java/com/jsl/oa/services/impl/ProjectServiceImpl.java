package com.jsl.oa.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jsl.oa.annotations.NeedPermission;
import com.jsl.oa.dao.*;
import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.dodata.*;
import com.jsl.oa.model.dodata.info.ProjectShowDO;
import com.jsl.oa.model.vodata.*;
import com.jsl.oa.model.vodata.business.info.ProjectShowVO;
import com.jsl.oa.services.MessageService;
import com.jsl.oa.services.ProjectService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

/**
 * <h1>项目服务层实现类</h1>
 * <hr/>
 * 用于项目服务层的实现类
 *
 * @author xiao_lfeng | 176yunxuan | xiangZr-hhh
 * @version v1.1.0
 * @see com.jsl.oa.services.ProjectService
 * @since v1.1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final ReviewDAO reviewDAO;
    private final ProjectDAO projectDAO;
    private final UserDAO userDAO;
    private final ObjectMapper objectMapper;
    private final RoleDAO roleDAO;
    private final ProjectDailyDAO projectDailyDAO;
    private final MessageService messageService;
    private final Gson gson;

    @Override
    public BaseResponse projectAdd(HttpServletRequest request, ProjectInfoVO projectAdd) {
        // 判断权限
        if (!Processing.checkUserIsPrincipal(request, roleDAO)) {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        }

        if (projectAdd.getDescription().isEmpty()) {
            projectAdd.setDescription("{}");
        } else {
            projectAdd.setDescription("{\"描述\":\" " + projectAdd.getDescription() + "\"}");
        }

        HashMap<String, Object> tagMap = new HashMap<>();
        tagMap.put("tags", projectAdd.getTags().split(","));
        projectAdd.setTags(gson.toJson(tagMap));

        projectAdd.setFiles("{\"URI\":\"" + projectAdd.getFiles() + "\"}");


        projectDAO.projectAdd(projectAdd);
        return ResultUtil.success("添加成功");
    }

    @Override
    public BaseResponse projectChildAdd(HttpServletRequest request, ProjectChildAddVO projectChildAddVO) {

        //是否是项目负责人
        if (projectMapper.getProjectById(projectChildAddVO.getProjectId()).getPrincipalId()
                .equals(Processing.getAuthHeaderToUserId(request))
        ) {
            HashMap<String, Object> descriptionMap = new HashMap<>();
            descriptionMap.put("description", projectChildAddVO.getDescription());
            projectChildAddVO.setDescription(gson.toJson(descriptionMap));
            projectDAO.projectWorkAdd(projectChildAddVO);
        } else {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        }
        return ResultUtil.success("添加成功");
    }

    @Override
    public BaseResponse tGet(List<String> tags, List<String> isFinish, Integer page, Integer pageSize) {

        List<ProjectDO> projectDOList = projectDAO.tget(isFinish, tags);

        List<ProjectSimpleVO> projectSimpleVOList = new ArrayList<>();
        for (ProjectDO projectDO : projectDOList) {
            ProjectSimpleVO projectSimpleVO1 = new ProjectSimpleVO();
            Processing.projectTosimply(projectSimpleVO1, projectDO, userDAO, objectMapper);
            projectSimpleVOList.add(projectSimpleVO1);
        }
        //分页返回
        int start = (page - 1) * pageSize;
        int end = start + pageSize;
        List<ProjectSimpleVO> pageData = projectSimpleVOList.subList(start,
                Math.min(end, projectSimpleVOList.size()));
        return ResultUtil.success(pageData);

    }

    @Override
    public BaseResponse projectFileGet(HttpServletRequest request, Long projectId) {

//        判断项目是否存在
        if (!projectDAO.isExistProjectById(projectId)) {
            return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
        }

        ProjectDO projectDO = projectDAO.getProjectById(projectId);

        if (projectDO.getFiles() == null || projectDO.getFiles().equals("{}")) {
            return ResultUtil.success(null);
        }

        // 将文件内容转换为 JSON 数组
        try {
            Object fileJson = new ObjectMapper().readValue(projectDO.getFiles(), Object.class);
            return ResultUtil.success(fileJson);
        } catch (JsonProcessingException e) {
            return ResultUtil.error(ErrorCode.PROJECT_FILE_JSON_ERROR);
        }

    }

    @Override
    public BaseResponse getProjectModuleById(Integer id) {
        return null;
    }

    @Override
    public BaseResponse getById(Integer id) {
        ProjectDO projectDO = projectMapper.tgetProjectById(id);
        return ResultUtil.success(projectDO);
    }

    @Override
    public BaseResponse getPrincipalProject(Integer page, Integer pageSize, HttpServletRequest request) {
        //获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);

        PageHelper.startPage(page, pageSize);
        List<ProjectDO> projectDOList = projectDAO.get(userId, null, null);

        List<ProjectSimpleVO> projectSimpleVOList = new ArrayList<>();
        for (ProjectDO projectDO : projectDOList) {
            ProjectSimpleVO projectSimpleVO1 = new ProjectSimpleVO();
            Processing.projectTosimply(projectSimpleVO1, projectDO, userDAO, objectMapper);
            projectSimpleVOList.add(projectSimpleVO1);
        }
        //分页返回
        PageInfo<ProjectSimpleVO> pageInfo = new PageInfo<>(projectSimpleVOList);
        pageInfo.setTotal(projectDAO.get(userId, null, null).size());
        return ResultUtil.success(pageInfo);
    }

    @Override
    public BaseResponse getParticipateProject(Integer page, Integer pageSize, HttpServletRequest request) {
        //获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);

        PageHelper.startPage(page, pageSize);
        List<ProjectDO> projectDOList = projectMapper.getParticipateProject(userId);

        List<ProjectSimpleVO> projectSimpleVOList = new ArrayList<>();
        for (ProjectDO projectDO : projectDOList) {
            ProjectSimpleVO projectSimpleVO1 = new ProjectSimpleVO();
            Processing.projectTosimply(projectSimpleVO1, projectDO, userDAO, objectMapper);
            projectSimpleVOList.add(projectSimpleVO1);
        }
        //分页返回
        PageInfo<ProjectSimpleVO> pageInfo = new PageInfo<>(projectSimpleVOList);
        pageInfo.setTotal(projectMapper.getParticipateProject(userId).size());
        return ResultUtil.success(pageInfo);
    }

    @Override
    public BaseResponse projectChildDelete(HttpServletRequest request, List<Long> id) {
        //判断是否是项目负责人
        for (Long id1 : id) {
            if (!Objects.equals(Processing.getAuthHeaderToUserId(request), projectMapper.getPirIdbyId(id1))) {
                return ResultUtil.error(ErrorCode.NOT_PERMISSION);
            } else {
                projectMapper.deleteProjectChild(id1);
                //同时删除对应审核信息
                reviewDAO.deleteReviewByProjectChildId(id1);
            }
        }
        return ResultUtil.success();
    }


    @Override
    public BaseResponse projectModuleDelete(HttpServletRequest request, List<Long> id) {
        //判断是否是子系统负责人
        for (Long id1 : id) {
            if (!Objects.equals(Processing.getAuthHeaderToUserId(request), projectMapper.getPirTdByModuleId(id1))) {
                return ResultUtil.error(ErrorCode.NOT_PERMISSION);
            } else {
                Integer projectChildId = projectMapper.getModuleById(id1.intValue()).getProjectChildId().intValue();
                out.println(projectChildId);
                Integer projectId = projectMapper.getWorkById(projectChildId).getProjectId().intValue();
                out.println(projectId);
                projectMapper.deleteProjectModule(id1);
                messageService.messageAdd(projectId, projectChildId, id1.intValue(), 1, request);

            }
        }
        return ResultUtil.success();
    }

    @Override
    public BaseResponse projectGetName(String name, HttpServletRequest request) {
        List<ProjectDO> projectDOList = projectMapper.getByLikeName(name);

        List<ReturnGetVO> returnGetVOList = new ArrayList<>();
        for (ProjectDO projectDO : projectDOList) {
            ReturnGetVO returnGetVO = new ReturnGetVO();
            Processing.copyProperties(projectDO, returnGetVO);
            returnGetVOList.add(returnGetVO);
        }
        return ResultUtil.success(returnGetVOList);
    }

    @Override
    public BaseResponse projectModuleAdd(HttpServletRequest request, ProjectModuleAddVO projectModuleAddVO) {
        //判断是否是子系统负责人或项目负责人
        if (!Objects.equals(Processing.getAuthHeaderToUserId(request),
                projectMapper.getProjectChildById(projectModuleAddVO.getProjectChildId().intValue()).getPrincipalId())
                // 项目负责人
                && !Objects.equals(Processing.getAuthHeaderToUserId(request),
                projectMapper.getPirIdbyId(projectModuleAddVO.getProjectChildId()))) {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        } else {
            HashMap<String, Object> descriptionMap = new HashMap<>();
            descriptionMap.put("description", projectModuleAddVO.getDescription());
            projectModuleAddVO.setDescription(gson.toJson(descriptionMap));
            projectMapper.projectModuleAdd(projectModuleAddVO);
        }
        return ResultUtil.success("添加成功");
    }

    @Override
    public BaseResponse projectChildGetName(String name, HttpServletRequest request) {

        List<ProjectChildDO> projectChildDOList = projectMapper.getChildByLikeName(name);
        List<ReturnGetVO> returnGetVOList = new ArrayList<>();
        for (ProjectChildDO projectChildDO : projectChildDOList) {
            ReturnGetVO returnGetVO = new ReturnGetVO();
            Processing.copyProperties(projectChildDO, returnGetVO);
            returnGetVOList.add(returnGetVO);
        }
        return ResultUtil.success(returnGetVOList);
    }

    @Override
    public BaseResponse projectChildGetById(Integer id, HttpServletRequest request) {

        List<ProjectChildDO> projectChildDOList = projectMapper.getAllChildByProjectId(id);

        List<ReturnGetVO> returnGetVOList = new ArrayList<>();

        if (projectChildDOList.size() == 0) {
            return ResultUtil.success(returnGetVOList);
        }

        for (ProjectChildDO projectChildDO : projectChildDOList) {
            ReturnGetVO returnGetVO = new ReturnGetVO();
            Processing.copyProperties(projectChildDO, returnGetVO);
            returnGetVOList.add(returnGetVO);
        }

        return ResultUtil.success(returnGetVOList);
    }

    @Override
    public BaseResponse projectModuleGetName(String projectName, String childName, HttpServletRequest request) {
        List<ProjectModuleDO> projectModuleDOList = projectMapper.getModuleByName(projectName, childName);
        List<ReturnGetVO> returnGetVOList = new ArrayList<>();
        for (ProjectModuleDO projectModuleDO : projectModuleDOList) {
            ReturnGetVO returnGetVO = new ReturnGetVO();
            Processing.copyProperties(projectModuleDO, returnGetVO);
            returnGetVOList.add(returnGetVO);
        }
        return ResultUtil.success(returnGetVOList);
    }

    @Override
    public BaseResponse projectModuleGetById(Integer id, HttpServletRequest request) {

        List<ProjectModuleDO> projectModuleDOList = projectMapper.getModuleByChildId(id);

        List<ReturnGetVO> returnGetVOList = new ArrayList<>();

        if (projectModuleDOList.size() == 0) {
            return ResultUtil.success(returnGetVOList);
        }

        for (ProjectModuleDO projectModuleDO : projectModuleDOList) {
            ReturnGetVO returnGetVO = new ReturnGetVO();
            Processing.copyProperties(projectModuleDO, returnGetVO);
            returnGetVOList.add(returnGetVO);
        }
        return ResultUtil.success(returnGetVOList);
    }

    @Override
    public BaseResponse projectModuleEdit(HttpServletRequest request, ProjectModuleDO projectModuleEditVO, Long id) {
        //判断是否是子系统负责人或项目负责人
        if (!Objects.equals(Processing.getAuthHeaderToUserId(request), projectMapper.getPirTdByModuleId(id))
                // 项目负责人
                && !Objects.equals(Processing.getAuthHeaderToUserId(request),
                projectMapper.getPirIdbyId(projectMapper.getModuleById(id.intValue()).getProjectChildId()))) {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        } else {
            ProjectModuleDO projectModuleDO = projectMapper.getModuleById(id.intValue());
            if (projectModuleDO == null) {
                return ResultUtil.error(ErrorCode.MODULE_NOT_EXIST);
            }
            if (projectModuleEditVO.getDescription() != null) {
                HashMap<String, Object> descriptionMap = new HashMap<>();
                descriptionMap.put("description", projectModuleEditVO.getDescription());
                projectModuleEditVO.setDescription(gson.toJson(descriptionMap));
            }
            projectModuleEditVO.setId(id);
            projectMapper.projectModuleEdit(projectModuleEditVO);
        }
        return ResultUtil.success("修改成功");
    }

    @Override
    public BaseResponse projectChildEdit(HttpServletRequest request, ProjectChildDO projectChildAddVO, Long id) {
        //判断是否是项目负责人
        if (!Objects.equals(Processing.getAuthHeaderToUserId(request), projectMapper.getPirIdbyId(id))) {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        } else {
            ProjectChildDO projectChildDO = projectMapper.getProjectChildById(id.intValue());
            if (projectChildDO == null) {
                return ResultUtil.error(ErrorCode.PROJECT_CHILD_NOT_EXIST);
            }
            JsonObject jsonObject = gson
                    .fromJson(projectChildDO.getDescription(), JsonObject.class);
            //改动简介发送消息
            if (projectChildAddVO.getDescription() != null) {
                if (!Objects.equals(projectChildAddVO.getDescription(), "")
                        && !projectChildAddVO.getDescription()
                        .equals(jsonObject.get("description").getAsString())) {
                    messageService.messageAdd(projectMapper.getProjectIdBySysId(id)
                            .intValue(), id.intValue(), null, 2, request);
                }
            } // 改动周期或工作量发送消息
            if ((projectChildAddVO.getCycle() != null
                    && projectChildDO.getCycle().equals(projectChildAddVO.getCycle()))
                    || (projectChildAddVO.getWorkLoad() != null
                    && projectChildDO.getWorkLoad().equals(projectChildAddVO.getWorkLoad()))
            ) {
                messageService.messageAdd(projectMapper.getProjectIdBySysId(id)
                        .intValue(), id.intValue(), null, 3, request);
            } //改动负责人发送消息
            if (projectChildAddVO.getPrincipalId() != null
                    && !Objects.equals(projectChildAddVO.getPrincipalId(),
                    projectChildDO.getPrincipalId())) {
                messageService.messageAdd(projectMapper.getProjectIdBySysId(id)
                        .intValue(),  3, id.intValue(),  request);
            }
            if (projectChildAddVO.getDescription() != null) {
                HashMap<String, Object> descriptionMap = new HashMap<>();
                descriptionMap.put("description", projectChildAddVO.getDescription());
                projectChildAddVO.setDescription(gson.toJson(descriptionMap));
            }

            projectChildAddVO.setId(id);
            projectMapper.projectChildEditAll(projectChildAddVO);
        }

        return ResultUtil.success("修改成功");
    }

    @Override
    public BaseResponse getModuleById(Integer id) {
        ProjectModuleDO projectWorkSimpleVO = projectMapper.getModuleById(id);
        // 解析JSON字符串
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(projectWorkSimpleVO.getDescription());
            // 访问特定的key
            JsonNode targetNode = rootNode.get("description");
            if (targetNode != null && !rootNode.isNull()) {
                projectWorkSimpleVO.setDescription(targetNode.asText());
            } else {
                projectWorkSimpleVO.setDescription("null");
            }
        } catch (JsonProcessingException ignored) {

        }
        ProjectModuleSimpleVO projectModuleSimpleVO = new ProjectModuleSimpleVO();
        projectModuleSimpleVO.setPrincipalUser(userDAO.getUserById(projectMapper.getPid(id)).getUsername());

        Processing.copyProperties(projectWorkSimpleVO, projectModuleSimpleVO);

        return ResultUtil.success(projectModuleSimpleVO);
    }

    @Override
    public BaseResponse projectPrincipalGet() {
        return ResultUtil.success(userMapper.getPrincipal());
    }

    @Override
    public BaseResponse getProjectById(HttpServletRequest request, Long projectId) {
        // 对项目 id 进行数据库校验
        ProjectDO getProject = projectDAO.getProjectById(projectId);
        if (getProject == null) {
            return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
        }
        // 检查项目是否被删除
        if (getProject.getIsDelete()) {
            return ResultUtil.error("项目已删除", ErrorCode.PROJECT_NOT_EXIST);
        }
        // 对项目具体信息进行检查
        // TODO: [10001] 需要检查普通用户是否有权限可以看到这一篇项目内容
        return ResultUtil.success(getProject);
    }

    @Override
    public BaseResponse projectEdit(HttpServletRequest request, @NotNull ProjectEditVO projectEdit, Long projectId) {


        //判断用户是否为负责人 或者 项目负责人
        if (!Processing.checkUserIsPrincipal(request, roleDAO)
                && !projectDAO.isPrincipalUser(Processing.getAuthHeaderToUserId(request), projectId)) {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        }
        //判断项目是否存在
        if (projectDAO.isExistProject(projectId)) {
            //更新数据
            ProjectDO projectDO = projectDAO.getProjectById(projectId);
            //改变项目状态发送消息
            if (projectEdit.getStatus() != null && !projectDO.getStatus().equals(projectEdit.getStatus())) {
                messageService.messageAdd(projectId.intValue(), 2, null, request);
            }
            if (projectEdit.getDescription() != null) {
                HashMap<String, Object> descriptionMap = new HashMap<>();
                descriptionMap.put("description", projectEdit.getDescription());
                projectEdit.setDescription(gson.toJson(descriptionMap));
            }

            if (projectEdit.getDescription() != null) {
                HashMap<String, Object> tagMap = new HashMap<>();
                tagMap.put("tags", projectEdit.getTags().split(","));
                projectEdit.setTags(gson.toJson(tagMap));
            }
            if (projectEdit.getDescription() != null) {
                HashMap<String, Object> filesMap = new HashMap<>();
                filesMap.put("URI", projectEdit.getFiles());
                projectEdit.setFiles(gson.toJson(filesMap));
            }

            projectEdit.setId(projectId);
            ProjectDO projectEdit1 = new ProjectDO();

            Processing.copyProperties(projectEdit, projectEdit1);
            projectDAO.projectEdit(projectEdit1);
            return ResultUtil.success(projectDAO.projectEdit(projectEdit1));
        } else {
            return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
        }

    }

    @Override
    public BaseResponse getHeader(Integer id) {
        log.info("\t> 执行 Service 层 InfoService.getHeader 方法");
        ProjectShowDO projectShowDO = projectDAO.getHeader();
        if (id != null) {
            if (id > projectShowDO.getData().size()) {
                return ResultUtil.error(ErrorCode.ID_NOT_EXIST);
            }
            ArrayList<ProjectShowDO.DataDO> newCarouselDO = new ArrayList<>();
            newCarouselDO.add(projectShowDO.getData().get(id - 1));
            projectShowDO.setData(newCarouselDO);
        }
        return ResultUtil.success(projectShowDO);
    }

    @Override
    @NeedPermission("info.project.add")
    public BaseResponse addHeader(HttpServletRequest request, ProjectShowVO projectShowVO) {
        // 获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);
        UserDO userDO = userDAO.getUserById(userId);
        // 获取展示信息
        ProjectShowDO projectShowDO = projectDAO.getHeader();
        // 添加展示
        ProjectShowDO.DataDO projectShow = new ProjectShowDO.DataDO();
        projectShow.setDisplayOrder(projectShowVO.getDisplayOrder())
                .setName(projectShowVO.getName())
                .setType(projectShowVO.getType())
                .setStatus(projectShowVO.getStatus())
                .setIsActive(projectShowVO.getIsActive())
                .setAuthor(userDO.getUsername())
                .setCreatedAt(new Timestamp(currentTimeMillis()).toString());
        projectShowDO.getData().add(projectShow);
        // 保存展示
        if (projectDAO.setProjectShow(projectShowDO)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
    }

    @Override
    @NeedPermission("info.project.del")
    public BaseResponse delHeader(Integer id, HttpServletRequest request) {
        // 获取展示信息
        ProjectShowDO projectShowDO = projectDAO.getHeader();
        // 删除指定展示id
        if (id > projectShowDO.getData().size()) {
            return ResultUtil.error(ErrorCode.ID_NOT_EXIST);
        }
        ProjectShowDO.DataDO data = projectShowDO.getData().remove(id - 1);
        // 保存展示信息
        if (projectDAO.setProjectShow(projectShowDO)) {
            return ResultUtil.success(data);
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
    }

    @Override
    @NeedPermission("info.project.edit")
    public BaseResponse editHeader(HttpServletRequest request, ProjectShowVO projectShowVO, Integer id) {
        // 获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);
        UserDO userDO = userDAO.getUserById(userId);
        // 获取展示信息
        ProjectShowDO projectShowDO = projectDAO.getHeader();
        // 获取指定展示位置
        if (id > projectShowDO.getData().size()) {
            return ResultUtil.error(ErrorCode.ID_NOT_EXIST);
        }
        ProjectShowDO.DataDO projectShow = projectShowDO.getData().get(id - 1);
        projectShow.setDisplayOrder(projectShowVO.getDisplayOrder())
                .setName(projectShowVO.getName())
                .setType(projectShowVO.getType())
                .setStatus(projectShowVO.getStatus())
                .setIsActive(projectShowVO.getIsActive())
                .setAuthor(userDO.getUsername())
                .setUpdatedAt(new Timestamp(currentTimeMillis()).toString());
        // 保存展示信息
        if (projectDAO.setProjectShow(projectShowDO)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
    }

    @Override
    public BaseResponse workGet(
            HttpServletRequest request,
            List<String> tags,
            List<String> isFinish,
            Integer is,
            Integer page,
            Integer pageSize
    ) {
        //获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);

        PageHelper.startPage(page, pageSize);
        List<ProjectDO> projectDOList = projectDAO.workget(userId, tags, isFinish, is);
        List<ProjectSimpleVO> projectSimpleVOList = new ArrayList<>();
        for (ProjectDO projectDO : projectDOList) {
            ProjectSimpleVO projectSimpleVO1 = new ProjectSimpleVO();
            Processing.projectTosimply(projectSimpleVO1, projectDO, userDAO, objectMapper);
            projectSimpleVOList.add(projectSimpleVO1);
        }
        //分页返回
        PageInfo<ProjectSimpleVO> pageInfo = new PageInfo<>(projectSimpleVOList);
        pageInfo.setTotal(projectDAO.workget(userId, tags, isFinish, is).size());
        return ResultUtil.success(pageInfo);

    }

    @Override
    public BaseResponse getByName(String name) {
        if (projectDAO.getByName(name) == null) {
            return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
        } else {
            return ResultUtil.success(projectDAO.getByName(name));
        }
    }

    @Override
    public BaseResponse projectDelete(HttpServletRequest request, List<Long> id) {

        //判断用户是否为老师 或者 项目负责人
        if (!Processing.checkUserIsPrincipal(request, roleDAO)) {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        }

        for (Long id1 : id) {

            if (projectMapper.getNotDeleteProjectById(id1) == null) {
                return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
            }

            if (!projectDAO.isPrincipalUser(Processing.getAuthHeaderToUserId(request), id1)) {
                return ResultUtil.error(ErrorCode.NOT_PERMISSION);
            }
            if (!projectDAO.projectDelete(id1)) {
                return ResultUtil.error(ErrorCode.DATABASE_DELETE_ERROR);
            }
            //同时删除所有对应审核信息
            reviewDAO.deleteReviewByProjectId(id1);
            //同时删除所有对应日报消息
            projectDailyDAO.deleteDailyByProject(id1);
        }

        return ResultUtil.success();
    }
}
