package com.jsl.oa.mapper;

import com.jsl.oa.model.dodata.ReviewDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewMapper {

    @Select("SELECT * FROM organize_oa.oa_review WHERE project_id = #{projectId} "
            + "AND is_delete = 0")
    List<ReviewDO> selectAllReviewFromProject(Long projectId);

    @Select("SELECT * FROM organize_oa.oa_review WHERE project_id = #{projectId} "
            + "AND is_delete = 0 AND review_result = #{result}")
    List<ReviewDO> selectApprovedResultReviewFromProject(Long projectId, short result);

    @Select("SELECT * FROM organize_oa.oa_review WHERE "
            + "project_subsystem_id = #{subsystemId} AND is_delete = 0")
    List<ReviewDO> selectReviewFromSubsystem(Long subsystemId);

    @Select("SELECT * FROM organize_oa.oa_review WHERE "
            + "project_subsystem_id = #{subsystemId} "
            + "AND is_delete = 0 AND review_result = #{result}")
    List<ReviewDO> selectApprovedResultReviewsFromSubsystem(Long subsystemId, short result);

    @Select("SELECT * FROM organize_oa.oa_review WHERE "
            + "project_submodule_id = #{subsystemId} AND is_delete = 0")
    List<ReviewDO> selectReviewFromSubmodule(Long submoduleId);

    @Select("SELECT * FROM  organize_oa.oa_review WHERE id = #{id} AND is_delete = 0")
    ReviewDO selectReviewById(Long id);

    void updateReview(ReviewDO reviewDO);

    void addReview(ReviewDO reviewDO);

    @Select("SELECT * FROM  organize_oa.oa_review WHERE name = #{name} AND is_delete = 0")
    List<ReviewDO> selectReviewByName(String name);
}
