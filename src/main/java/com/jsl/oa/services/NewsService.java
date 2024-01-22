package com.jsl.oa.services;

import com.jsl.oa.model.voData.NewsAddVO;
import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>新闻服务接口</h1>
 * <hr/>
 * 用于新闻控制
 *
 * @author 张睿相
 * @version v1.1.0
 * @since v1.1.0
 */
public interface NewsService {

    BaseResponse newsAdd(NewsAddVO newsAddVO, HttpServletRequest request);

}
