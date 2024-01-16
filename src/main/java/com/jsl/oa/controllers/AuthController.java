package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.UserLoginVO;
import com.jsl.oa.model.voData.UserRegisterVO;
import com.jsl.oa.services.AuthService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * <h1>用户认证控制器</h1>
 * <hr/>
 * 用户认证控制器，包含用户注册、用户登录、用户登出接口
 *
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
    public BaseResponse authRegister(@RequestBody @Validated UserRegisterVO userRegisterVO, @NotNull BindingResult bindingResult) throws ParseException {
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
    @GetMapping("/auth/login")
    public BaseResponse authLogin(@RequestBody @Validated UserLoginVO userLoginVO, @NotNull BindingResult bindingResult) {
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
    @GetMapping("/auth/login/email/code")
    public BaseResponse authLoginSendEmailCode(@RequestParam String email) {
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

    @GetMapping("/auth/login/email")
    public BaseResponse authLoginByEmail(@RequestParam String email, @RequestParam Integer code) {
        if (email != null && code != null) {
            if (Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email)) {
                return authService.authLoginByEmail(email, code);
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
    public BaseResponse authLogout() {
        return null;
    }
}