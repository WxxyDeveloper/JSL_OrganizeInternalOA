package com.jsl.oa.services;

import com.jsl.oa.model.voData.UserLoginVO;
import com.jsl.oa.model.voData.UserRegisterVO;
import com.jsl.oa.utils.BaseResponse;

/**
 * <h1>用户认证服务接口</h1>
 * <hr/>
 * 用户认证服务接口，包含用户注册、用户登录、用户登出接口
 *
 * @version v1.1.0
 * @since v1.0.0
 */
public interface AuthService {
    /**
     * <h2>用户注册</h2>
     * <hr/>
     * 用户注册服务类操作
     *
     * @param userRegisterVO 用户注册信息
     * @return {@link BaseResponse}
     * @author 筱锋xiao_lfeng
     */
    BaseResponse authRegister(UserRegisterVO userRegisterVO);

    /**
     * <h2>用户登录</h2>
     * <hr/>
     * 用户登录服务类操作
     *
     * @param userLoginVO 用户登录信息
     * @return {@link BaseResponse}
     * @author 176yunxuan
     */
    BaseResponse authLogin(UserLoginVO userLoginVO);

    /**
     * <h2>邮箱登陆</h2>
     * <hr/>
     * 用户邮箱登陆服务类操作
     *
     * @param email 邮箱
     * @param code  验证码
     * @return {@link BaseResponse}
     */
    BaseResponse authLoginByEmail(String email, Integer code);

    /**
     * <h2>发送邮箱验证码</h2>
     * <hr/>
     * 用户邮箱登陆服务类操作
     *
     * @param email 邮箱
     * @return {@link BaseResponse}
     */
    BaseResponse authLoginSendEmailCode(String email);
}
