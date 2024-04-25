package com.jsl.oa.dao;
import com.google.gson.Gson;
import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.dodata.ProjectChildDO;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.ProjectModuleDO;
import com.jsl.oa.model.dodata.UserDO;
import com.jsl.oa.model.dodata.info.ProjectShowDO;
import com.jsl.oa.model.vodata.ProjectInfoVO;
import com.jsl.oa.model.vodata.ProjectChildAddVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectDAO {


    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;
    private final Gson gson;



    public void projectAdd(ProjectInfoVO projectAdd) {
        log.info("\t> 执行 DAO 层 ProjectDAO.projectAdd 方法");
        log.info("\t\t> 从 MySQL 获取数据");

        projectMapper.projectAdd(projectAdd);
    }



    public void projectWorkAdd(ProjectChildAddVO projectChildAddVO) {
        log.info("\t> 执行 DAO 层 ProjectDAO.projectWorkAdd 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        projectMapper.projectWorkAdd(projectChildAddVO);
    }



    public ProjectDO projectEdit(@NotNull ProjectDO projectEdit) {
        log.info("\t> 执行 DAO 层 ProjectDAO.projectEdit 方法");
        log.info("\t\t> 从 MySQL 更新数据");
        projectMapper.projectEdit(projectEdit);
        log.info("\t\t> 从 MySQL 获取数据");
        return projectMapper.getProjectById(projectEdit.getId());
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



    public List<ProjectDO> get(Long userId, List<String> tags, List<String> isFinish) {
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

        return projectMapper.get(userId);

    }

    public List<ProjectDO> workget(Long userId, List<String> tags, List<String> isFinish, Integer is) {
        log.info("\t> 执行 DAO 层 ProjectDAO.workGet 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        if (tags != null && !tags.isEmpty()) {
            return projectMapper.workgetByTags(userId, tags, is, isFinish);
        }

        if (isFinish != null && !isFinish.isEmpty()) {
            return projectMapper.workgetByIsfinish(userId, isFinish, is);
        }
        return projectMapper.workget(userId, is);

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


    public List<ProjectDO> tget(List<String> isFinish, List<String> tags) {
        log.info("DAO层tget");

        if (tags != null && !tags.isEmpty()) {
            return projectMapper.tgetBytags(tags, isFinish);
        }

        if (isFinish != null && !isFinish.isEmpty()) {
            return projectMapper.tgetByIsfinish(isFinish);
        }
        return projectMapper.getAllProject();
    }


    public List<ProjectDO> getProjectByPrincipalUser(Long uid) {
        log.info("\t> 执行 DAO 层 ProjectDAO.getProjectFromUser 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return projectMapper.getAllProjectByUserId(uid);
    }

    public List<ProjectChildDO> getAllProjectChildByUId(Long uid) {
        log.info("\t> 执行 DAO 层 ProjectDAO.getAllProjectChildByUId 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return projectMapper.getAllChildProjectByUserId(uid);
    }

    public List<ProjectModuleDO> getAllModuleByUId(Long uid) {
        log.info("\t> 执行 DAO 层 ProjectDAO.getAllModuleByUId 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return projectMapper.getAllModuleByUserId(uid);
    }


    public String getPrincipalUserFromProject(Long projectId) {

        if (projectId == null) {
            return "";
        }

        ProjectDO projectDO = projectMapper.getProjectById(projectId);

        if (projectDO == null) {
            return "项目负责人不存在";
        }

        UserDO userDO =  userMapper.getUserById(projectDO.getPrincipalId());

        if (userDO == null) {
            return "";
        }

        if (userDO.getNickname() == null) {
            return userDO.getUsername();
        } else {
            return userDO.getNickname();
        }
    }


    /**
     * @Description: 获取该用户在这个项目的所有上级管理用户（比如管理该用户的项目经理，管理该用户的项目负责人）
     * @Date: 2024/4/21
     * @Param pid:      项目id
     * @Param uid:      用户id
     **/
    public List<Long> getAllManagerUserByProject(Integer pid, Long uid) {

//        储存  该用户  的  管理用户  数组
        List<Long> managerUserIds = new ArrayList<>();
//        获取项目
        ProjectDO projectDO = projectMapper.getProjectById(Long.valueOf(pid));
        if (projectDO == null || projectDO.getPrincipalId() == null) {
            return new ArrayList<>();
        }
//         获取该用户
        UserDO userDO = userMapper.getUserById(uid);
        if (userDO == null || userDO.getUsername() == null) {
            return new ArrayList<>();
        }

//        获取用户负责的所有子模块
        List<ProjectModuleDO> projectModuleDOS = projectMapper.getAllModuleByUserId(uid);
//        去除子模块里 具有相同所属子系统 的数据（查询出来的数据里会出现多个子模块属于同一子系统情况）
        projectModuleDOS = projectModuleDOS.stream()
                .collect(Collectors.groupingBy(ProjectModuleDO::getProjectChildId))
                .values()
                .stream()
                .map(projectModuleDOS1 -> projectModuleDOS1.get(0))
                .collect(Collectors.toList());
//        获取这些子模块的上一级，子系统，添加对应负责人。
        for (ProjectModuleDO projectModuleDO: projectModuleDOS) {
            ProjectChildDO projectChildDO = projectMapper.
                    getProjectChildById(Math.toIntExact(projectModuleDO.getProjectChildId()));
            if (projectChildDO == null || projectChildDO.getPrincipalId() == null) {
                continue;
            }
            managerUserIds.add(projectChildDO.getPrincipalId());
        }
//        添加项目负责人
        managerUserIds.add(projectDO.getPrincipalId());
//        去重：去除管理用户数组里相同的人(子系统负责人可能项目，或者与项目负责人冲突)
        managerUserIds = managerUserIds.stream()
                .distinct().collect(Collectors.toList());
//        如果管理用户数组有自己，去除
        managerUserIds.removeIf(userId -> userId.equals(uid));

        return managerUserIds;
    }



}
