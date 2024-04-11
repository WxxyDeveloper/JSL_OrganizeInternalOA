package com.jsl.oa.mapper;
import com.jsl.oa.model.dodata.ProjectCuttingDO;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.ProjectWorkDO;
import com.jsl.oa.model.vodata.ProjectInfoVO;
import com.jsl.oa.model.vodata.ProjectWorkSimpleVO;
import com.jsl.oa.model.vodata.ProjectWorkVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProjectMapper {

    @Insert("insert into organize_oa.oa_project "
            + "(name, description, principal_id, cycle,file,complete_time,"
            + "deadline,status,is_finish,tags,work_load) "
            + "value (#{name},#{description},#{principalId},#{cycle},#{file}"
            + ",#{completeTime},#{deadline},#{status},#{isFinish},#{tags},#{workLoad})")
    void projectAdd(ProjectInfoVO projectAdd);

    @Insert("insert into organize_oa.oa_project_work (project_id, pid, name, principal_id,"
            + " work_load, description, cycle, complete_time, type, is_finish,status) "
            + "value (#{projectId},#{pid},#{name},#{principalId},#{workLoad},"
            + "#{description},#{cycle},#{completeTime},#{type},#{isFinish},#{status})")
    void projectWorkAdd(ProjectWorkVO projectWorkVO);

    void projectEdit(ProjectDO projectEdit);

    @Select("select * from organize_oa.oa_project where id=#{id}")
    ProjectDO getProjectById(Long id);

    @Select("select * from organize_oa.oa_project where id=#{id}")
    ProjectDO tgetProjectById(Integer id);

    @Select("select * from organize_oa.oa_project_work where principal_id=#{uid}")
        //"(select id from organize_oa.oa_project_work where id in)")
    List<ProjectCuttingDO> projectGetUserInCutting(Long uid);

    @Insert("update organize_oa.oa_project_work set principal_id =#{uid} where id=#{pid}")
    void projectAddUserInCutting(Long uid, Long pid);

    @Select("select data from organize_oa.oa_config where value='project_show'")
    String getHeader();

    @Insert("insert into organize_oa.oa_config(value, data, created_at)value ('project_show',null,NOW())")
    void insertProjectShow();

    @Update("UPDATE organize_oa.oa_config"
            + " SET data = #{setProjectShow}, updated_at = CURRENT_TIMESTAMP "
            + " WHERE value = 'project_show'"
    )
    boolean setProjectShow(String setProjectShow);

    List<ProjectDO> getByIsfinish(Long userId, List<Integer> isFinish);

    List<ProjectDO> getByTags(Long userId, List<String> tags, List<Integer> isFinish);

    @Select("select * from organize_oa.oa_project where is_delete=false and status=1 and principal_id=#{userId}")
    List<ProjectDO> get(Long userId);

    @Select("select * from organize_oa.oa_project where status =1 and principal_id=#{userId}")
    List<ProjectDO> get1(Long userId);

    @Select("select * from organize_oa.oa_project where name=#{name}")
    ProjectDO getByName(String name);

    @Update("UPDATE organize_oa.oa_project SET is_delete = 1 where id=#{id}")
    boolean deleteProject(Long id);

    @Update("UPDATE  organize_oa.oa_project_cutting SET name = #{name}, "
            + "tag = #{tag}, engineering = #{engineering}, estimated_time = #{estimatedTime}, "
            + "real_time = #{realTime}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    boolean projectCuttingUpdate(ProjectCuttingDO projectCuttingDO);
    @Update("UPDATE organize_oa.oa_project_user SET uid = #{uid} , updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    boolean updateUserForProjectUser(Long uid, Long id);



    List<ProjectDO> workgetByIsfinish(Long userId, List<Integer> isFinish, Integer is);

    List<ProjectDO> workgetByTags(Long userId, List<String> tags, Integer is, List<Integer> isFinish);

    //@Select("select * from organize_oa.oa_project where id in(select project_id from " +
    //"organize_oa.oa_project_work where is_delete=false and status =1 and principal_id=#{userId} and type=0)")
    List<ProjectDO> workget(Long userId, Integer is);

    //@Select("select * from organize_oa.oa_project_work where status=1 and principal_id=#{userId}")
    List<ProjectDO> workget1(Long userId, Integer is);


    List<ProjectDO> tget(Integer id);

    List<ProjectDO> tgetByIsfinish(List<Integer> isFinish);

    List<ProjectDO> tgetBytags(List<String> tags, List<Integer> isFinish);

    @Select("select * from organize_oa.oa_project_work where id=#{id}")
    ProjectWorkSimpleVO getWorkById(Integer id);

    @Select("select principal_id from organize_oa.oa_project_work where id=#{pid}")
    Long getPirIdbyWorkid(Long pid);

    @Select("select principal_id from organize_oa.oa_project_work where id=#{id}")
    Long getPid(Integer id);

    @Select("select * from organize_oa.oa_project_work where id=#{id}")
    ProjectWorkDO getProjectWorkById(Long id);

    @Select("select principal_id from organize_oa.oa_project_work where project_id=#{id}")
    List <Long> getMemberByProjectId(Integer id);

    @Select("select principal_id from organize_oa.oa_project_work where pid=#{id}")
    List <Long> getMemberBySystemId(Integer id);

    @Select("select * from organize_oa.oa_project_work "
            + "where DATE(deadline) = DATE(#{threeDayLater}) and is_finish != 1")
    List<ProjectWorkDO> getProjectWorkByTime(LocalDateTime threeDayLater);

}
