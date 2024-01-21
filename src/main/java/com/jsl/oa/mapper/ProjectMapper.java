package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.ProjectCuttingDO;
import com.jsl.oa.model.doData.ProjectDO;
import com.jsl.oa.model.voData.ProjectInfoVO;
import com.jsl.oa.utils.BaseResponse;
import org.apache.ibatis.annotations.*;

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

    @Select("select data from organize_oa.oa_config where value='project_show'")
    String getHeader();

    @Insert("insert into organize_oa.oa_config(value, data, created_at)value ('project_show',null,NOW())")
    void insertProjectShow();

    @Update("UPDATE organize_oa.oa_config SET data = #{setProjectShow} WHERE value = 'project_show'")
    boolean setProjectShow(String setProjectShow);

    @Select("select * from organize_oa.oa_permissions")
    List<ProjectDO> get();

    @Select("select * from organize_oa.oa_project where name=#{name}")
    ProjectDO getByName(String name);

    @Delete("DELETE FROM organize_oa.oa_project where id=#{id}")
    boolean deleteProject(Long id);

    @Insert("INSERT INTO organize_oa.oa_project_cutting (pid, name, tag, real_time) " +
            "VALUES (#{pid}, #{name}, #{tag}, #{realTime})")
    void projectCuttingAdd(ProjectCuttingDO projectCuttingDO);

}
