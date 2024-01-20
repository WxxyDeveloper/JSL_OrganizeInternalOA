package com.jsl.oa.dao;

import com.google.gson.Gson;
import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.model.doData.ProjectCuttingDO;
import com.jsl.oa.model.doData.info.CarouselDO;
import com.jsl.oa.model.doData.info.ProjectShowDO;
import com.jsl.oa.model.voData.ProjectInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectDAO {

    private final ProjectMapper projectMapper;
    private final Gson gson;

    public void projectAdd(ProjectInfoVO projectAdd) {
        projectMapper.projectAdd(projectAdd);

    }

    public void projectEdit(ProjectInfoVO projectEdit) {
        projectMapper.projectEdit(projectEdit);
    }

    public boolean isExistProject(Long id) {
        if(projectMapper.getProjectById(id)==null) {
            return false;
        }else return true;
    }

    public List<ProjectCuttingDO> projectGetUserInCutting(Long uid) {
        return projectMapper.projectGetUserInCutting(uid);
    }

    public void projectAddUserForCutting(Long uid, Long pid) {
         projectMapper.projectAddUserInCutting(uid,pid);
    }

    public ProjectShowDO getHeader() {
            String getProjectShowSql = projectMapper.getHeader();
            ProjectShowDO getProjectShow = null;
            if (!getProjectShowSql.equals("{}")) {
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
        for (int i = 0; i < projectShowDO.getData().size(); i++) {
            for (int j = 0; j < projectShowDO.getData().size(); j++) {
                if (projectShowDO.getOrder().equals("desc")) {
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
        sortProject(projectShowDO);
        String setProjectShow = gson.toJson(projectShowDO);
        return projectMapper.setProjectShow(setProjectShow);
    }
}
