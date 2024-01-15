package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.*;
import com.jsl.oa.services.AuthService;
import com.jsl.oa.services.UserService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 用户账号删除
     * @param userDeleteVO
     * @param bindingResult
     * @return
     */
    @PutMapping("/user/delete")
    public BaseResponse userDelete(@RequestBody @Validated UserDeleteVO userDeleteVO, BindingResult bindingResult){
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userDelete(userDeleteVO);
    }

    /**
     * 用户账号锁定
     * @param userLockVO
     * @param bindingResult
     * @return
     */
    @PutMapping("/user/lock")
    public BaseResponse userLock(@RequestBody @Validated UserLockVO userLockVO, BindingResult bindingResult){
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userLock(userLockVO);
    }

    @PutMapping("/user/profile/edit")
    public BaseResponse userEditProfile(@RequestBody @Validated UserEditProfile userEditProfile, BindingResult bindingResult){
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userEditProfile(userEditProfile);
    }


}
