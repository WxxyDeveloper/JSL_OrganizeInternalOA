package com.jsl.oa.controllers;

import com.jsl.oa.model.vodata.UserAddVO;
import com.jsl.oa.model.vodata.UserAllCurrentVO;
import com.jsl.oa.model.vodata.UserEditProfileVO;
import com.jsl.oa.model.vodata.UserEditVO;
import com.jsl.oa.services.UserService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@Slf4j
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
    @DeleteMapping("/user/delete")
    public BaseResponse userDelete(HttpServletRequest request, @RequestParam String id) {
        // 判断是否有参数错误
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        } else {
            return userService.userDelete(request, Long.valueOf(id));
        }
    }

    /**
     * 用户账号锁定
     *
     * @param id 用户id
     * @return {@link BaseResponse}
     */
    @PutMapping("/user/lock")
    public BaseResponse userLock(HttpServletRequest request, @RequestParam Long id, @RequestParam Long isLock) {
        // 判断是否有参数错误
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return userService.userLock(request, id, isLock);
    }

    /**
     * 用户编辑自己的信息
     *
     * @param userEditProfileVO 用户编辑自己的信息
     * @param bindingResult     参数校验结果
     * @return {@link BaseResponse}
     */
    @PutMapping("/user/profile/edit")
    public BaseResponse userEditProfile(
            @RequestBody @Validated UserEditProfileVO userEditProfileVO,
            BindingResult bindingResult
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userEditProfile(userEditProfileVO);
    }

    /**
     * <h2>获取当前用户信息</h2>
     * <hr/>
     * 获取当前用户信息接口<br/>
     * Admin接口
     *
     * @param request  请求
     * @param id       用户id
     * @param username 用户名
     * @param email    邮箱
     * @param phone    手机号
     * @return {@link BaseResponse}
     */
    @GetMapping("/user/current")
    public BaseResponse userCurrent(
            @RequestParam @Nullable String id,
            @RequestParam @Nullable String username,
            @RequestParam @Nullable String email,
            @RequestParam @Nullable String phone,
            @NotNull HttpServletRequest request
    ) {
        // 检查数据是否有问题
        if (id != null && !id.isEmpty()) {
            if (!Pattern.matches("^[0-9]+$", id)) {
                return ResultUtil.error(ErrorCode.PARAMETER_ERROR, "id 只能为数字");
            }
        }
        if (username != null && !username.isEmpty()) {
            if (!Pattern.matches("^[0-9A-Za-z_]+$", username)) {
                return ResultUtil.error(ErrorCode.PARAMETER_ERROR, "username 只允许 0-9、A-Z、a-z、_");
            }
        }
        if (email != null && !email.isEmpty()) {
            if (!Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email)) {
                return ResultUtil.error(ErrorCode.PARAMETER_ERROR, "email 格式不正确");
            }
        }
        if (phone != null && !phone.isEmpty()) {
            if (!Pattern.matches(
                    "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",
                    phone
            )) {
                return ResultUtil.error(ErrorCode.PARAMETER_ERROR, "手机格式不正确");
            }
        }
        // 检查是否出现错误
        return userService.userCurrent(request, id, username, email, phone);
    }

    /**
     * <h2>获取全部的用户信息</h2>
     * <hr/>
     * 获取全部的用户信息接口<br/>
     * Admin接口
     *
     * @return {@link BaseResponse}
     */
    @PostMapping("/user/current/all")
    public BaseResponse userCurrentAll(
            @RequestBody @Validated UserAllCurrentVO userAllCurrentVO,
            @NotNull BindingResult bindingResult,
            HttpServletRequest request
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userCurrentAll(request, userAllCurrentVO);
    }


    /**
     * @Description: 管理员添加用户
     * @Date: 2024/1/18
     * @Param userEditProfileVO:
     * @Param bindingResult:
     **/
    @PostMapping("/user/add")
    public BaseResponse userAdd(
            @RequestBody @Validated UserAddVO userAddVo,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userAdd(userAddVo, request);
    }

    /**
     * @Description: 管理员编辑用户
     * @Date: 2024/1/18
     * @Param userEditVO:
     * @Param bindingResult:
     **/
    @PutMapping("/user/edit")
    public BaseResponse userEdit(
            @RequestBody @Validated UserEditVO userEditVO,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userEdit(userEditVO, request);
    }


    @GetMapping("/user/profile/get")
    public BaseResponse userProfileGet(HttpServletRequest request) {
        return userService.userProfileGet(request);
    }


}
