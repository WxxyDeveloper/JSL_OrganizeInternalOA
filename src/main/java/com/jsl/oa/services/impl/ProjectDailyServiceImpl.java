package com.jsl.oa.services.impl;
/*
        张睿相   Java
*/

import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.ProjectDailyDAO;
import com.jsl.oa.exception.BusinessException;
import com.jsl.oa.model.dodata.ProjectDailyDO;
import com.jsl.oa.model.vodata.ProjectDailyAddVO;
import com.jsl.oa.services.ProjectDailyService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <h1>日志服务层实现类</h1>
 * <hr/>
 * 用于日志服务层的实现类
 *
 * @author xiao_lfeng | 176yunxuan | xiangZr-hhh
 * @version v1.1.0
 * @see com.jsl.oa.services.ProjectService
 * @since v1.1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectDailyServiceImpl implements ProjectDailyService {

    private final ProjectDAO projectDAO;

    private final ProjectDailyDAO projectDailyDAO;


    @Override
    public BaseResponse addDaily(ProjectDailyAddVO projectDailyAddVO, HttpServletRequest request) {

//        获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);
//        从请求体中获取项目id
        Long projectId = projectDailyAddVO.getProjectId();
//        检查项目是否存在
        if (!projectDAO.isExistProjectById(projectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_EXIST);
        }

//        赋值数据库表实体类相关属性
        ProjectDailyDO projectDailyDO = new ProjectDailyDO();
        Processing.copyProperties(projectDailyAddVO, projectDailyDO);
        projectDailyDO.setUserId(userId)
                .setDailyTime(new Date());

//        向数据库添加数据
        projectDailyDAO.addProjectDaily(projectDailyDO);

        return ResultUtil.success();
    }





}


