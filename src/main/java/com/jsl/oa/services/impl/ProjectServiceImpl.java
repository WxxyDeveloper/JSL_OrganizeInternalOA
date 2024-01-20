package com.jsl.oa.services.impl;

import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.ProjectCuttingDO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.doData.info.CarouselDO;
import com.jsl.oa.model.doData.info.ProjectShowDO;
import com.jsl.oa.model.voData.ProjectInfoVO;
import com.jsl.oa.model.voData.business.info.ProjectShowVO;
import com.jsl.oa.services.ProjectService;
import com.jsl.oa.services.UserService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDAO projectDAO;
    private final UserDAO userDAO;
    private final RoleMapper roleMapper;

    @Override
    public BaseResponse projectAdd(ProjectInfoVO projectAdd) {

        projectDAO.projectAdd(projectAdd);
        return ResultUtil.success("添加成功");
    }

    @Override
    public BaseResponse projectEdit(ProjectInfoVO projectEdit) {
        //判断项目是否存在
        if(projectDAO.isExistProject(projectEdit.getId())) {
            projectDAO.projectEdit(projectEdit);
            return ResultUtil.success("修改成功");
        }else return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
    }

    @Override
    public BaseResponse projectGetUserInCutting(Long uid) {
        if(userDAO.isExistUser(uid)) {
            List<ProjectCuttingDO> projectCuttingDOList =projectDAO.projectGetUserInCutting(uid);
            return ResultUtil.success(projectCuttingDOList);
        }
        else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse projectAddUserForCutting(Long uid, Long pid) {
        if(userDAO.isExistUser(uid)){
            projectDAO.projectAddUserForCutting(uid,pid);
            return ResultUtil.success();
        }
        return null;
    }

    @Override
    public BaseResponse getHeader(Integer id) {
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
    public BaseResponse addHeader(HttpServletRequest request, ProjectShowVO projectShowVO) {
        // 用户权限校验
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        // 获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);
        UserDO userDO = userDAO.getUserById(userId);
        // 获取展示信息
        ProjectShowDO projectShowDO = projectDAO.getHeader();
        // 添加展示
        ProjectShowDO.DataDO project_show = new ProjectShowDO.DataDO();
        project_show.setDisplayOrder(projectShowVO.getDisplayOrder())
                .setName(projectShowVO.getName())
                .setType(projectShowVO.getType())
                .setStatus(projectShowVO.getStatus())
                .setIsActive(projectShowVO.getIsActive())
                .setAuthor(userDO.getUsername())
                .setCreatedAt(new Timestamp(System.currentTimeMillis()).toString());
        projectShowDO.getData().add(project_show);
        // 保存展示
        if (projectDAO.setProjectShow(projectShowDO)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
    }

    @Override
    public BaseResponse delHeader(Integer id, HttpServletRequest request) {
        // 用户权限校验
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
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
    public BaseResponse editHeader(HttpServletRequest request, ProjectShowVO projectShowVO, Integer id) {
        // 用户权限校验
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
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


}
