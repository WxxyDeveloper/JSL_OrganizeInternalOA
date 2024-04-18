package com.jsl.oa.services;



import com.jsl.oa.model.vodata.ProjectDailyAddVO;
import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * 项目日报(ProjectDaily)表服务接口
 *
 * @author zrx
 * @since 2024-04-18 11:40:53
 */
public interface ProjectDailyService {


    BaseResponse addDaily(ProjectDailyAddVO projectDailyAddVO, HttpServletRequest request);

    BaseResponse getMyDaily(Integer page, Integer pageSize, HttpServletRequest request);

    BaseResponse searchMyDaily(Integer page,
                               Integer pageSize,
                               Date beginTime,
                               Date endTime,
                               HttpServletRequest request);
}


