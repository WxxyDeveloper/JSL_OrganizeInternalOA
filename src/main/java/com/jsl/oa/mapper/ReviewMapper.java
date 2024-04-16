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

    @Select("SELECT * FROM organize_oa.oa_review WHERE project_id = #{projectId} "
            + "AND is_delete = 0 AND review_result = #{result}")
    List<ReviewDO> selectApprovedResultReviewFromProject(Long projectId, short result);

    @Select("SELECT * FROM organize_oa.oa_review WHERE "
            + "project_child_id = #{childId} AND is_delete = 0")
    List<ReviewDO> selectReviewFromSubsystem(Long childId);

    @Select("SELECT * FROM organize_oa.oa_review WHERE "
            + "project_child_id = #{childId} "
            + "AND is_delete = 0 AND review_result = #{result}")
    List<ReviewDO> selectApprovedResultReviewsFromSubsystem(Long childId, short result);

    @Select("SELECT * FROM organize_oa.oa_review WHERE "
            + "project_module_id = #{moduleId} AND is_delete = 0")
    List<ReviewDO> selectReviewFromSubmodule(Long moduleId);

    @Select("SELECT * FROM organize_oa.oa_review WHERE review_result = #{result} and "
            + "project_module_id = #{moduleId} AND is_delete = 0")
    List<ReviewDO> selectApprovedResultReviewFromModule(Long moduleId, short result);

    @Select("SELECT * FROM  organize_oa.oa_review WHERE id = #{id} AND is_delete = 0")
    ReviewDO selectReviewById(Long id);

    void updateReview(ReviewDO reviewDO);

    void addReview(ReviewDO reviewDO);

    @Select("SELECT * FROM  organize_oa.oa_review WHERE name = #{name} AND is_delete = 0")
    List<ReviewDO> selectReviewByName(String name);
}
