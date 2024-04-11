package com.jsl.oa.services.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsl.oa.annotations.CheckUserHasPermission;
import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.UserDO;
import com.jsl.oa.model.dodata.info.ProjectShowDO;
import com.jsl.oa.model.vodata.*;
import com.jsl.oa.model.vodata.business.info.ProjectShowVO;
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
import java.util.List;
import java.util.Objects;

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
    private final ProjectDAO projectDAO;
    private final UserDAO userDAO;
    private final ObjectMapper objectMapper;
    private final RoleDAO roleDAO;

    @Override
    public BaseResponse projectAdd(HttpServletRequest request, ProjectInfoVO projectAdd) {
        log.info("\t> 执行 Service 层 ProjectService.projectAdd 方法");
        if (projectAdd.getDescription().isEmpty()) {
            projectAdd.setDescription("{}");
        } else {
            projectAdd.setDescription("{\"description\":\" " + projectAdd.getDescription() + "\"}");
        }
        String tags = projectAdd.getTags();
        String[] split = tags.split(",");
        String open = "{\"tags\":[\"";
        String close = "]}";
        StringBuilder tag = new StringBuilder();
        for (String tag1 : split) {
            tag.append(tag1).append("\",\"");
        }
        if (tag.length() > 0) {
            tag = new StringBuilder(tag.substring(0, tag.length() - 2));
        }
        projectAdd.setTags(open + tag + close);
        projectAdd.setFile("{\"URI\":\"" + projectAdd.getFile() + "\"}");


        projectDAO.projectAdd(projectAdd);
        return ResultUtil.success("添加成功");
    }

    @Override
    public BaseResponse projectToOtherUserForCutting(HttpServletRequest request, Long oldUid, Long pid, Long newUid) {
        return null;
    }

    @Override
    public BaseResponse projecWorktAdd(HttpServletRequest request, ProjectWorkVO projectWorkVO) {
        log.info("\t> 执行 Service 层 ProjectService.projectWorkAdd 方法");
        //获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);
        //是否是增加子系统
        if (projectWorkVO.getType() == 0) {
            //是否是老师
            if (Processing.checkUserIsTeacher(request, roleDAO)) {
                projectDAO.projectWorkAdd(projectWorkVO);
            } else {
                return ResultUtil.error(ErrorCode.NOT_PERMISSION);
            }
        } else {
            //是否是子系统的负责人
            if (Objects.equals(userId, projectMapper.getPirIdbyWorkid(projectWorkVO.getPid()))) {
                projectDAO.projectWorkAdd(projectWorkVO);
            } else {
                return ResultUtil.error(ErrorCode.NOT_PERMISSION);
            }
        }

        return ResultUtil.success("添加成功");
    }

    @Override
    public BaseResponse tget(Integer id, List<String> tags, List<Integer> isFinish, Integer page, Integer pageSize) {
        log.info("\t> 执行 Service 层 ProjectService.tget 方法");
        //根据id查询
        if (id != null) {
            ProjectDO projectDO = projectMapper.tgetProjectById(id);
            ProjectSimpleVO projectSimpleVO = new ProjectSimpleVO();
            Processing.projectTosimply(projectSimpleVO, projectDO, userDAO, objectMapper);
            return ResultUtil.success(projectSimpleVO);
        }

        //根据标签查询
        if (tags != null && !tags.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.tget(id, isFinish, tags);

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

        //根据状态查询
        if (isFinish != null && !isFinish.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.tget(id, isFinish, tags);

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

        List<ProjectDO> projectDOList = projectDAO.tget(id, isFinish, tags);
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

        if (projectDO.getFile() == null || projectDO.getFile().equals("{}")) {
            return ResultUtil.success(null);
        }

        // 将文件内容转换为 JSON 数组
        try {
            Object fileJson = new ObjectMapper().readValue(projectDO.getFile(), Object.class);
            return ResultUtil.success(fileJson);
        } catch (JsonProcessingException e) {
            return ResultUtil.error(ErrorCode.PROJECT_FILE_JSON_ERROR);
        }

    }

    @Override
    public BaseResponse getById(Integer id) {
        ProjectDO projectDO = projectMapper.tgetProjectById(id);
        return ResultUtil.success(projectDO);
    }

    @Override
    public BaseResponse getWorkById(Integer id) {
        ProjectWorkSimpleVO projectWorkSimpleVO = projectMapper.getWorkById(id);

        projectWorkSimpleVO.setPrincipalUser(userDAO.getUserById(projectMapper.getPid(id)).getUsername());
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
        return ResultUtil.success(projectWorkSimpleVO);
    }

    @Override
    public BaseResponse projectPrincipalGet() {
        return ResultUtil.success(userMapper.getPrincipal());
    }

    @Override
    public BaseResponse projectEdit(HttpServletRequest request, @NotNull ProjectEditVO projectEdit, Long projectId) {
        log.info("\t> 执行 Service 层 ProjectService.projectEdit 方法");


        //判断用户是否为老师 或者 项目负责人
        if (!Processing.checkUserIsTeacher(request, roleDAO)
                || !projectDAO.isPrincipalUser(Processing.getAuthHeaderToUserId(request), projectId)) {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        }

        //判断项目是否存在
        if (projectDAO.isExistProject(projectId)) {
            //更新数据
            return ResultUtil.success(projectDAO.projectEdit(projectEdit, projectId));
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
    @CheckUserHasPermission("info.project.add")
    public BaseResponse addHeader(HttpServletRequest request, ProjectShowVO projectShowVO) {
        log.info("\t> 执行 Service 层 InfoService.addHeader 方法");
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
                .setCreatedAt(new Timestamp(System.currentTimeMillis()).toString());
        projectShowDO.getData().add(projectShow);
        // 保存展示
        if (projectDAO.setProjectShow(projectShowDO)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
    }

    @Override
    @CheckUserHasPermission("info.project.del")
    public BaseResponse delHeader(Integer id, HttpServletRequest request) {
        log.info("\t> 执行 Service 层 InfoService.delHeader 方法");
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
    @CheckUserHasPermission("info.project.edit")
    public BaseResponse editHeader(HttpServletRequest request, ProjectShowVO projectShowVO, Integer id) {
        log.info("\t> 执行 Service 层 InfoService.editHeader 方法");
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
                .setUpdatedAt(new Timestamp(System.currentTimeMillis()).toString());
        // 保存展示信息
        if (projectDAO.setProjectShow(projectShowDO)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
    }

    @Override
    public BaseResponse get(
            Integer listAll,
            HttpServletRequest request,
            List<String> tags,
            List<Integer> isFinish,
            Integer page,
            Integer pageSize
    ) {
        log.info("\t> 执行 Service 层 ProjectService.get 方法");

        //获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);
        //根据标签查询
        if (tags != null && !tags.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.get(userId, listAll, tags, isFinish);

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

        //根据状态查询
        if (isFinish != null && !isFinish.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.get(userId, listAll, tags, isFinish);
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


        //判断是否是老师(项目负责人)
        if (listAll != null && Processing.checkUserIsTeacher(request, roleDAO)) {
            List<ProjectDO> projectDOList = projectDAO.get(userId, listAll, tags, isFinish);
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
        } else {
            listAll = 0;
            List<ProjectDO> projectDOList = projectDAO.get(userId, listAll, tags, isFinish);
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

    }

    @Override
    public BaseResponse workget(
            Integer listAll,
            HttpServletRequest request,
            List<String> tags,
            List<Integer> isFinish,
            Integer is,
            Integer page,
            Integer pageSize
    ) {
        log.info("\t> 执行 Service 层 ProjectService.workget 方法");

        //获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);

        //根据标签查询
        if (tags != null && !tags.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.workget(userId, listAll, tags, isFinish, is);
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

        //根据状态查询
        if (isFinish != null && !isFinish.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.workget(userId, listAll, tags, isFinish, is);
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


        //判断是否是老师(项目负责人)
        if (listAll != null && Processing.checkUserIsTeacher(request, roleDAO)) {
            List<ProjectDO> projectDOList = projectDAO.workget(userId, listAll, tags, isFinish, is);
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
        } else {
            listAll = 0;
            List<ProjectDO> projectDOList = projectDAO.workget(userId, listAll, tags, isFinish, is);
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
    }


    @Override
    public BaseResponse getByName(String name) {
        log.info("\t> 执行 Service 层 ProjectService.getByName 方法");
        if (projectDAO.getByName(name) == null) {
            return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
        } else {
            return ResultUtil.success(projectDAO.getByName(name));
        }
    }

    @Override
    public BaseResponse projectDelete(HttpServletRequest request, List<Long> id) {
        log.info("\t> 执行 Service 层 ProjectService.projectDelete 方法");

        //判断用户是否为老师 或者 项目负责人
        if (!Processing.checkUserIsTeacher(request, roleDAO)) {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        }

        for (Long id1 : id) {
            if (!projectDAO.isPrincipalUser(Processing.getAuthHeaderToUserId(request), id1)) {
                return ResultUtil.error(ErrorCode.NOT_PERMISSION);
            }
            if (!projectDAO.projectDelete(id1)) {
                return ResultUtil.error(ErrorCode.DATABASE_DELETE_ERROR);
            }
        }
        return ResultUtil.success();
    }
}
