package com.jsl.oa.dao;

import com.google.gson.Gson;
import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.model.doData.ProjectCuttingDO;
import com.jsl.oa.model.doData.ProjectDO;
import com.jsl.oa.model.doData.ProjectUserDO;
import com.jsl.oa.model.doData.info.ProjectShowDO;
import com.jsl.oa.model.voData.ProjectCuttingAddVO;
import com.jsl.oa.model.voData.ProjectInfoVO;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectDAO {

    private final ProjectMapper projectMapper;
    private final Gson gson;

    public void projectAdd(ProjectInfoVO projectAdd) {
        log.info("\t> 执行 DAO 层 ProjectDAO.projectAdd 方法");
        projectMapper.projectAdd(projectAdd);

    }

    public void projectEdit(ProjectInfoVO projectEdit) {
        log.info("\t> 执行 DAO 层 ProjectDAO.projectEdit 方法");
        projectMapper.projectEdit(projectEdit);
    }

    public boolean isExistProject(Long id) {
        log.info("\t> 执行 DAO 层 ProjectDAO.isExistProject 方法");
        return projectMapper.getProjectById(id) != null;
    }

    public List<ProjectCuttingDO> projectGetUserInCutting(Long uid) {
        log.info("\t> 执行 DAO 层 ProjectDAO.projectGetUserInCutting 方法");
        return projectMapper.projectGetUserInCutting(uid);
    }

    public void projectAddUserForCutting(Long uid, Long pid) {
        log.info("\t> 执行 DAO 层 ProjectDAO.projectAddUserForCutting 方法");
        projectMapper.projectAddUserInCutting(uid, pid);
    }

    public ProjectShowDO getHeader() {
        log.info("\t> 执行 DAO 层 ProjectDAO.getHeader 方法");
        String getProjectShowSql = projectMapper.getHeader();
        ProjectShowDO getProjectShow = null;
        if (getProjectShowSql != null && !getProjectShowSql.equals("{}")) {
            getProjectShow = gson.fromJson(getProjectShowSql, ProjectShowDO.class);
        }
        if (getProjectShow == null) {
            // 初始化
            getProjectShow = new ProjectShowDO();
            getProjectShow.setOrder("desc");
            getProjectShow.setData(new ArrayList<>());
            try {
                projectMapper.insertProjectShow();
            } catch (DuplicateKeyException ignored) {
            }
        }
        // 获取排序
        sortProject(getProjectShow);
        return getProjectShow;
    }

    private void sortProject(ProjectShowDO projectShowDO) {
        log.info("\t> 执行 DAO 层 ProjectDAO.sortProject 方法");
        for (int i = 0; i < projectShowDO.getData().size(); i++) {
            for (int j = 0; j < projectShowDO.getData().size(); j++) {
                if ("desc".equals(projectShowDO.getOrder())) {
                    if (projectShowDO.getData().get(i).getDisplayOrder() > projectShowDO.getData().get(j).getDisplayOrder()) {
                        Collections.swap(projectShowDO.getData(), i, j);
                    }
                } else {
                    if (projectShowDO.getData().get(i).getDisplayOrder() < projectShowDO.getData().get(j).getDisplayOrder()) {
                        Collections.swap(projectShowDO.getData(), i, j);
                    }
                }
            }
        }
    }

    public boolean setProjectShow(ProjectShowDO projectShowDO) {
        log.info("\t> 执行 DAO 层 ProjectDAO.setProjectShow 方法");
        sortProject(projectShowDO);
        String setProjectShow = gson.toJson(projectShowDO);
        return projectMapper.setProjectShow(setProjectShow);
    }

    public List<ProjectDO> get() {
        log.info("\t> 执行 DAO 层 ProjectDAO.get 方法");
        return projectMapper.get();
    }

    public ProjectDO getByName(String name) {
        log.info("\t> 执行 DAO 层 ProjectDAO.getByName 方法");
        return projectMapper.getByName(name);
    }

    public boolean projectDelete(Long id){
        log.info("\t> 执行 DAO 层 ProjectDAO.projectDelete 方法");
        return projectMapper.deleteProject(id);
    }

    public void projectCuttingAdd(ProjectCuttingDO projectCuttingDO){
        log.info("\t> 执行 DAO 层 ProjectDAO.projectCuttingAdd 方法");
        projectMapper.projectCuttingAdd(projectCuttingDO);
    }

    public boolean isExistProjectById(Long id){
        log.info("\t> 执行 DAO 层 ProjectDAO.isExistProjectById 方法");
        if(projectMapper.getProjectById(id) == null){
            return false;
        }else {return  true;}
    }

    public boolean updateProjectCutting(ProjectCuttingDO projectCuttingDO){
        log.info("\t> 执行 DAO 层 ProjectDAO.updateProjectCutting 方法");
        return projectMapper.projectCuttingUpdate(projectCuttingDO);
    }

    public boolean isExistProjectCutting(Long id){
        log.info("\t> 执行 DAO 层 ProjectDAO.isExistProjectCutting 方法");
        if(projectMapper.getProjectCuttingById(id) == null){
            return false;
        }else {return  true;}
    }

    public boolean isExistProjectUser(Long pid,Long uid){
        log.info("\t> 执行 DAO 层 ProjectDAO.isExistProjectUse 方法");
        if(projectMapper.getProjectUserByPidAndUid(pid,uid) == null){
            return false;
        }else {return  true;}
    }

    public boolean updateUserForProjectUserByPidAndUid(Long pid,Long oldUid,Long newUid){
        log.info("\t> 执行 DAO 层 ProjectDAO.updateUserForProjectUserByPidAndUid 方法");
        ProjectUserDO projectUserDO = projectMapper.getProjectUserByPidAndUid(pid,oldUid);
        if(projectUserDO == null){
            return false;
        }
        return projectMapper.updateUserForProjectUser(newUid,projectUserDO.getId());
    }

}
