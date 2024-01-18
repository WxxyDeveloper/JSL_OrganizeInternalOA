package com.jsl.oa.services;

import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserAllCurrentVO;
import com.jsl.oa.model.voData.UserEditProfileVO;
import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>用户控制器接口</h1>
 * <hr/>
 * 该接口用于定义用户控制器的方法
 *
 * @version 1.1.0
 * @since v1.1.0
 */
public interface UserService {
    /**
     * <h2>根据用户名获取用户信息</h2>
     *
     * <p>该方法用于根据用户名获取用户信息</p>
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserDO getUserInfoByUsername(String username);

    /**
     * 用户账号删除
     *
     * @param id
     * @return
     */
    BaseResponse userDelete(Long id);

    /**
     * 用户账号锁定
     *
     * @param id
     * @return
     */
    BaseResponse userLock(Long id);

    BaseResponse userEditProfile(UserEditProfileVO userEditProfileVO);

    /**
     * <h2>获取全部的用户信息</h2>
     * <hr/>
     * 该方法用于获取全部的用户信息
     *
     * @param request          请求
     * @param userAllCurrentVO 输入信息
     * @return {@link BaseResponse}
     */
    BaseResponse userCurrentAll(HttpServletRequest request, UserAllCurrentVO userAllCurrentVO);
}
