package com.jsl.oa.dao;

import com.jsl.oa.mapper.TagMapper;
import com.jsl.oa.model.dodata.TagProjectDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * TagDAO
 * <hr/>
 * 用于处理标签相关的请求, 包括获取标签列表、编辑标签信息等
 *
 * @since v1.0.0-SNAPSHOT
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TagDAO {
    private final TagMapper tagMapper;

    /**
     * 获取标签列表
     * <hr/>
     * 该接口提供标签列表的查询功能。用户在成功登录后，可以请求此接口来获取标签列表.
     *
     * @param page 页码
     * @param limit 每页数量
     * @param order 排序
     */
    public ArrayList<TagProjectDO> getTagsProjectList(Integer page, Integer limit, String order) {
        log.info("[DAO] 请求 getTagsProjectList 接口");
        // 获取标签列表
        page = (page - 1) * limit;
        return (ArrayList<TagProjectDO>) tagMapper.getTagsProjectList(order, limit, page);
    }
}
