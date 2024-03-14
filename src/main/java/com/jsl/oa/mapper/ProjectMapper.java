package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.ProjectCuttingDO;
import com.jsl.oa.model.doData.ProjectDO;
import com.jsl.oa.model.doData.ProjectUserDO;
import com.jsl.oa.model.voData.ProjectInfoVO;
import com.jsl.oa.model.voData.ProjectWorkVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProjectMapper {

    @Insert("insert into organize_oa.oa_project " +
            "(name, description, principal_id, cycle,file,complete_time," +
            "deadline,status,is_finish) " +
            "value (#{name},#{description},#{principalId},#{cycle},#{file}" +
            ",#{completeTime},#{deadline},#{status},#{isFinish})")
    void projectAdd(ProjectInfoVO projectAdd);

    @Insert("insert into organize_oa.oa_project_work (project_id, pid, name, principal_id," +
            " work_load, description, cycle, complete_time, type, is_finish,status) " +
            "value (#{projectId},#{pid},#{name},#{principalId},#{workLoad}," +
            "#{description},#{cycle},#{completeTime},#{type},#{isFinish},#{status})")
    void projectWorkAdd(ProjectWorkVO projectWorkVO);

    void projectEdit(ProjectDO projectEdit);

    @Select("select * from organize_oa.oa_project where id=#{id}")
    ProjectDO getProjectById(Long id);

    @Select("select * from organize_oa.oa_project_work where principal_id=#{uid}")
            //"(select id from organize_oa.oa_project_work where id in)")
    List<ProjectCuttingDO> projectGetUserInCutting(Long uid);

    @Insert("update organize_oa.oa_project_work set principal_id =#{uid} where id=#{pid}")
    void projectAddUserInCutting(Long uid, Long pid);

    @Select("select data from organize_oa.oa_config where value='project_show'")
    String getHeader();

    @Insert("insert into organize_oa.oa_config(value, data, created_at)value ('project_show',null,NOW())")
    void insertProjectShow();

    @Update("UPDATE organize_oa.oa_config SET data = #{setProjectShow}, updated_at = CURRENT_TIMESTAMP WHERE value = 'project_show'")
    boolean setProjectShow(String setProjectShow);

    //@Select("select * from organize_oa.oa_project where json_extract(tags,'$.tags')" +
            //"like concat('%',#{tags},'%')")

    //@Select("select * from organize_oa.oa_project where is_finish=#{isFinish} and is_delete=false and principal_id=#{userId}")
    List<ProjectDO>getByIsfinish(Long userId,List<Integer> isFinish);

    List<ProjectDO>getByTags(Long userId,List<String> tags);

    @Select("select * from organize_oa.oa_project where is_delete=false and status=1 and principal_id=#{userId}")
    List<ProjectDO> get(Long userId);

    @Select("select * from organize_oa.oa_project where status =1 and principal_id=#{userId}")
    List<ProjectDO> get1(Long userId);

    @Select("select * from organize_oa.oa_project where name=#{name}")
    ProjectDO getByName(String name);

    @Update("UPDATE organize_oa.oa_project SET is_delete = 1 where id=#{id}")
    boolean deleteProject(Long id);

    @Insert("INSERT INTO organize_oa.oa_project_cutting (pid, name, tag, real_time) " +
            "VALUES (#{pid}, #{name}, #{tag}, #{realTime})")
    void projectCuttingAdd(ProjectCuttingDO projectCuttingDO);

    @Update("UPDATE  organize_oa.oa_project_cutting SET name = #{name}, " +
            "tag = #{tag}, engineering = #{engineering}, estimated_time = #{estimatedTime}, " +
            "real_time = #{realTime}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    boolean projectCuttingUpdate(ProjectCuttingDO projectCuttingDO);

    @Select("SELECT * FROM organize_oa.oa_project_cutting WHERE id = #{id}")
    ProjectCuttingDO getProjectCuttingById(Long id);

    @Select("SELECT * FROM organize_oa.oa_project_user WHERE pid = #{pid} AND uid = #{uid}")
    ProjectUserDO getProjectUserByPidAndUid(Long pid, Long uid);

    @Update("UPDATE organize_oa.oa_project_user SET uid = #{uid} , updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    boolean updateUserForProjectUser(Long uid, Long id);


    //@Select("select * from organize_oa.oa_project_work where is_finish=#{isFinish} and is_delete=false and principal_id =#{userId}")
    List<ProjectDO> workgetByIsfinish(Long userId, List<Integer> isFinish, Integer is);

    List<ProjectDO> workgetByTags(Long userId, List<String> tags, Integer is);

    //@Select("select * from organize_oa.oa_project where id in(select project_id from " +
            //"organize_oa.oa_project_work where is_delete=false and status =1 and principal_id=#{userId} and type=0)")
    List<ProjectDO> workget(Long userId, Integer is);

    //@Select("select * from organize_oa.oa_project_work where status=1 and principal_id=#{userId}")
    List<ProjectDO> workget1(Long userId, Integer is);


    List<ProjectDO> tget(Integer id);

    List<ProjectDO> tgetByIsfinish(List<Integer> isFinish);

    List<ProjectDO> tgetBytags(List<String> tags);
}
