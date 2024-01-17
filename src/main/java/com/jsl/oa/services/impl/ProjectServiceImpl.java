package com.jsl.oa.services.impl;

import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.model.doData.ProjectCuttingDO;
import com.jsl.oa.model.voData.ProjectInfoVO;
import com.jsl.oa.services.ProjectService;
import com.jsl.oa.services.UserService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDAO projectDAO;
    private final UserDAO userDAO;

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
}
