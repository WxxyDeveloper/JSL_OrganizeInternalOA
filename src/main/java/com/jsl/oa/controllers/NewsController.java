package com.jsl.oa.controllers;


import com.jsl.oa.model.voData.NewsAddVO;
import com.jsl.oa.services.NewsService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>新闻控制器</h1>
 * <hr/>
 * 角色控制器，包含角色获取接口
 *
 * @version v1.1.0
 * @see NewsService
 * @since v1.1.0
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping("/news/add")
    public BaseResponse newsAdd(@RequestBody @Validated NewsAddVO newsAddVO, BindingResult bindingResult, HttpServletRequest request) {
        log.info("请求接口[POST]: /news/add");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }

        return newsService.newsAdd(newsAddVO, request);
    }

}


