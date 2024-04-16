package com.jsl.oa.controllers;

import com.jsl.oa.services.PermissionService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
