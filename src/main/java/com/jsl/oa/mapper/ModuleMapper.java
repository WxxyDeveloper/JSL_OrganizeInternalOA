package com.jsl.oa.mapper;

import com.jsl.oa.model.dodata.ProjectChildDO;
import com.jsl.oa.model.dodata.ProjectModuleDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ModuleMapper {



    List<ProjectChildDO> getByProjectId(Integer projectId, Long userId, int is);

    List<ProjectModuleDO> getBySysId(Long sysId, Long userId, int is);

    @Select("select principal_id from organize_oa.oa_project where id=#{projectId}")
    Long getPidByProjectid(Integer projectId);

    @Select("select principal_id from organize_oa.oa_project_child where id=#{sysId}")
    Long getPidBySysid(Integer sysId);

    @Delete("DELETE FROM organize_oa.oa_project_work WHERE id = #{id}")
    void deleteMoudule(Long id);

    @Select("select * from organize_oa.oa_project_work where pid=#{id} and is_delete=0 and type=1 ")
    List<ProjectModuleDO> getAllMoudleByPid(Long id);

    @Select("select principal_id from organize_oa.oa_project where id=(select project_id "
            + "from organize_oa.oa_project_child where id = #{sysId})")
    Long getPridBySysyid(Integer sysId);
}
