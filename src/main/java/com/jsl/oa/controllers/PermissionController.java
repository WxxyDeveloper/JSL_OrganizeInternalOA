package com.jsl.oa.controllers;

import com.jsl.oa.model.vodata.PermissionEditVO;
import com.jsl.oa.services.PermissionService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限控制器类，处理权限相关的API请求。
 * 它使用 {@link PermissionService} 来执行权限相关的操作。
 *
 * @author xiao_lfeng | xiangZr-hhh | 176yunxuan
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PermissionController {

    /**
     * 权限服务实例，用于执行权限相关的操作。
     */
    private final PermissionService permissionService;

    /**
     * 添加新的权限。
     *
     * @param request HTTP请求对象。
     * @param rid     角色ID。
     * @param pid     权限ID。
     * @return {@link BaseResponse} 对象，包含操作结果。
     */
    @PostMapping("/permission/add")
    public BaseResponse permissionAdd(HttpServletRequest request, @RequestParam Long rid, @RequestParam Long pid) {
        // 判断是否有参数错误
        if (rid == null || pid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        } else {
            return permissionService.permissionAdd(request, rid, pid);
        }
    }

    /**
     * 获取当前用户的权限信息。
     *
     * @param request HTTP请求对象。
     * @param uid     用户ID。
     * @return {@link BaseResponse} 对象，包含操作结果。
     */
    @GetMapping("/permission/current")
    public BaseResponse permissionUser(HttpServletRequest request, @RequestParam Long uid) {
        // 判断是否有参数错误
        if (uid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        } else {
            return permissionService.permissionUser(request, uid);
        }
    }

    /**
     * 获取所有权限信息。
     *
     * @param request HTTP请求对象。
     * @return {@link BaseResponse} 对象，包含操作结果。
     */
    @GetMapping("/permission/get")
    public BaseResponse permissionGet(HttpServletRequest request) {
        return permissionService.permissionGet(request);
    }

    /**
     * 编辑权限信息。
     *
     * @param permissionEditVo {@link PermissionEditVO} 对象，包含更新后的权限信息。
     * @param bindingResult    Binding结果对象，包含任何验证错误。
     * @param request          HTTP请求对象。
     * @return {@link BaseResponse} 对象，包含操作结果。
     */
    @PutMapping("/permission/edit")
    public BaseResponse permissionEdit(
            @RequestBody @Validated PermissionEditVO permissionEditVo,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return permissionService.permissionEdit(permissionEditVo, request);
    }

    /**
     * 删除权限。
     *
     * @param pid     权限ID。
     * @param request HTTP请求对象。
     * @return {@link BaseResponse} 对象，包含操作结果。
     */
    @DeleteMapping("/permission/delete")
    public BaseResponse permissionDelete(@RequestParam Long pid, HttpServletRequest request) {
        // 判断是否有参数错误
        if (pid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        } else {
            return permissionService.permissionDelete(request, pid);
        }
    }
}
