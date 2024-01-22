package com.jsl.oa.services.impl;


import com.jsl.oa.dao.NewsDAO;
import com.jsl.oa.model.doData.NewsDO;
import com.jsl.oa.model.voData.NewsAddVO;
import com.jsl.oa.services.NewsService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.JwtUtil;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsDAO newsDAO;

    @Override
    public BaseResponse newsAdd(NewsAddVO newsAddVO, HttpServletRequest request) {
        log.info("\t> 执行 Service 层 NewsService.newsAdd 方法");
//        拷贝新闻数据到实体类
        NewsDO newsDO = new NewsDO();
        Processing.copyProperties(newsAddVO,newsDO);
//        获取现在的用户id
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long uid = JwtUtil.getUserId(token);
//        添加新闻数据
        newsDAO.addNews(newsDO,uid);

        return ResultUtil.success();
    }

}


