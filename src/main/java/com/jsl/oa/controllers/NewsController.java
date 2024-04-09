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
 * 新闻控制器，包含新闻添加接口
 *
 * @version v1.1.0
 * @see NewsService
 * @since v1.1.0
 * @author xiangZr-hhh | xiao_lfeng | 176yunxuan
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /**
     * <h2>新闻添加接口</h2>
     * <hr/>
     * 新闻添加接口，接收新闻添加VO对象，并调用NewsService的newsAdd方法进行新闻添加操作。
     *
     * @param newsAddVO 新闻添加VO对象，包含新闻信息
     * @param bindingResult 数据校验结果，用于检查请求参数是否有错误
     * @param request 请求对象，包含请求信息
     * @return BaseResponse 返回结果，包含操作结果和错误信息
     * @see NewsService#newsAdd(NewsAddVO, HttpServletRequest)
     * @since v1.1.0
     */
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


