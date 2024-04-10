package com.jsl.oa.mapper;

import com.jsl.oa.model.dodata.TagProjectDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * TagMapper
 * <hr/>
 * 用于处理标签相关的请求, 包括获取标签列表、编辑标签信息等
 *
 * @since v1.0.0-SNAPSHOT
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Mapper
public interface TagMapper {

    /**
     * 获取标签列表
     * <hr/>
     * 该接口提供标签列表的查询功能。用户在成功登录后，可以请求此接口来获取标签列表.
     *
     * @param page 页码
     * @param limit 每页数量
     * @param order 排序
     */
    @Select("SELECT * FROM organize_oa.oa_project_tags ORDER BY ${order} LIMIT #{limit} OFFSET #{page}")
    List<TagProjectDO> getTagsProjectList(String order, Integer limit, Integer page);
}
