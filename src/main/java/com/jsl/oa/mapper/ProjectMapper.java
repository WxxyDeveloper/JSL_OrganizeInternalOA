package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.ProjectCuttingDO;
import com.jsl.oa.model.doData.ProjectDO;
import com.jsl.oa.model.voData.ProjectInfoVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectMapper {

    @Insert("insert into organize_oa.oa_project " +
            "(name, description, introduction, core_code, git,type, reward) " +
            "value (#{name},#{description},#{introduction},#{coreCode},#{git},#{type},#{reward})")
    void projectAdd(ProjectInfoVO projectAdd);


    void projectEdit(ProjectInfoVO projectEdit);

    @Select("select * from organize_oa.oa_project where id=#{id}")
    ProjectDO getProjectById(Long id);

    @Select("select * from organize_oa.oa_project_cutting where id in" +
            "(select pid from organize_oa.oa_project_user where uid=#{uid})")
    List<ProjectCuttingDO> projectGetUserInCutting(Long uid);

    @Insert("insert into organize_oa.oa_project_user(uid, pid)value (#{uid},#{pid})")
    void projectAddUserInCutting(Long uid, Long pid);
}
