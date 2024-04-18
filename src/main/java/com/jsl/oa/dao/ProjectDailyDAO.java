package com.jsl.oa.dao;
/*
        张睿相   Java
*/

import com.jsl.oa.mapper.ProjectDailyMapper;
import com.jsl.oa.model.dodata.ProjectDailyDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



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


    public void addProjectDaily(ProjectDailyDO projectDailyDO) {
        projectDailyMapper.insert(projectDailyDO);
    }


}


