package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.RoleAddUserVO;
import com.jsl.oa.model.voData.RoleRemoveUserVO;
import com.jsl.oa.services.RoleService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class RoleController {
    private final RoleService roleService;

    /**
     * 用户权限授予
     * @param roleAddUserVO
     * @param bindingResult
     * @return
     */
    @PostMapping("role/user/add")
    public BaseResponse roleAddUser(@RequestBody @Validated RoleAddUserVO roleAddUserVO, BindingResult bindingResult){
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return roleService.roleAddUser(roleAddUserVO);
    }

    /**
     * 用户权限删除
     * @param roleRemoveUserVO
     * @param bindingResult
     * @return
     */
    @DeleteMapping("role/user/remove")
    public BaseResponse roleRemoveUser(@RequestBody @Validated RoleRemoveUserVO roleRemoveUserVO, BindingResult bindingResult){
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return roleService.roleRemoveUser(roleRemoveUserVO);
    }
}
