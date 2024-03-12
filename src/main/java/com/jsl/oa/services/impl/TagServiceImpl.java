package com.jsl.oa.services.impl;

import com.jsl.oa.dao.TagDAO;
import com.jsl.oa.model.doData.TagProjectDO;
import com.jsl.oa.services.TagService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * TagServiceImpl
 * <hr/>
 * 用于处理标签相关的请求, 包括获取标签列表、编辑标签信息等
 *
 * @since v1.0.0-SNAPSHOT
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagDAO tagDAO;

    /**
     * 获取标签列表
     * <hr/>
     * 该接口提供标签列表的查询功能。用户在成功登录后，可以请求此接口来获取标签列表.
     *
     * @param page  页码
     * @param limit 每页数量
     * @return BaseResponse 返回标签列表
     */
    @Override
    public BaseResponse getTagsProjectList(Integer page, Integer limit, String order) {
        log.info("[Service] 请求 getTagsProjectList 接口");
        // 获取标签列表
        ArrayList<TagProjectDO> getTagList = tagDAO.getTagsProjectList(page, limit, order);
        return ResultUtil.success(getTagList);
    }
}
