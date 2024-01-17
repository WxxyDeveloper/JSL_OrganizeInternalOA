package com.jsl.oa.dao;

import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.model.doData.ProjectCuttingDO;
import com.jsl.oa.model.voData.ProjectInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectDAO {

    private final ProjectMapper projectMapper;
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
}
