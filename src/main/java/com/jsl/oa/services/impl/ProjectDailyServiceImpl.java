package com.jsl.oa.services.impl;
/*
        张睿相   Java
*/

import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.ProjectDailyDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.ProjectDailyMapper;
import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.ProjectDailyDO;
import com.jsl.oa.model.dodata.UserDO;
import com.jsl.oa.model.vodata.ProjectDailyAddVO;
import com.jsl.oa.model.vodata.ProjectDailyDataVO;
import com.jsl.oa.model.vodata.ProjectDailyUpdateVO;
import com.jsl.oa.model.vodata.ProjectDailyVO;
import com.jsl.oa.services.MessageService;
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
    private final ProjectDailyMapper projectDailyMapper;
    private final ProjectMapper projectMapper;
    private final MessageService messageService;


    @Override
    public BaseResponse addDaily(ProjectDailyAddVO projectDailyAddVO, HttpServletRequest request) {

//        获取用户id
            Long userId = Processing.getAuthHeaderToUserId(request);
//        从请求体中获取项目id
            Integer projectId = projectDailyAddVO.getProjectId();
//        检查项目是否存在
            if (projectMapper.getNotDeleteProjectById(Long.valueOf(projectId)) == null) {
                return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
            }

//        赋值数据库表实体类相关属性
        ProjectDailyDO projectDailyDO = new ProjectDailyDO();
        Processing.copyProperties(projectDailyAddVO, projectDailyDO);
        projectDailyDO.setUserId(userId);
        projectDailyDO.setProjectId(Long.valueOf(projectDailyAddVO.getProjectId()));
        projectDailyDO.setDailyTime(Processing.convertStringToDate(projectDailyAddVO.getDailyTime()));

//        向数据库添加数据
        projectDailyDAO.addProjectDaily(projectDailyDO);

//        发送消息
        List<Long> managerUserId = projectDAO.getAllManagerUserByProject(projectId, userId);
        for (Long uid: managerUserId) {
            messageService.messageAdd(projectId, userId, uid);
        }
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
    public BaseResponse searchMyDaily(Integer projectId,
                                      Integer page,
                                      Integer pageSize,
                                      Date beginTime,
                                      Date endTime,
                                      HttpServletRequest request) {
//        获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);

//        根据时间筛选---获取 我发布的及自己负责的项目下 的日报
        List<ProjectDailyDO> projectDailyDOList = new ArrayList<>();
        //如果时间不为空，则先根据时间筛选
        if (beginTime != null && endTime != null) {
           projectDailyDOList  = projectDailyDAO.
                   getMyProjectDailyByTime(userId, beginTime, endTime);
        } else {
        //否则获取全部数据
            projectDailyDOList =
                    projectDailyDAO.getMyProjectDaily(userId);
        }

//        再根据项目id进行筛选
        if (projectId != null) {
            projectDailyDOList.removeIf(projectDailyDO -> projectDailyDO.getProjectId() != Long.valueOf(projectId));
        }

//        进行分页
        List<ProjectDailyDO> dailyPage =  Processing.getPage(projectDailyDOList, page, pageSize);
//        封装结果类
        List<ProjectDailyVO> projectDailyVOS = encapsulateArrayClass(dailyPage);
        ProjectDailyDataVO projectDailyDataVO =
                new ProjectDailyDataVO(projectDailyDOList.size(), page, pageSize, projectDailyVOS);

        return ResultUtil.success(projectDailyDataVO);
    }


    @Override
    public BaseResponse deleteDaily(Integer dailyId, HttpServletRequest request) {

        Long userId = Processing.getAuthHeaderToUserId(request);

        ProjectDailyDO projectDailyDO = projectDailyMapper.getDailyById(dailyId);

        if (projectDailyDO == null) {
            return ResultUtil.error(ErrorCode.PROJECT_DAILY_NOT_EXIST);
        }

//      检查用户是否为项目负责人
        if (!projectDAO.isPrincipalUser(userId, projectDailyDO.getProjectId())) {
            return ResultUtil.error(ErrorCode.User_NOT_PROJECT_PRINCIPAL);
        }

        projectDailyDAO.deleteDailyById(dailyId);

        return ResultUtil.success();
    }

    @Override
    public BaseResponse updateDaily(ProjectDailyUpdateVO projectDailyUpdateVO, HttpServletRequest request) {
//      获取用户id
        Long userId = Processing.getAuthHeaderToUserId(request);
//      获取对应日报数据
        ProjectDailyDO projectDailyDO = projectDailyDAO.getPorjectDailyById(projectDailyUpdateVO.getId());
//      检测日报是否为空
        if (projectDailyDO == null) {
            return ResultUtil.error(ErrorCode.PROJECT_DAILY_NOT_EXIST);
        }
//       查询用户是否有修改权限（本人或项目负责人）
        if (userId.equals(projectDailyDO.getUserId())
                || projectDAO.getProjectById(
                        projectDailyDO.getProjectId()).
                        getPrincipalId().equals(userId)) {
            String content = projectDailyUpdateVO.getContent();
            Long projectId = Long.valueOf(projectDailyUpdateVO.getProjectId());
            String dailyTime = projectDailyUpdateVO.getDailyTime();

            if (content != null && !content.equals("")) {
                projectDailyDO.setContent(content);
            }

            if (projectDAO.isExistProject(projectId)) {
                projectDailyDO.setProjectId(projectId);
            }

            if (dailyTime != null && !dailyTime.equals("")) {
                projectDailyDO.setDailyTime(Processing.convertStringToDate(dailyTime));
            }

        } else {
            return ResultUtil.error(ErrorCode.NOT_PERMISSION_UPDATE_DAILY);
        }

        projectDailyDAO.updateDaily(projectDailyDO);

        return ResultUtil.success();
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
            ProjectDO projectDO =  projectDAO.getProjectById(projectDailyVO.getProjectId());
            if (projectDO == null) {
                projectDailyVO.setProjectName(projectDO.getName());
            } else {
                projectDailyVO.setPrincipalName("项目不存在");
            }
            //设置发送者名称，如果为昵称为空则赋值用户账号
            UserDO senderUser = userDAO.getUserById(projectDailyDO.getUserId());

            if (senderUser == null) {
                projectDailyVO.setUserName("");
                projectDailyVO.setNickName("");
            } else {
                projectDailyVO.setUserName(senderUser.getUsername());
                projectDailyVO.setNickName(senderUser.getNickname());
            }

            //设置项目负责人名称
            projectDailyVO.setPrincipalName(projectDAO.getPrincipalUserFromProject(projectDailyDO.getProjectId()));
                //用户是否有权限删除
            if (projectDailyDO.getUserId().equals(projectDAO.
                    getProjectById(projectDailyVO.getProjectId()).getPrincipalId())) {
                projectDailyVO.setIsAllowDelete(true);
            } else {
                projectDailyVO.setIsAllowDelete(false);
            }

//            向 结果封装类数组 添加对应 日报封装类
            projectDailyVOS.add(projectDailyVO);
        }

        return projectDailyVOS;
    }

}


