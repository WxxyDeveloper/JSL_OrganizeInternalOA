package com.jsl.oa.dao;
import com.google.gson.Gson;
import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.ProjectWorkDO;
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
        log.info("\t> 执行 DAO 层 ProjectDAO.projectAdd 方法");
        log.info("\t\t> 从 MySQL 获取数据");


        projectMapper.projectAdd(projectAdd);
    }

    public void projectWorkAdd(ProjectWorkVO projectWorkVO) {
        log.info("\t> 执行 DAO 层 ProjectDAO.projecWorktAdd 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        projectMapper.projectWorkAdd(projectWorkVO);
    }

    public ProjectDO projectEdit(@NotNull ProjectEditVO projectEdit, Long projectId) {
        log.info("\t> 执行 DAO 层 ProjectDAO.projectEdit 方法");
        log.info("\t\t> 从 MySQL 更新数据");
        ProjectDO projectDO = new ProjectDO();
        Processing.copyProperties(projectEdit, projectDO);
        projectDO.setId(projectId);
        projectMapper.projectEdit(projectDO);
        log.info("\t\t> 从 MySQL 获取数据");
        return projectMapper.getProjectById(projectId);
    }

    public boolean isExistProject(Long id) {
        log.info("\t> 执行 DAO 层 ProjectDAO.isExistProject 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return projectMapper.getProjectById(id) != null;
    }

    public ProjectShowDO getHeader() {
        log.info("\t> 执行 DAO 层 ProjectDAO.getHeader 方法");
        log.info("\t\t> 从 MySQL 获取数据");
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
                log.info("\t\t> 从 MySQL 插入数据");
                projectMapper.insertProjectShow();
            } catch (DuplicateKeyException ignored) {
            }
        }
        // 获取排序
        sortProject(getProjectShow);
        return getProjectShow;
    }

    private void sortProject(@NotNull ProjectShowDO projectShowDO) {
        log.info("\t> 执行 DAO 层 ProjectDAO.sortProject 方法");
        for (int i = 0; i < projectShowDO.getData().size(); i++) {
            for (int j = 0; j < projectShowDO.getData().size(); j++) {
                ProjectShowDO.DataDO dataDO = projectShowDO.getData().get(i);
                if ("desc".equals(projectShowDO.getOrder())) {
                    if (dataDO.getDisplayOrder() > projectShowDO.getData().get(j).getDisplayOrder()) {
                        Collections.swap(projectShowDO.getData(), i, j);
                    }
                } else {
                    if (dataDO.getDisplayOrder() < projectShowDO.getData().get(j).getDisplayOrder()) {
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
        log.info("\t\t> 从 MySQL 获取数据");
        return projectMapper.setProjectShow(setProjectShow);
    }


    public ProjectDO getProjectById(Long id) {
        log.info("\t> 执行 DAO 层 ProjectDAO.getProjectById 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return projectMapper.getProjectById(id);
    }

    public List<ProjectDO> get(Long userId, Integer listAll, List<String> tags, List<Integer> isFinish) {
        log.info("\t> 执行 DAO 层 ProjectDAO.get 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        if (tags != null && !tags.isEmpty()) {
            log.info("tags");
            return projectMapper.getByTags(userId, tags, isFinish);
        }

        if (isFinish != null && !isFinish.isEmpty()) {
            log.info("finish");
            return projectMapper.getByIsfinish(userId, isFinish);
        }

        if (listAll == 0) {
            return projectMapper.get(userId);
        } else {
            return projectMapper.get1(userId);
        }
    }

    public List<ProjectDO> workget(Long userId, Integer listAll,
                                   List<String> tags, List<Integer> isFinish, Integer is) {
        log.info("\t> 执行 DAO 层 ProjectDAO.workget 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        if (tags != null && !tags.isEmpty()) {
            return projectMapper.workgetByTags(userId, tags, is, isFinish);
        }

        if (isFinish != null && !isFinish.isEmpty()) {
            return projectMapper.workgetByIsfinish(userId, isFinish, is);
        }

        if (listAll == 0) {
            return projectMapper.workget(userId, is);
        } else {
            return projectMapper.workget1(userId, is);
        }
    }

    public ProjectDO getByName(String name) {
        log.info("\t> 执行 DAO 层 ProjectDAO.getByName 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return projectMapper.getByName(name);
    }

    public boolean projectDelete(Long id) {
        log.info("\t> 执行 DAO 层 ProjectDAO.projectDelete 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return projectMapper.deleteProject(id);
    }

    public boolean isExistProjectById(Long id) {
        log.info("\t> 执行 DAO 层 ProjectDAO.isExistProjectById 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return projectMapper.getProjectById(id) != null;
    }


    public boolean isPrincipalUser(Long uid, Long projectId) {
        log.info("\t> 执行 DAO 层 ProjectDAO.isPrincipalUser 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        ProjectDO projectDO = projectMapper.getProjectById(projectId);
        return Objects.equals(uid, projectDO.getPrincipalId());
    }


    public List<ProjectDO> tget(Integer id, List<Integer> isFinish, List<String> tags) {
        log.info("DAO层tget");

        if (tags != null && !tags.isEmpty()) {
            return projectMapper.tgetBytags(tags, isFinish);
        }

        if (isFinish != null && !isFinish.isEmpty()) {
            return projectMapper.tgetByIsfinish(isFinish);
        }

        return projectMapper.tget(id);
    }

    public ProjectWorkDO getProjectWorkerById(Long id) {
        return projectMapper.getProjectWorkById(id);
    }


}
