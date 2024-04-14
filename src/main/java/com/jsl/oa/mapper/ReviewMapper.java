package com.jsl.oa.mapper;

import com.jsl.oa.model.dodata.ReviewDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReviewMapper {

    @Select("SELECT * FROM organize_oa.oa_review WHERE project_id = #{projectId} "
            + "AND is_delete = 0")
    List<ReviewDO> selectAllReviewFromProject(Long projectId);

    @Select("SELECT * FROM organize_oa.oa_review WHERE "
            + "project_subsystem_id = #{subsystemId} AND is_delete = 0")
    List<ReviewDO> selectReviewFromSubsystem(Long subsystemId);

    @Select("SELECT * FROM organize_oa.oa_review WHERE "
            + "project_submodule_id = #{subsystemId} AND is_delete = 0")
    List<ReviewDO> selectReviewFromSubmodule(Long submoduleId);

    @Select("SELECT * FROM  organize_oa.oa_review WHERE id = #{id} AND is_delete = 0")
    ReviewDO selectReviewById(Long id);


}
