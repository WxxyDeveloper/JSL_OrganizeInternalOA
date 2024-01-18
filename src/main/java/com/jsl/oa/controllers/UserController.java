package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.UserAllCurrentVO;
import com.jsl.oa.model.voData.UserEditProfileVO;
import com.jsl.oa.services.UserService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>用户控制器</h1>
 * <hr/>
 * 用户控制器，包含用户账号删除、用户账号锁定、用户编辑自己的信息接口
 *
 * @version v1.1.0
 * @see UserService
 * @see UserEditProfileVO
 * @since v1.0.0
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 用户账号删除
     *
     * @param id
     * @return
     */
    @PutMapping("/user/delete")
    public BaseResponse userDelete(@RequestParam Long id) {
        // 判断是否有参数错误
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        } else return userService.userDelete(id);
    }

    /**
     * 用户账号锁定
     *
     * @param id
     * @return
     */
    @PutMapping("/user/lock")
    public BaseResponse userLock(@RequestParam Long id) {
        // 判断是否有参数错误
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return userService.userLock(id);
    }

    /**
     * 用户编辑自己的信息
     *
     * @param userEditProfileVO
     * @param bindingResult
     * @return
     */
    @PutMapping("/user/profile/edit")
    public BaseResponse userEditProfile(@RequestBody @Validated UserEditProfileVO userEditProfileVO, BindingResult bindingResult) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userEditProfile(userEditProfileVO);
    }

    /**
     * <h2>获取全部的用户信息</h2>
     * <hr/>
     * 获取全部的用户信息接口<br/>
     * Admin接口
     *
     * @return {@link BaseResponse}
     */
    @GetMapping("/user/current/all")
    public BaseResponse userCurrentAll(@RequestBody @Validated UserAllCurrentVO userAllCurrentVO,
                                       HttpServletRequest request, @NotNull BindingResult bindingResult) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userCurrentAll(request, userAllCurrentVO);
    }

}
