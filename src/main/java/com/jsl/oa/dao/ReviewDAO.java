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
        log.info("\t> 执行 DAO 层 ReviewDAO.selectAllReviewFromProject 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return reviewMapper.selectAllReviewFromProject(projectId);
    }

    public List<ReviewDO> selectApprovedResultReviewFromProject(Long projectId,
                                                                Integer result) {
        log.info("\t> 执行 DAO 层 ReviewDAO.selectApprovedResultReviewFromProject 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return reviewMapper.selectApprovedResultReviewFromProject(projectId,
                result);
    }

    public List<ReviewDO> selectReviewFromSubsystem(Long subsystemId) {
        log.info("\t> 执行 DAO 层 ReviewDAO.selectReviewFromSubsystem 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return reviewMapper.selectReviewFromSubsystem(subsystemId);
    }

    public List<ReviewDO> selectApprovedResultReviewsFromSubsystem(Long subsystemId,
                                                                   Integer result) {
        log.info("\t> 执行 DAO 层 ReviewDAO.selectApprovedResultReviewsFromSubsystem 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return reviewMapper.selectApprovedResultReviewsFromSubsystem(subsystemId,
                result);
    }

    public List<ReviewDO> selectReviewFromSubmodule(Long submoduleId) {
        log.info("\t> 执行 DAO 层 ReviewDAO.selectReviewFromSubmodule 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return reviewMapper.selectReviewFromSubmodule(submoduleId);
    }

    public void addReview(ReviewDO reviewDO) {
        log.info("\t> 执行 DAO 层 ReviewDAO.addReview 方法");
        log.info("\t\t> 从 MySQL 插入数据");
        reviewMapper.addReview(reviewDO);
    }


    public String getNameBySubproject(Long subId) {

        log.info("\t> 执行 DAO 层 ReviewDAO.getNameBySubproject 方法");

        if (subId != null) {
            log.info("\t\t> 从 MySQL 获取数据");
            return projectMapper.getProjectWorkById(subId).getName();
        }

        if (subId == null) {
            return "无";
        }

        return "";
    }

}


