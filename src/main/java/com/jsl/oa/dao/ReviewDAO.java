package com.jsl.oa.dao;


import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.mapper.ReviewMapper;
import com.jsl.oa.model.dodata.ReviewDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ReviewDAO
 * <hr/>
 * 用于处理审核相关的请求, 包括获取审核列表、编辑审核信息等
 *
 * @author zrx_hhh
 * @version v1.0.0
 * @since v1.0.0-SNAPSHOT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewDAO {

    private final ReviewMapper reviewMapper;
    private final ProjectMapper projectMapper;


    public List<ReviewDO> selectAllReviewFromProject(Long projectId) {
        return reviewMapper.selectAllReviewFromProject(projectId);
    }

    public List<ReviewDO> selectApprovedResultReviewFromProject(Long projectId,
                                                                short result) {
        return reviewMapper.selectApprovedResultReviewFromProject(projectId,
                result);
    }

    public List<ReviewDO> selectReviewFromSubsystem(Long subsystemId) {
        return reviewMapper.selectReviewFromSubsystem(subsystemId);
    }

    public List<ReviewDO> selectApprovedResultReviewsFromSubsystem(Long subsystemId,
                                                                   short result) {
        return reviewMapper.selectApprovedResultReviewsFromSubsystem(subsystemId,
                result);
    }

    public List<ReviewDO> selectReviewFromSubmodule(Long submoduleId) {
        return reviewMapper.selectReviewFromSubmodule(submoduleId);
    }

    public List<ReviewDO> selectApprovedResultReviewsFromSubModule(Long id,
                                                                   short result) {
        return reviewMapper.selectApprovedResultReviewFromModule(id,
                result);
    }

    public List<ReviewDO> getReviewByUser(Long uid) {
        return reviewMapper.selectReviewByUser(uid);
    }

    public List<ReviewDO> getReviewByUserAndResult(Long uid, Short result) {
        return reviewMapper.selectReviewByUserAndResult(uid, result);
    }



    public void addReview(ReviewDO reviewDO) {
        reviewMapper.addReview(reviewDO);
    }

    public ReviewDO selectReviewById(Long id) {
        return reviewMapper.selectReviewById(id);
    }

    public void updateReview(ReviewDO  reviewDO) {
        reviewMapper.updateReview(reviewDO);
    }


    public String getNameByModule(Integer subId) {

        if (subId != null) {
            return projectMapper.getModuleById(subId).getName();
        }

        if (subId == null) {
            return "无";
        }

        return "";
    }

    public void deleteReviewByProjectId(Long pid) {
        List<ReviewDO> reviewDOS = reviewMapper.getAllReviewByProjectId(pid);
        for (ReviewDO reviewDO :reviewDOS) {
            reviewMapper.deleteReview(reviewDO.getId());
        }
    }

    public void deleteReviewByProjectChildId(Long cid) {
        List<ReviewDO> reviewDOS = reviewMapper.getAllReviewByProjectId(cid);
        for (ReviewDO reviewDO :reviewDOS) {
            reviewMapper.deleteReview(reviewDO.getId());
        }
    }

    public void deleteReviewByProjectModuleId(Long mid) {
        List<ReviewDO> reviewDOS = reviewMapper.getAllReviewByProjectId(mid);
        for (ReviewDO reviewDO :reviewDOS) {
            reviewMapper.deleteReview(reviewDO.getId());
        }
    }

}


