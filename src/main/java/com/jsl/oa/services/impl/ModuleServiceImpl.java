package com.jsl.oa.services.impl;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.jsl.oa.dao.ModuleDAO;
import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.ModuleMapper;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.ProjectDO;
import com.jsl.oa.model.doData.ProjectWorkDO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.ProjectWorkAndNameVO;
import com.jsl.oa.model.voData.ProjectWorkVO;
import com.jsl.oa.services.ModuleService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final RoleMapper roleMapper;
    private final ModuleDAO moduleDAO;
    private final ProjectDAO projectDAO;
    private final ModuleMapper moduleMapper;
    private final UserDAO userDAO;

    @Override
    public BaseResponse getByProjectId(Integer projectId, HttpServletRequest request) {
        log.info("projectService");
        //获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);
        //获取项目负责人id
        Long pid = moduleMapper.getPidByProjectid(projectId);
        //判断是否是项目负责人
        int is = 1;
        if (!pid.equals(userId)) {
            log.info("不是负责人");
            is = 0;
        }

        List<ProjectWorkDO> projectWorkDOList = moduleMapper.getByProjectId(projectId, userId, is);
        return ResultUtil.success(projectWorkDOList);
    }

    @Override
    public BaseResponse getBySysId(Integer sysId, HttpServletRequest request) {
        log.info("SysService");
        //获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);
        //获取子系统负责人id
        Long pid = moduleMapper.getPidBySysid(sysId);
        //获取项目负责人id
        Long prid = moduleMapper.getPridBySysyid(sysId);
        //判断是否是子系统/项目负责人
        int is = 1;
        if (!pid.equals(userId) && !prid.equals(userId)) {
            is = 0;
        }

        List<ProjectWorkDO> projectWorkDOList = moduleMapper.getBySysId(sysId, userId, is);
//      封装VO类
        List<ProjectWorkAndNameVO> projectWorkAndNameVOS = new ArrayList<>();
        for (ProjectWorkDO projectWorkDO : projectWorkDOList) {
            ProjectWorkAndNameVO projectWorkAndNameVO = new ProjectWorkAndNameVO();
            Processing.copyProperties(projectWorkDO,projectWorkAndNameVO);
//        添加负责人和子系统名称
            projectWorkAndNameVO.
                    setChildSystemName(projectDAO.getProjectWorkerById(projectWorkDO.getPid()).getName())
                    .setPrincipalUser(userDAO.getUserById(projectWorkDO.getPrincipalId()).getUsername());

            projectWorkAndNameVOS.add(projectWorkAndNameVO);

        }
        return ResultUtil.success(projectWorkAndNameVOS);
    }


    @Override
    public BaseResponse deleteById(HttpServletRequest request, Long id) {

//        检测是否为管理员
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        }

        deleteMoudule(id);

        return ResultUtil.success("删除成功");
    }

    //    删除子模块方法
    public void deleteMoudule(Long id) {
        //获取所有父Id=id的子模块
        List<ProjectWorkDO> projectWorkDOS = moduleMapper.getAllMoudleByPid(id);

        for (ProjectWorkDO workDO : projectWorkDOS) {
            deleteMoudule(workDO.getId());
        }

        moduleMapper.deleteMoudule(id);
    }

}
