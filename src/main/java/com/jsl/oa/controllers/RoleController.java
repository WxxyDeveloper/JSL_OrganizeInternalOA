package com.jsl.oa.controllers;

import com.jsl.oa.services.RoleService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * <h1>角色控制器</h1>
 * <hr/>
 * 角色控制器，包含角色获取接口
 *
 * @version v1.1.0
 * @see RoleService
 * @since v1.1.0
 */
@RestController
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    /**
     * <h2>角色获取</h2>
     * <hr/>
     * 角色获取接口
     *
     * @param id 角色id
     * @return {@link BaseResponse}
     */
    @GetMapping("/role/get")
    public BaseResponse roleGet(HttpServletRequest request, @RequestParam @Nullable String id) {
        return roleService.roleGet(request, id);
    }

    /**
     * 用户权限授予
     *
     * @return
     */
    @PostMapping("role/user/add")
    public BaseResponse roleAddUser(HttpServletRequest request, @RequestParam Long uid, @RequestParam Long rid) {
        // 判断是否有参数错误
        if (uid == null || rid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return roleService.roleAddUser(request,uid, rid);
    }

    /**
     * 用户权限删除
     *
     * @return
     */
    @DeleteMapping("role/user/remove")
    public BaseResponse roleRemoveUser(HttpServletRequest request,@RequestParam Long uid) {
        // 判断是否有参数错误
        if (uid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return roleService.roleRemoveUser(request,uid);
    }
}
