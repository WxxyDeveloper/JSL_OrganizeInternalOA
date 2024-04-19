package com.jsl.oa.dao;
/*
        张睿相   Java
*/

import com.jsl.oa.mapper.ProjectDailyMapper;
import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.ProjectDailyDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * ProjectDailyDAO
 * <hr/>
 * 用于处理日报相关的请求, 包括获取日报、添加日报信息、编辑日报信息等
 *
 * @author zrx_hhh
 * @version v1.0.0
 * @since v1.0.0-SNAPSHOT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectDailyDAO {

    private final ProjectDailyMapper projectDailyMapper;
    private final ProjectMapper projectMapper;


    public void addProjectDaily(ProjectDailyDO projectDailyDO) {
        projectDailyMapper.insert(projectDailyDO);
    }


    public List<ProjectDailyDO> getMyProjectDaily(Long userId) {

//        日报数据数组
        List<ProjectDailyDO> projectDailyDOList = new ArrayList<>();

//        先获取我负责的项目下的日报数据
        //获取我负责的项目
        List<ProjectDO> projectDOS = projectMapper.getAllProjectByUserId(userId);
        //获取项目下对应日报
        for (ProjectDO projectDO:projectDOS) {
            List dailyFromProject = projectDailyMapper.getProjectDailyByProject(projectDO.getId());
            projectDailyDOList.addAll(dailyFromProject);
        }

//        在获取本人的发布日报
        List<ProjectDailyDO> myProjectDaily = projectDailyMapper.getProjectDailyByUser(userId);
        projectDailyDOList.addAll(myProjectDaily);

//        排序并去重
        projectDailyDOList = sortaAndNotRepeatDailyDO(projectDailyDOList);

        return projectDailyDOList;
    }


    public List<ProjectDailyDO> getMyProjectDailyByTime(Long userId, Date beginTime, Date endTime) {

//        日报数据数组
        List<ProjectDailyDO> projectDailyDOList = new ArrayList<>();

//        先获取我负责的项目下的日报数据
        //获取我负责的项目
        List<ProjectDO> projectDOS = projectMapper.getAllProjectByUserId(userId);
        //获取项目下对应日报,并根据时间筛选
        for (ProjectDO projectDO:projectDOS) {
            List dailyFromProject = projectDailyMapper.getProjectDailyByProjectAndTime(projectDO.getId(),
                    beginTime, endTime);
            projectDailyDOList.addAll(dailyFromProject);
        }

//        在获取本人的发布日报,并根据时间筛选
        List<ProjectDailyDO> myProjectDaily = projectDailyMapper.
                getProjectDailyByUserAndTime(userId, beginTime, endTime);
        projectDailyDOList.addAll(myProjectDaily);

//        排序并去重
        projectDailyDOList = sortaAndNotRepeatDailyDO(projectDailyDOList);

        return projectDailyDOList;
    }


    public List<ProjectDailyDO> sortaAndNotRepeatDailyDO(List<ProjectDailyDO> projectDailyDOList) {

//        去除重复的日报信息
        projectDailyDOList = projectDailyDOList.stream()
                // 根据 id 属性进行去重
                .collect(Collectors.toMap(ProjectDailyDO::getId,
                        Function.identity(), (existing, replacement) -> existing))
                .values().stream()
                .collect(Collectors.toList());

//        根据时间进行排序
        projectDailyDOList = projectDailyDOList.stream()
                .sorted(Comparator.comparing(ProjectDailyDO::getCreatedAt).reversed())
                .collect(Collectors.toList());

        return projectDailyDOList;
    }

    public void deleteDailyById(Integer dailyId) {
        projectDailyMapper.deleteDailyById(dailyId);
    }
}


