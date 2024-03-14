package com.jsl.oa.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsl.oa.annotations.CheckUserHasPermission;
import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.ProjectCuttingDO;
import com.jsl.oa.model.doData.ProjectDO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.doData.info.ProjectShowDO;
import com.jsl.oa.model.voData.*;
import com.jsl.oa.model.voData.business.info.ProjectShowVO;
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

    private final RoleMapper roleMapper;
    private final ProjectDAO projectDAO;
    private final UserDAO userDAO;

    @Override
    @CheckUserHasPermission("project.add")
    public BaseResponse projectAdd(HttpServletRequest request, ProjectInfoVO projectAdd) {
        log.info("\t> 执行 Service 层 ProjectService.projectAdd 方法");
        projectDAO.projectAdd(projectAdd);
        return ResultUtil.success("添加成功");
    }

    @Override
    public BaseResponse projecWorktAdd(HttpServletRequest request, ProjectWorkVO projectWorkVO) {
        log.info("\t> 执行 Service 层 ProjectService.projectWorkAdd 方法");
        projectDAO.projectWorkAdd(projectWorkVO);
        return ResultUtil.success("添加成功");
    }

    @Override
    public BaseResponse tget(Integer id, List<String> tags, List<Integer> isFinish) {
        log.info("\t> 执行 Service 层 ProjectService.tget 方法");
        //根据状态查询
        if (isFinish != null && !isFinish.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.tget(id,isFinish,tags);
            return ResultUtil.success(projectDOList);
        }
        //根据标签查询
        if (tags != null && !tags.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.tget(id,isFinish,tags);
            return ResultUtil.success(projectDOList);
        }

        List<ProjectDO> projectDOList = projectDAO.tget(id,isFinish,tags);
        return ResultUtil.success(projectDOList);
    }

    @Override
    public BaseResponse projectFileGet(HttpServletRequest request, Long projectId) {


//        判断项目是否存在
        if(!projectDAO.isExistProjectById(projectId)){
            return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
        }

        ProjectDO projectDO = projectDAO.getProjectById(projectId);

        if(projectDO.getFile() == null || projectDO.getFile().equals("{}")){
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
    public BaseResponse projectEdit(HttpServletRequest request, @NotNull ProjectEditVO projectEdit, Long projectId) {
        log.info("\t> 执行 Service 层 ProjectService.projectEdit 方法");


        //判断用户是否为老师 或者 项目负责人
        if (!Processing.checkUserIsTeacher(request, roleMapper) ||
                !projectDAO.isPrincipalUser(Processing.getAuthHeaderToUserId(request), projectId)) {
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
    @CheckUserHasPermission("project.cutting.user.get")
    public BaseResponse projectGetUserInCutting(Long uid) {
        log.info("\t> 执行 Service 层 ProjectService.projectGetUserInCutting 方法");
        if (userDAO.isExistUser(uid)) {
            List<ProjectCuttingDO> projectCuttingDOList = projectDAO.projectGetUserInCutting(uid);
            return ResultUtil.success(projectCuttingDOList);
        } else {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
    }

    @Override
    @CheckUserHasPermission("project.cutting.user.add")
    public BaseResponse projectAddUserForCutting(HttpServletRequest request, Long uid, Long pid) {
        log.info("\t> 执行 Service 层 ProjectService.projectAddUserForCutting 方法");
        if (userDAO.isExistUser(uid)) {
            projectDAO.projectAddUserForCutting(uid, pid);
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
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
    public BaseResponse get(Integer listAll, HttpServletRequest request, List<String> tags, List<Integer> isFinish) {
        log.info("\t> 执行 Service 层 ProjectService.get 方法");

        //获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);
        //根据状态查询
        if (isFinish != null && !isFinish.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.get(userId, listAll, tags, isFinish);
            return ResultUtil.success(projectDOList);
        }
        //根据标签查询
        if (tags != null && !tags.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.get(userId, listAll, tags, isFinish);
            return ResultUtil.success(projectDOList);
        }

        //判断是否是老师(项目负责人)
        if (listAll != null && Processing.checkUserIsTeacher(request, roleMapper)) {
            List<ProjectDO> projectDOList = projectDAO.get(userId, listAll, tags, isFinish);
            return ResultUtil.success(projectDOList);
        } else {
            listAll = 0;
            List<ProjectDO> projectDOList = projectDAO.get(userId, listAll, tags, isFinish);
            return ResultUtil.success(projectDOList);
        }

    }

    @Override
    public BaseResponse workget(Integer listAll, HttpServletRequest request, List<String> tags, List<Integer> isFinish, Integer is) {
        log.info("\t> 执行 Service 层 ProjectService.workget 方法");

        //获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);
        //根据状态查询
        if (isFinish != null && !isFinish.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.workget(userId, listAll, tags, isFinish,is);
            return ResultUtil.success(projectDOList);
        }
        //根据标签查询
        if (tags != null && !tags.isEmpty()) {
            List<ProjectDO> projectDOList = projectDAO.workget(userId, listAll, tags, isFinish,is);
            return ResultUtil.success(projectDOList);
        }

        //判断是否是老师(项目负责人)
        if (listAll != null && Processing.checkUserIsTeacher(request, roleMapper)) {
            List<ProjectDO> projectDOList = projectDAO.workget(userId, listAll, tags, isFinish,is);
            return ResultUtil.success(projectDOList);
        } else {
            listAll = 0;
            List<ProjectDO> projectDOList = projectDAO.workget(userId, listAll, tags, isFinish,is);
            return ResultUtil.success(projectDOList);
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
    public BaseResponse projectDelete(HttpServletRequest request, Long id) {
        log.info("\t> 执行 Service 层 ProjectService.projectDelete 方法");

        //判断用户是否为老师 或者 项目负责人 或管理员
        if (!Processing.checkUserIsTeacher(request, roleMapper) &&
                !projectDAO.isPrincipalUser(Processing.getAuthHeaderToUserId(request), id)
                && !Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        }

        if (!projectDAO.projectDelete(id)) {
            return ResultUtil.error(ErrorCode.DATABASE_DELETE_ERROR);
        } else {
            return ResultUtil.success();
        }
    }

    @Override
    @CheckUserHasPermission("project.cutting.add")
    public BaseResponse addProjectCutting(HttpServletRequest request, ProjectCuttingAddVO projectCuttingAddVO) {
        log.info("\t> 执行 Service 层 ProjectService.projectCuttingAdd方法");
        //赋值数据
        ProjectCuttingDO projectCuttingDO = new ProjectCuttingDO();
        Processing.copyProperties(projectCuttingAddVO, projectCuttingDO);
        //根据pid检测项目是否存在
        if (!projectDAO.isExistProjectById(projectCuttingAddVO.getPid())) {
            return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
        }
        //向数据库添加数据
        projectDAO.projectCuttingAdd(projectCuttingDO);
        return ResultUtil.success();
    }

    @Override
    @CheckUserHasPermission("project.cutting.edit")
    public BaseResponse editProjectCutting(HttpServletRequest request, ProjectCuttingEditVO projectCuttingEditVO) {
        log.info("\t> 执行 Service 层 ProjectService.projectCuttingEdit方法");
        //赋值数据
        ProjectCuttingDO projectCuttingDO = new ProjectCuttingDO();
        Processing.copyProperties(projectCuttingEditVO, projectCuttingDO);
        //根据id检测项目模块是否存在
        if (!projectDAO.isExistProjectCutting(projectCuttingEditVO.getId())) {
            return ResultUtil.error(ErrorCode.PROJECT_CUTTING_NOT_EXIST);
        }
        //向数据库添加数据
        projectDAO.updateProjectCutting(projectCuttingDO);
        return ResultUtil.success();
    }

    @Override
    @CheckUserHasPermission("project.cutting.delete")
    public BaseResponse projectToOtherUserForCutting(HttpServletRequest request, Long oldUid, Long pid, Long newUid) {
        log.info("\t> 执行 Service 层 ProjectService.projectToOtherUserForCutting方法");
        //检测新旧用户是否存在
        if (!userDAO.isExistUser(oldUid) || !userDAO.isExistUser(newUid)) {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
        //用户项目表是否含有对应记录
        if (!projectDAO.isExistProjectUser(pid, oldUid)) {
            return ResultUtil.error(ErrorCode.PROJECT_USER_NOT_EXIST);
        }
        //更新数据
        if (!projectDAO.updateUserForProjectUserByPidAndUid(pid, oldUid, newUid)) {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
        return ResultUtil.success();
    }


}
