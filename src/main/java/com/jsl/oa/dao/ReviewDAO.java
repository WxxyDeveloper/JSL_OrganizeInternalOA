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

    public List<ReviewDO> getPrincipalUserReview(Long pid) {
        return reviewMapper.selectAllReviewFromProject(pid);
    }

    public String getNameBySubproject(Long subId) {

        if (subId != null) {
            return projectMapper.getProjectWorkById(subId).getName();
        }

        if (subId == null) {
            return "无";
        }

        return "";
    }

}


