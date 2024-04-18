package com.jsl.oa.mapper;


import com.jsl.oa.model.dodata.ProjectDailyDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * 项目日报(ProjectDaily)表数据库访问层
 *
 * @author zrx
 * @since 2024-04-18 11:40:56
 */
@Mapper
public interface ProjectDailyMapper {


    void insert(ProjectDailyDO projectDailyDO);
}



