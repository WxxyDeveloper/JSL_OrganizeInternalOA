package com.jsl.oa.services;

import com.jsl.oa.utils.BaseResponse;

/**
 * TagService
 * <hr/>
 * 用于处理标签相关的请求, 包括获取标签列表、编辑标签信息等
 *
 * @since v1.0.0-SNAPSHOT
 * @version v1.0.0
 * @author xiao_lfeng
 */
public interface TagService {
    /**
     * 获取标签列表
     * <hr/>
     * 该接口提供标签列表的查询功能。用户在成功登录后，可以请求此接口来获取标签列表.
     *
     * @param page  页码
     * @param limit 每页数量
     * @return BaseResponse 返回标签列表
     */
    BaseResponse getTagsProjectList(Integer page, Integer limit, String order);
}
