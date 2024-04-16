package com.jsl.oa.dao;

import com.jsl.oa.mapper.NewsMapper;
import com.jsl.oa.model.dodata.NewsDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class NewsDAO {

    private final NewsMapper newsMapper;

    public void addNews(NewsDO newsVO, Long uid) {
//        添加新闻数据
        newsMapper.addNews(newsVO);
//        添加作者
        newsMapper.addNewsUser(uid, newsVO.getId());
    }


}


