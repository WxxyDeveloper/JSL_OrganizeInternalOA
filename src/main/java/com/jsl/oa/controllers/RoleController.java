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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

public class RoleController {
    private final RoleService roleService;

    /**
     * 用户权限授予
     *
     * @return
     */
    @PostMapping("role/user/add")
    public BaseResponse roleAddUser(@RequestParam Long uid,@RequestParam Long rid){
        // 判断是否有参数错误
        if (uid == null || rid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return roleService.roleAddUser(uid,rid);
    }

    /**
     * 用户权限删除
     *
     * @return
     */
    @DeleteMapping("role/user/remove")
    public BaseResponse roleRemoveUser(@RequestParam Long uid){
        // 判断是否有参数错误
        if (uid==null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return roleService.roleRemoveUser(uid);
    }
}
