package com.jsl.oa.mapper;
import com.jsl.oa.model.dodata.ProjectChildDO;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.ProjectModuleDO;
import com.jsl.oa.model.vodata.ProjectInfoVO;
import com.jsl.oa.model.vodata.ProjectModuleAddVO;
import com.jsl.oa.model.vodata.ProjectWorkSimpleVO;
import com.jsl.oa.model.vodata.ProjectChildAddVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProjectMapper {

    @Insert("insert into organize_oa.oa_project "
            + "(name, description, principal_id, cycle,files,complete_time,"
            + "dead_line,status,tags,work_load) "
            + "value (#{name},#{description},#{principalId},#{cycle},#{files}"
            + ",#{completeTime},#{deadLine},#{status},#{tags},#{workLoad})")
    void projectAdd(ProjectInfoVO projectAdd);

    @Insert("insert into organize_oa.oa_project_child (project_id,name, principal_id,"
            + " work_load, description, cycle,status,dead_line) "
            + "value (#{projectId},#{name},#{principalId},#{workLoad},"
            + "#{description},#{cycle},#{status},#{deadLine})")
    void projectWorkAdd(ProjectChildAddVO projectChildAddVO);

    @Insert("insert into organize_oa.oa_project_modules (project_child_id, name, principal_id,"
            + " work_load, description, status, dead_line,cycle) "
            + "value (#{projectChildId},#{name},#{principalId},#{workLoad},"
            + "#{description},#{status},#{deadLine},#{cycle})")
    void projectModuleAdd(ProjectModuleAddVO projectModuleAddVO);

    void projectEdit(ProjectDO projectEdit);

    @Select("select * from organize_oa.oa_project where id=#{id}")
    ProjectDO getProjectById(Long id);

    @Select("select * from organize_oa.oa_project where id=#{id}")
    ProjectDO tgetProjectById(Integer id);

    @Select("select * from organize_oa.oa_project where is_delete=false")
    List<ProjectDO> getAllProject();

    @Select("select data from organize_oa.oa_config where value='project_show'")
    String getHeader();

    @Insert("insert into organize_oa.oa_config(value, data, created_at)value ('project_show',null,NOW())")
    void insertProjectShow();

    @Update("UPDATE organize_oa.oa_config SET data = #{setProjectShow},"
            + " updated_at = CURRENT_TIMESTAMP WHERE value = 'project_show'")
    boolean setProjectShow(String setProjectShow);

    List<ProjectDO> getByIsfinish(Long userId, List<String> isFinish);

    List<ProjectDO> getByTags(Long userId, List<String> tags, List<String> isFinish);

    @Select("select * from organize_oa.oa_project where is_delete=false  and principal_id=#{userId}")
    List<ProjectDO> get(Long userId);

    @Select("select * from organize_oa.oa_project where status =1 and principal_id=#{userId}")
    List<ProjectDO> get1(Long userId);

    @Select("select * from organize_oa.oa_project where name=#{name}")
    ProjectDO getByName(String name);

    @Update("UPDATE organize_oa.oa_project SET is_delete = 1 where id=#{id}")
    boolean deleteProject(Long id);

    List<ProjectDO> workgetByIsfinish(Long userId, List<String> isFinish, Integer is);

    List<ProjectDO> workgetByTags(Long userId, List<String> tags, Integer is, List<String> isFinish);

    //@Select("select * from organize_oa.oa_project where id in(select project_id from " +
    //"organize_oa.oa_project_work where is_delete=false and status =1 and principal_id=#{userId} and type=0)")
    List<ProjectDO> workget(Long userId, Integer is);

    //@Select("select * from organize_oa.oa_project_work where status=1 and principal_id=#{userId}")
    List<ProjectDO> workget1(Long userId, Integer is);

    List<ProjectDO> tget(Integer id);

    List<ProjectDO> tgetByIsfinish(List<String> isFinish);

    List<ProjectDO> tgetBytags(List<String> tags, List<String> isFinish);

    @Select("select * from organize_oa.oa_project_child where id=#{id}")
    ProjectWorkSimpleVO getWorkById(Integer id);

    @Select("select * from organize_oa.oa_project_modules where id=#{id}")
    ProjectModuleDO getModuleById(Integer id);

    @Select("select principal_id from organize_oa.oa_project where id="
            + "(select project_id from organize_oa.oa_project_child where id=#{id})")
    Long getPirIdbyId(Long id);

    @Select("select principal_id from organize_oa.oa_project_child where id="
            + "(select project_child_id from organize_oa.oa_project_modules where id=#{id})")
    Long getPirTdByModuleId(Long id);


    @Select("select principal_id from organize_oa.oa_project_modules where id=#{id} ")
    Long getPid(Integer id);

    @Select("select * from organize_oa.oa_project_child where id=#{id} "
            + "AND is_delete = 0")
    ProjectModuleDO getProjectWorkById(Long id);

    @Select("select principal_id from organize_oa.oa_project_child where project_id=#{id} union "
            + "select principal_id from organize_oa.oa_project_modules where project_child_id in"
            + "(select id from organize_oa.oa_project_child where project_id = #{id})")
    List <Long> getMemberByProjectId(Integer id);

    @Select("select principal_id from organize_oa.oa_project_modules where project_child_id=#{id}")
    List <Long> getMemberBySystemId(Integer id);

    @Select("select * from organize_oa.oa_project_modules "
            + "where DATE(dead_line) = DATE(#{threeDayLater}) and status != 'complete' ")
    List<ProjectModuleDO> getProjectWorkByTime(LocalDateTime threeDayLater);


    @Select("select * from organize_oa.oa_project_modules "
            + "where is_delete = 0 and principal_id = #{uid}")
    List<ProjectModuleDO> getAllModuleByUserId(Long uid);

    @Select("select * from organize_oa.oa_project_child "
            + "where project_id = #{pid} and is_delete = 0 ")
    List<ProjectChildDO> getAllChildProjectByUserId(Long uid);

    @Select("select * from organize_oa.oa_project "
            + "where is_delete = 0 and principal_id = #{uid}")
    List<ProjectDO> getAllProjectByUserId(Long uid);


    @Select("select * from organize_oa.oa_project_child where "
            + "DATE (dead_line) = DATE (#{threeDaysLater}) and status != 'complete' ")
    List<ProjectChildDO> getProjectChildByTime(LocalDateTime threeDaysLater);


    @Select("select * from organize_oa.oa_project_child where "
            + "id = #{id} and is_delete = 0")
    ProjectChildDO getProjectChildById(Integer id);


    List<ProjectDO> getParticipateProject(Long userId);

    void deleteProjectChild(Long id1);

    void deleteProjectModule(Long id1);

    @Select("select * from organize_oa.oa_project where name like CONCAT('%',#{name},'%')")
    List<ProjectDO> getByLikeName(String name);


    @Select("select * from organize_oa.oa_project_child where project_id = "
            + "(select id from organize_oa.oa_project where name =#{name})")
    List<ProjectChildDO> getChildByLikeName(String name);

    @Select("select * from organize_oa.oa_project_modules where project_child_id = "
            + "(select id from organize_oa.oa_project_child where name =#{childName} "
            + "and project_id = (select id from organize_oa.oa_project where name =#{projectName}))")
    List<ProjectModuleDO> getModuleByName(String projectName, String childName);

    void projectModuleUpdate(ProjectModuleDO projectModuleDO);

    void projectChildEdit(ProjectChildDO projectChildDO);

    @Select("select * from organize_oa.oa_project_child where project_id = #{id} "
            + "and is_delete = 0")
    List<ProjectChildDO> getAllChildByProjectId(Integer id);

    @Select("select * from organize_oa.oa_project_modules where project_child_id = #{id} "
            + "and is_delete = 0")
    List<ProjectModuleDO> getModuleByChildId(Integer id);
}
