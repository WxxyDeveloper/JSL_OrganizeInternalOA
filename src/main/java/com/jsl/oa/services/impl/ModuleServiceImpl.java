package com.jsl.oa.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.ModuleMapper;
import com.jsl.oa.model.dodata.ProjectChildDO;
import com.jsl.oa.model.dodata.ProjectModuleDO;
import com.jsl.oa.model.vodata.ProjectChildGetVO;
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

import static java.lang.System.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final ProjectDAO projectDAO;
    private final ModuleMapper moduleMapper;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final Gson gson;

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
        List<ProjectChildDO> projectWorkDOList = moduleMapper.getByProjectId(projectId, userId, is);
        List<ProjectChildGetVO> projectWorkAndNameVOS = new ArrayList<>();
        for (ProjectChildDO projectWorkDO : projectWorkDOList) {
            ProjectChildGetVO projectWorkAndNameVO = new ProjectChildGetVO();
            Processing.copyProperties(projectWorkDO, projectWorkAndNameVO);
            //描述转换,负责人名字转换
            JsonObject jsonObject = gson.fromJson(projectWorkDO.getDescription(), JsonObject.class);
            projectWorkAndNameVO.setDescription(jsonObject.get("description").getAsString());
            if (projectWorkDO.getPrincipalId() != null) {
                projectWorkAndNameVO
                        .setPrincipalName(userDAO.getUserById(projectWorkDO.getPrincipalId()).getUsername());
            }
            projectWorkAndNameVOS.add(projectWorkAndNameVO);
        }
        return ResultUtil.success(projectWorkAndNameVOS);
    }

    @SuppressWarnings("checkstyle:Regexp")
    @Override
    public BaseResponse getBySysId(Long sysId, HttpServletRequest request) {
        log.info("SysService");
        //获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);
        //获取子系统负责人id
        Long pid = moduleMapper.getPidBySysid(sysId.intValue());
        //获取项目负责人id
        Long prid = moduleMapper.getPridBySysyid(sysId.intValue());
        //判断是否是子系统/项目负责人
        int is = 1;
        if (!pid.equals(userId) && !prid.equals(userId)) {
            is = 0;
        }

        List<ProjectModuleDO> projectWorkDOList = moduleMapper.getBySysId(sysId, userId, is);
        out.println(projectWorkDOList.size());
//      封装VO类
        List<ProjectWorkAndNameVO> projectWorkAndNameVOS = new ArrayList<>();
        for (ProjectModuleDO projectWorkDO : projectWorkDOList) {
            ProjectWorkAndNameVO projectWorkAndNameVO = new ProjectWorkAndNameVO();
            Processing.copyProperties(projectWorkDO, projectWorkAndNameVO);
            //描述转换，负责人名字转换
            JsonObject jsonObject = gson.fromJson(projectWorkDO.getDescription(), JsonObject.class);
            projectWorkAndNameVO.setDescription(jsonObject.get("description").getAsString());
            if (projectWorkDO.getPrincipalId() != null) {
                projectWorkAndNameVO
                        .setPrincipalUser(userDAO.getUserById(projectWorkDO.getPrincipalId()).getUsername());
            }
            projectWorkAndNameVOS.add(projectWorkAndNameVO);
        }
        return ResultUtil.success(projectWorkAndNameVOS);
    }


    @Override
    public BaseResponse deleteById(HttpServletRequest request, Long id) {
//        检测是否为管理员
        if (!Processing.checkUserIsConsole(request, roleDAO)) {
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
