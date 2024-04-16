package com.jsl.oa.services.impl;

import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.ModuleMapper;
import com.jsl.oa.model.dodata.ProjectModuleDO;
import com.jsl.oa.model.vodata.ProjectWorkAndNameVO;
import com.jsl.oa.services.ModuleService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final ProjectDAO projectDAO;
    private final ModuleMapper moduleMapper;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

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

        List<ProjectModuleDO> projectWorkDOList = moduleMapper.getByProjectId(projectId, userId, is);
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

        List<ProjectModuleDO> projectWorkDOList = moduleMapper.getBySysId(sysId, userId, is);
        // 封装VO类
        List<ProjectWorkAndNameVO> projectWorkAndNameVOS = new ArrayList<>();
        for (ProjectModuleDO projectWorkDO : projectWorkDOList) {
            ProjectWorkAndNameVO projectWorkAndNameVO = new ProjectWorkAndNameVO();
            Processing.copyProperties(projectWorkDO, projectWorkAndNameVO);
            // 添加负责人和子系统名称
            projectWorkAndNameVO
                    .setChildSystemName(projectDAO.getProjectWorkerById(projectWorkDO.getProjectChildId()).getName())
                    .setPrincipalUser(userDAO.getUserById(projectWorkDO.getPrincipalId()).getUsername());

            projectWorkAndNameVOS.add(projectWorkAndNameVO);

        }
        return ResultUtil.success(projectWorkAndNameVOS);
    }


    @Override
    public BaseResponse deleteById(HttpServletRequest request, Long id) {
//        检测是否为管理员
        if (!Processing.checkUserIsAdmin(request, roleDAO)) {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION);
        }

        deleteMoudule(id);

        return ResultUtil.success("删除成功");
    }

    //    删除子模块方法
    public void deleteMoudule(Long id) {
        //获取所有父Id=id的子模块
        List<ProjectModuleDO> projectWorkDOS = moduleMapper.getAllMoudleByPid(id);

        for (ProjectModuleDO workDO : projectWorkDOS) {
            deleteMoudule(workDO.getId());
        }

        moduleMapper.deleteMoudule(id);
    }

}
