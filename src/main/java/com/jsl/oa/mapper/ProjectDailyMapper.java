package com.jsl.oa.mapper;


import com.jsl.oa.model.dodata.ProjectDailyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


/**
 * 项目日报(ProjectDaily)表数据库访问层
 *
 * @author zrx
 * @since 2024-04-18 11:40:56
 */
@Mapper
public interface ProjectDailyMapper {


    void insert(ProjectDailyDO projectDailyDO);

    List<ProjectDailyDO> getProjectDailyByUser(Long uid);

    List<ProjectDailyDO> getProjectDailyByProject(Long pid);

    List getProjectDailyByProjectAndTime(Long id, Date beginTime, Date endTime);

    List<ProjectDailyDO> getProjectDailyByUserAndTime(Long userId, Date beginTime, Date endTime);

    ProjectDailyDO getDailyById(Integer id);

    void deleteDailyById(Integer id);

    void updateDaily(ProjectDailyDO projectDailyDO);
}



