package com.jsl.oa.controllers;

import com.jsl.oa.exception.ClassCopyException;
import com.jsl.oa.model.voData.RoleAddVo;
import com.jsl.oa.model.voData.RoleEditVO;
import com.jsl.oa.services.RoleService;
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
 * <h1>角色控制器</h1>
 * <hr/>
 * 角色控制器，包含角色获取接口
 *
 * @version v1.1.0
 * @see RoleService
 * @since v1.1.0
 * @author xiao_lfeng | 176yunxuan | xiangZr-hhh
 */
@Slf4j
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
    public BaseResponse roleGet(HttpServletRequest request, @RequestParam(required = false) String id) {
        log.info("请求接口[GET]: /role/get");
        return roleService.roleGet(request, id);
    }

    /**
     * <h2>角色编辑</h2>
     * <hr/>
     * 角色编辑接口
     *
     * @param request 请求
     * @param roleEditVO 角色编辑VO
     * @param bindingResult 参数校验结果
     * @return {@link BaseResponse}
     */
    @PutMapping("/role/edit")
    public BaseResponse roleEdit(HttpServletRequest request, @RequestBody @Validated RoleEditVO roleEditVO, @NotNull BindingResult bindingResult) {
        log.info("请求接口[PUT]: /role/edit");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return roleService.roleEdit(request, roleEditVO);
    }

    /**
     * <h2>角色删除</h2>
     * <hr/>
     * 角色删除接口
     *
     * @param request 请求
     * @param id 角色id
     * @return {@link BaseResponse}
     */
    @DeleteMapping("/role/delete")
    public BaseResponse roleDelete(HttpServletRequest request, @RequestParam Long id) {
        log.info("请求接口[DELETE]: /role/delete");
        // 判断是否有参数错误
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return roleService.roleDelete(request, id);
    }

    /**
     * 用户权限授予
     *
     * @return
     */
    @PostMapping("role/user/add")
    public BaseResponse roleAddUser(HttpServletRequest request, @RequestParam Long uid, @RequestParam Long rid) {
        log.info("请求接口[POST]: /role/user/add");
        // 判断是否有参数错误
        if (uid == null || rid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return roleService.roleAddUser(request, uid, rid);
    }

    /**
     * 用户权限删除
     *
     * @return
     */
    @DeleteMapping("role/user/remove")
    public BaseResponse roleRemoveUser(HttpServletRequest request, @RequestParam Long uid) {
        log.info("请求接口[POST]: /role/user/remove");
        // 判断是否有参数错误
        if (uid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return roleService.roleRemoveUser(request, uid);
    }


    /**
     * @Description: 添加用户权限
     * @Date: 2024/1/19
     * @Param request:
     * @Param uid:
     **/
    @PostMapping("role/add")
    public BaseResponse addRole(HttpServletRequest request, @RequestBody @Validated RoleAddVo roleAddVO, @NotNull BindingResult bindingResult ) throws ClassCopyException {
        log.info("请求接口[POST]: /role/add");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return roleService.addRole(request, roleAddVO);
    }


    /**
     * @Description:  改变用户角色权限信息
     * @Date: 2024/1/20
     * @Param request:
     * @Param uid: 用户id
     * @Param rid: 角色id
     **/
    @PutMapping("role/user/change")
    public BaseResponse roleChangeUser(HttpServletRequest request, @RequestParam Long uid, @RequestParam Long rid) {
        log.info("请求接口[POST]: /role/user/change");
        // 判断是否有参数错误
        if (uid == null || rid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return roleService.roleChangeUser(request, uid, rid);
    }






}
