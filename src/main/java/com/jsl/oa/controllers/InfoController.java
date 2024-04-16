package com.jsl.oa.controllers;

import com.jsl.oa.model.vodata.business.info.CarouselVO;
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
 * @author xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class InfoController {

    /**
     * 信息服务
     */
    private final InfoService infoService;

    /**
     * 获取头部图片信息
     *
     * @param id 图片id
     * @return 图片信息
     */
    @GetMapping("/info/header-image/get")
    public BaseResponse infoGetHeaderImage(@RequestParam(required = false) Integer id) {
        return infoService.getHeaderImage(id);
    }

    /**
     * 编辑头部图片信息
     *
     * @param carouselVO    图片信息
     * @param request       请求对象
     * @param bindingResult 数据校验结果
     * @return 编辑结果
     */
    @PutMapping("/info/header-image/edit")
    public BaseResponse infoEditHeaderImage(
            @RequestBody @Validated CarouselVO carouselVO,
            HttpServletRequest request,
            @NotNull BindingResult bindingResult
    ) {
        // 参数校验
        if (bindingResult.hasErrors()) {
            log.warn("参数校验失败: {}", Processing.getValidatedErrorList(bindingResult));
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        if (carouselVO.getId() == null) {
            log.warn("参数校验失败: {}", "id不能为空");
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, "id不能为空");
        }
        return infoService.editHeaderImage(request, carouselVO);
    }

    /**
     * 删除头部图片信息
     *
     * @param id      图片id
     * @param request 请求对象
     * @return 删除结果
     */
    @DeleteMapping("/info/header-image/del")
    public BaseResponse infoDelHeaderImage(@RequestParam Integer id, HttpServletRequest request) {
        return infoService.delHeaderImage(request, id);
    }

    /**
     * 添加头部图片信息
     *
     * @param carouselVO    图片信息
     * @param request       请求对象
     * @param bindingResult 数据校验结果
     * @return 添加结果
     */
    @PostMapping("/info/header-image/add")
    public BaseResponse infoAddHeaderImage(
            @RequestBody @Validated CarouselVO carouselVO,
            HttpServletRequest request,
            @NotNull BindingResult bindingResult
    ) {
        // 参数校验
        if (bindingResult.hasErrors()) {
            log.warn("参数校验失败: {}", Processing.getValidatedErrorList(bindingResult));
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return infoService.addHeaderImage(request, carouselVO);
    }

    /**
     * 编辑头部图片设置
     *
     * @param showType 是否显示
     * @param request  请求对象
     * @return 编辑结果
     */
    @PutMapping("/info/header-image/edit-setting")
    public BaseResponse infoEditSettingHeaderImage(@RequestParam Boolean showType, HttpServletRequest request) {
        return infoService.editSettingHeaderImage(request, showType);
    }

    /**
     * 获取头部用户信息
     *
     * @param order   排序方式
     * @param orderBy 排序字段
     * @param request 请求对象
     * @return 用户信息
     */
    @GetMapping("info/header-user/get")
    public BaseResponse infoGetHeaderUser(
            @RequestParam String order,
            @RequestParam String orderBy,
            HttpServletRequest request
    ) {
        return infoService.getHeaderUser(request, order, orderBy);
    }
}
