package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.UserLoginVO;
import com.jsl.oa.model.voData.UserRegisterVO;
import com.jsl.oa.services.AuthService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * <h1>用户认证控制器</h1>
 * <hr/>
 * 用户认证控制器，包含用户注册、用户登录、用户登出接口
 *
 * @since v1.0.0
 * @version v1.1.0
 * @see AuthService
 * @see UserRegisterVO
 * @see UserLoginVO
 * @see BaseResponse
 * @see ErrorCode
 * @see Processing
 * @see ResultUtil
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
     * @since v1.0.0
     * @return {@link BaseResponse}
     * @author 筱锋xiao_lfeng
     */
    @PostMapping("/auth/register")
    public BaseResponse authRegister(@RequestBody @Validated UserRegisterVO userRegisterVO, BindingResult bindingResult) throws ParseException {
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
     * @since v1.0.0
     * @param userLoginVO 用户登录信息
     * @return {@link BaseResponse}
     * @author 176yunxuan
     */
    @GetMapping("/auth/login")
    public BaseResponse authLogin(@RequestBody @Validated UserLoginVO userLoginVO, BindingResult bindingResult){
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return authService.authLogin(userLoginVO);
    }

    public BaseResponse authLogout() {
        return null;
    }
}
