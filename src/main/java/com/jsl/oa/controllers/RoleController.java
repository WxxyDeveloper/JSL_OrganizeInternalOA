package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.RoleAddUser;
import com.jsl.oa.model.voData.RoleRemoveUser;
import com.jsl.oa.services.RoleService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
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
     * @param roleAddUser
     * @param bindingResult
     * @return
     */
    @PostMapping("role/user/add")
    public BaseResponse roleAddUser(@RequestBody @Validated RoleAddUser roleAddUser, BindingResult bindingResult){
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return roleService.roleAddUser(roleAddUser);
    }

    /**
     * 用户权限删除
     * @param roleRemoveUser
     * @param bindingResult
     * @return
     */
    @DeleteMapping("role/user/remove")
    public BaseResponse roleRemoveUser(@RequestBody @Validated RoleRemoveUser roleRemoveUser, BindingResult bindingResult){
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return roleService.roleRemoveUser(roleRemoveUser);
    }
}
