package com.jsl.oa.dao;
import com.google.gson.Gson;
import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.ProjectModuleDO;
import com.jsl.oa.model.dodata.info.ProjectShowDO;
import com.jsl.oa.model.vodata.ProjectEditVO;
import com.jsl.oa.model.vodata.ProjectInfoVO;
import com.jsl.oa.model.vodata.ProjectWorkVO;
import com.jsl.oa.utils.Processing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectDAO {

    private final ProjectMapper projectMapper;
    private final Gson gson;

    public void projectAdd(ProjectInfoVO projectAdd) {

        projectMapper.projectAdd(projectAdd);
    }

    public void projectWorkAdd(ProjectWorkVO projectWorkVO) {
        projectMapper.projectWorkAdd(projectWorkVO);
    }

    public ProjectDO projectEdit(@NotNull ProjectEditVO projectEdit, Long projectId) {
        ProjectDO projectDO = new ProjectDO();
        Processing.copyProperties(projectEdit, projectDO);
        projectDO.setId(projectId);
        projectMapper.projectEdit(projectDO);
        return projectMapper.getProjectById(projectId);
    }

    public boolean isExistProject(Long id) {
        return projectMapper.getProjectById(id) != null;
    }

    public ProjectShowDO getHeader() {
        String getProjectShowSql = projectMapper.getHeader();
        ProjectShowDO getProjectShow = null;
        if (getProjectShowSql != null && !"{}".equals(getProjectShowSql)) {
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

    private void sortProject(@NotNull ProjectShowDO projectShowDO) {
        for (int i = 0; i < projectShowDO.getData().size(); i++) {
            for (int j = 0; j < projectShowDO.getData().size(); j++) {
                if ("desc".equals(projectShowDO.getOrder())) {
                    if (projectShowDO.getData().get(i).getDisplayOrder()
                            > projectShowDO.getData().get(j).getDisplayOrder()) {
                        Collections.swap(projectShowDO.getData(), i, j);
                    }
                } else {
                    if (projectShowDO.getData().get(i).getDisplayOrder()
                            < projectShowDO.getData().get(j).getDisplayOrder()) {
                        Collections.swap(projectShowDO.getData(), i, j);
                    }
                }
            }
        }
    }

    public boolean setProjectShow(ProjectShowDO projectShowDO) {
        sortProject(projectShowDO);
        String setProjectShow = gson.toJson(projectShowDO);
        return projectMapper.setProjectShow(setProjectShow);
    }


    public ProjectDO getProjectById(Long id) {
        return projectMapper.getProjectById(id);
    }

    public List<ProjectDO> get(Long userId, List<String> tags, List<String> isFinish) {
        if (tags != null && !tags.isEmpty()) {
            return projectMapper.getByTags(userId, tags, isFinish);
        }

        if (isFinish != null && !isFinish.isEmpty()) {
            return projectMapper.getByIsfinish(userId, isFinish);
        }

        return projectMapper.get(userId);

    }

    public List<ProjectDO> workget(Long userId, List<String> tags, List<String> isFinish, Integer is) {
        if (tags != null && !tags.isEmpty()) {
            return projectMapper.workgetByTags(userId, tags, is, isFinish);
        }

        if (isFinish != null && !isFinish.isEmpty()) {
            return projectMapper.workgetByIsfinish(userId, isFinish, is);
        }
        return projectMapper.workget(userId, is);

    }

    public ProjectDO getByName(String name) {
        return projectMapper.getByName(name);
    }

    public boolean projectDelete(Long id) {
        return projectMapper.deleteProject(id);
    }

    public boolean isExistProjectById(Long id) {
        return projectMapper.getProjectById(id) != null;
    }


    public boolean isPrincipalUser(Long uid, Long projectId) {
        ProjectDO projectDO = projectMapper.getProjectById(projectId);
        return Objects.equals(uid, projectDO.getPrincipalId());
    }


    public List<ProjectDO> tget(List<String> isFinish, List<String> tags) {

        if (tags != null && !tags.isEmpty()) {
            return projectMapper.tgetBytags(tags, isFinish);
        }

        if (isFinish != null && !isFinish.isEmpty()) {
            return projectMapper.tgetByIsfinish(isFinish);
        }
        return projectMapper.getAllProject();
    }

    public ProjectModuleDO getProjectWorkerById(Long id) {
        return projectMapper.getProjectWorkById(id);
    }

    public List<ProjectDO> getProjectByPrincipalUser(Long uid) {
        return projectMapper.getProjectByPrincipalUser(uid);
    }

    public List<ProjectModuleDO> getAllSubsystemByUserId(Long uid) {
        return projectMapper.getAllSubsystemByUserId(uid);
    }

    public List<ProjectModuleDO> getAllSubmoduleByUserId(Long uid) {
        return projectMapper.getAllSubmoduleByUserId(uid);
    }

}
