package com.jsl.oa.services.impl;
/*
        张睿相   Java
*/

import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.ProjectDailyDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.model.dodata.ProjectDailyDO;
import com.jsl.oa.model.vodata.ProjectDailyAddVO;
import com.jsl.oa.model.vodata.ProjectDailyDataVO;
import com.jsl.oa.model.vodata.ProjectDailyVO;
import com.jsl.oa.services.ProjectDailyService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private final UserDAO userDAO;
    private final ProjectDailyDAO projectDailyDAO;


    @Override
    public BaseResponse addDaily(ProjectDailyAddVO projectDailyAddVO, HttpServletRequest request) {

//        获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);
//        从请求体中获取项目id
        Integer projectId = projectDailyAddVO.getProjectId();
//        检查项目是否存在
        if (!projectDAO.isExistProjectById(Long.valueOf(projectId))) {
            return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
        }

//        赋值数据库表实体类相关属性
        ProjectDailyDO projectDailyDO = new ProjectDailyDO();
        Processing.copyProperties(projectDailyAddVO, projectDailyDO);
        projectDailyDO.setUserId(userId);


//        向数据库添加数据
        projectDailyDAO.addProjectDaily(projectDailyDO);

        return ResultUtil.success();
    }


    @Override
    public BaseResponse getMyDaily(Integer page, Integer pageSize, HttpServletRequest request) {

//        获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);
//        获取 我发布的及自己负责的项目下 的日报
        List<ProjectDailyDO> projectDailyDOList =
                projectDailyDAO.getMyProjectDaily(userId);
//        进行分页
        List<ProjectDailyDO> dailyPage =  Processing.getPage(projectDailyDOList, page, pageSize);
//        封装结果类
        List<ProjectDailyVO> projectDailyVOS = encapsulateArrayClass(dailyPage);
        ProjectDailyDataVO projectDailyDataVO =
                new ProjectDailyDataVO(projectDailyDOList.size(), page, pageSize, projectDailyVOS);

        return ResultUtil.success(projectDailyDataVO);
    }

    @Override
    public BaseResponse searchMyDaily(Integer page,
                                      Integer pageSize,
                                      Date beginTime,
                                      Date endTime,
                                      HttpServletRequest request) {
//        获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);
//        获取 我发布的及自己负责的项目下 的日报
        List<ProjectDailyDO> projectDailyDOList =
                projectDailyDAO.getMyProjectDaily(userId);


        return null;
    }


    public List<ProjectDailyVO> encapsulateArrayClass(List<ProjectDailyDO>
                                                              projectDailyDOList) {
//        定义封装结果数组
        List<ProjectDailyVO> projectDailyVOS = new ArrayList<>();
//        遍历原始数据，依次封装
        for (ProjectDailyDO projectDailyDO : projectDailyDOList) {
//            定义一个封装结果类
            ProjectDailyVO projectDailyVO = new ProjectDailyVO();
//            复制相同的属性值
            Processing.copyProperties(projectDailyDO, projectDailyVO);
//            赋值其他需查询的属性
            projectDailyVO.setProjectName(
                    projectDAO.getProjectById(projectDailyVO.getProjectId()).getName())
                    .setUserName(userDAO.getUserById(projectDailyDO.getUserId()).getNickname());
//            向 结果封装类数组 添加对应 日报封装类
            projectDailyVOS.add(projectDailyVO);
        }

        return projectDailyVOS;
    }

}


