package com.jsl.oa.controllers;

import com.jsl.oa.common.voData.UserLoginVO;
import com.jsl.oa.common.voData.UserRegisterVO;
import com.jsl.oa.services.UserService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * <h1>用户注册</h1>
     * <hr/>
     * 用户注册接口
     *
     * @return {@link BaseResponse}
     * @author 筱锋xiao_lfeng
     */
    @PostMapping("/user/register")
    public BaseResponse userRegister(@RequestBody @Validated UserRegisterVO userRegisterVO, BindingResult bindingResult) throws ParseException {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userRegister(userRegisterVO);
    }

    /**
     * <h1>用户登录</h1>
     * <hr/>
     * 用户登录接口
     *
     * @since v1.0.0
     * @param userLoginVO 用户登录信息
     * @return {@link BaseResponse}
     * @author 176yunxuan
     */
    @PostMapping("/user/login")
    public BaseResponse userLogin(@RequestBody UserLoginVO userLoginVO, BindingResult bindingResult){
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return userService.userLogin(userLoginVO);
    }
}
