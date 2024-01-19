package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.business.info.CarouselVO;
import com.jsl.oa.services.InfoService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>信息控制器</h1>
 * <hr/>
 * 信息控制器，包含信息获取接口
 *
 * @author 筱锋xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class InfoController {
    private final InfoService infoService;

    @GetMapping("/info/header-image/get")
    public BaseResponse infoGetHeaderImage(@RequestParam(required = false) Integer id) {
        log.info("请求接口[GET]: /info/header-image/get");
        return infoService.getHeaderImage(id);
    }

    @PutMapping("/info/header-image/edit")
    public BaseResponse infoEditHeaderImage(@RequestBody @Validated CarouselVO carouselVO, @RequestParam Integer id, HttpServletRequest request, @NotNull BindingResult bindingResult) {
        log.info("请求接口[PUT]: /info/header-image/edit");
        // 参数校验
        if (bindingResult.hasErrors()) {
            log.warn("参数校验失败: {}", Processing.getValidatedErrorList(bindingResult));
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        if (id == null) {
            log.warn("参数校验失败: {}", "id不能为空");
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, "id不能为空");
        }
        return infoService.editHeaderImage(request, carouselVO, id);
    }

    @DeleteMapping("/info/header-image/del")
    public BaseResponse infoDelHeaderImage(@RequestParam Integer id, HttpServletRequest request) {
        log.info("请求接口[DELETE]: /info/header-image/del");
        return infoService.delHeaderImage(request, id);
    }

    @PostMapping("/info/header-image/add")
    public BaseResponse infoAddHeaderImage(@RequestBody @Validated CarouselVO carouselVO, HttpServletRequest request, @NotNull BindingResult bindingResult) {
        log.info("请求接口[POST]: /info/header-image/add");
        // 参数校验
        if (bindingResult.hasErrors()) {
            log.warn("参数校验失败: {}", Processing.getValidatedErrorList(bindingResult));
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return infoService.addHeaderImage(request, carouselVO);
    }

    @PutMapping("/info/header-image/edit-setting")
    public BaseResponse infoEditSettingHeaderImage(@RequestParam Boolean showType, HttpServletRequest request) {
        log.info("请求接口[PUT]: /info/header-image/edit-setting");
        return infoService.editSettingHeaderImage(request, showType);
    }
}
