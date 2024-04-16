package com.jsl.oa.controllers;

import com.jsl.oa.annotations.NeedPermission;
import com.jsl.oa.model.vodata.UserChangePasswordVO;
import com.jsl.oa.model.vodata.UserForgetPasswordVO;
import com.jsl.oa.model.vodata.UserLoginVO;
import com.jsl.oa.model.vodata.UserRegisterVO;
import com.jsl.oa.services.AuthService;
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
import java.util.regex.Pattern;

/**
 * <h1>用户认证控制器</h1>
 * <hr/>
 * 用户认证控制器，包含用户注册、用户登录、用户登出接口
 *
 * @author 筱锋xiao_lfeng|176yunxuan
 * @version v1.1.0
 * @see AuthService
 * @see UserRegisterVO
 * @see UserLoginVO
 * @see BaseResponse
 * @see ErrorCode
 * @see Processing
 * @see ResultUtil
 * @since v1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * <h2>用户注册</h2>
     * <hr/>
     * 用户注册接口
     *
     * @return {@link BaseResponse}
     * @author 筱锋xiao_lfeng
     * @since v1.0.0
     */
    @PostMapping("/auth/register")
    public BaseResponse authRegister(
            @RequestBody @Validated UserRegisterVO userRegisterVO,
            @NotNull BindingResult bindingResult
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return authService.authRegister(userRegisterVO);
    }

    /**
     * <h2>用户登录</h2>
     * <hr/>
     * 用户登录接口
     *
     * @param userLoginVO   用户登录信息
     * @param bindingResult 参数校验结果
     * @return {@link BaseResponse}
     * @author 176yunxuan
     * @since v1.0.0
     */
    @PostMapping("/auth/login")
    public BaseResponse authLogin(
            @RequestBody @Validated UserLoginVO userLoginVO,
            @NotNull BindingResult bindingResult
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return authService.authLogin(userLoginVO);
    }

    /**
     * <h2>用户邮箱登录</h2>
     * <hr/>
     * 用户邮箱登录接口
     *
     * @param email 用户登陆邮箱
     * @return {@link BaseResponse}
     * @author 筱锋xiao_lfeng
     * @since v1.1.0
     */
    @GetMapping("/auth/email/code")
    public BaseResponse authSendEmailCode(@RequestParam String email) {
        if (email != null) {
            if (Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email)) {
                return authService.authLoginSendEmailCode(email);
            } else {
                return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
            }
        } else {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
    }

    /**
     * <h1>通过电子邮件和验证码处理用户登录。</h1>
     * <hr/>
     * 此方法首先验证电子邮件格式，并检查提供的验证码是否为有效整数。
     * 如果验证通过，它尝试使用提供的电子邮件和验证码登录用户。
     *
     * @param email 用户尝试登录的电子邮件地址。
     * @param code  发送到用户电子邮件的验证码，用于身份验证。
     * @return {@link BaseResponse} 包含登录尝试的结果。这可以是带有登录详细信息的成功消息，
     * 或者指示出了什么问题（例如，参数无效、验证码错误）的错误消息。
     */
    @GetMapping("/auth/login/email")
    public BaseResponse authLoginByEmail(@RequestParam String email, @RequestParam String code) {
        if (email != null && code != null && !email.isEmpty() && !code.isEmpty()) {
            if (Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email)) {
                try {
                    Integer integer = Integer.valueOf(code);
                    return authService.authLoginByEmail(email, integer);
                } catch (NumberFormatException e) {
                    return ResultUtil.error(ErrorCode.VERIFICATION_INVALID);
                }
            } else {
                return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
            }
        } else {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
    }

    /**
     * <h2>用户登出</h2>
     * <hr/>
     * 用户登出接口
     *
     * @return {@link BaseResponse}
     * @since v1.1.0
     */
    @GetMapping("/auth/logout")
    @NeedPermission("auth:logout")
    public BaseResponse authLogout(HttpServletRequest request) {
        return authService.authLogout(request);
    }

    /**
     * <h2>修改密码</h2>
     * <hr/>
     * 修改密码
     *
     * @param userChangePasswordVO 用户修改密码信息
     * @param request              HTTP请求
     * @param bindingResult        参数校验结果
     * @return {@link BaseResponse}
     * @since v1.1.0
     */
    @PutMapping("/auth/password")
    @NeedPermission("auth:change_password")
    public BaseResponse authChangePassword(
            @RequestBody @Validated UserChangePasswordVO userChangePasswordVO,
            @NotNull BindingResult bindingResult,
            HttpServletRequest request
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return authService.authChangePassword(userChangePasswordVO, request);
    }

    /**
     * <h2>忘记密码</h2>
     * <hr/>
     * 忘记密码
     */
    @PutMapping("auth/password/forget")
    public BaseResponse authForgetPassword(
            @RequestBody @Validated UserForgetPasswordVO userForgetPasswordVO,
            @NotNull BindingResult bindingResult
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return authService.authForgetPassword(userForgetPasswordVO);
    }
}
