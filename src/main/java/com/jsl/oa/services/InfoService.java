package com.jsl.oa.services;

import com.jsl.oa.model.voData.business.info.CarouselVO;
import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>信息服务接口</h1>
 * <hr/>
 * 信息服务接口，包含信息获取接口
 *
 * @author 筱锋xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
public interface InfoService {
    /**
     * <h2>添加轮播图</h2>
     * <hr/>
     * 添加轮播图
     *
     * @param request    请求
     * @param carouselVO 轮播图VO
     * @return {@link BaseResponse}
     */
    BaseResponse addHeaderImage(HttpServletRequest request, CarouselVO carouselVO);

    /**
     * <h2>编辑轮播图</h2>
     * <hr/>
     * 编辑轮播图
     *
     * @param request    请求
     * @param carouselVO 轮播图VO
     * @param id         轮播图ID
     * @return {@link BaseResponse}
     */
    BaseResponse editHeaderImage(HttpServletRequest request, CarouselVO carouselVO, Integer id);

    /**
     * <h2>获取轮播图</h2>
     * <hr/>
     * 获取轮播图
     *
     * @return {@link BaseResponse}
     */
    BaseResponse getHeaderImage(Integer id);

    /**
     * <h2>删除轮播图</h2>
     * <hr/>
     * 删除轮播图
     *
     * @param request 请求
     * @param id      轮播图ID
     * @return {@link BaseResponse}
     */
    BaseResponse delHeaderImage(HttpServletRequest request, Integer id);
}
