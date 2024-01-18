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
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
     * @param id 用户id
     * @return {@link BaseResponse}
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
     * @param id 用户id
     * @return {@link BaseResponse}
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
     * @param userEditProfileVO 用户编辑自己的信息
     * @param bindingResult    参数校验结果
     * @return {@link BaseResponse}
     */
    @PutMapping("/user/profile/edit")
    public BaseResponse userEditProfile(@RequestBody @Validated UserEditProfileVO userEditProfileVO, BindingResult bindingResult) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userEditProfile(userEditProfileVO);
    }

    @GetMapping("/user/current")
    public BaseResponse userCurrent(HttpServletRequest request, @RequestParam @Nullable String id, @RequestParam @Nullable String username, @RequestParam @Nullable String email, @RequestParam @Nullable String phone) {
        // 判断是否有参数错误
        if (id == null && username == null && email == null && phone == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        // 检查数据是否有问题
        ArrayList<String> arrayForError = new ArrayList<>();
        if (id != null) {
            if (!Pattern.matches("^[0-9]+$", id)) {
                arrayForError.add("id 只能为数字");
            }
        }
        if (username != null) {
            if (!Pattern.matches("^[0-9A-Za-z_]+$", username)) {
                arrayForError.add("username 只允许 0-9、A-Z、a-z、_");
            }
        }
        if (email != null) {
            if (!Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email)) {
                arrayForError.add("email 格式不正确");
            }
        }
        if (phone != null) {
            if (!Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", phone)) {
                arrayForError.add("手机格式不正确");
            }
        }
        // 检查是否出现错误
        if (arrayForError.isEmpty()) {
            return userService.userCurrent(request, id, username, email, phone);
        } else {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, arrayForError);
        }
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
