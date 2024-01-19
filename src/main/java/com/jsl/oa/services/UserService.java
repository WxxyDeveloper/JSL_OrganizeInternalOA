package com.jsl.oa.services;

import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserAddVo;
import com.jsl.oa.model.voData.UserAllCurrentVO;
import com.jsl.oa.model.voData.UserEditProfileVO;
import com.jsl.oa.model.voData.UserEditVo;
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
     * <hr/>
     * 该方法用于根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserDO getUserInfoByUsername(String username);

    /**
     * <h2>用户账号删除</h2>
     * <hr/>
     * 该方法用于用户账号删除
     *
     * @param id 用户id
     * @return {@link BaseResponse}
     */
    BaseResponse userDelete(HttpServletRequest request,Long id);

    /**
     * <h2>账号锁定</h2>
     * <hr/>
     * 该方法用于用户账号锁定
     *
     * @param id 用户id
     * @return {@link BaseResponse}
     */
    BaseResponse userLock(HttpServletRequest request,Long id,Long isLock);

    /**
     * <h2>用户编辑自己的信息</h2>
     * <hr/>
     * 该方法用于用户编辑自己的信息
     *
     * @param userEditProfileVO 用户编辑自己的信息
     * @return {@link BaseResponse}
     */
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

    /**
     * <h2>获取当前用户信息</h2>
     * <hr/>
     * 该方法用于获取当前用户信息
     *
     * @param request  请求
     * @param id       用户id
     * @param username 用户名
     * @param email    邮箱
     * @param phone    手机号
     * @return {@link BaseResponse}
     */
    BaseResponse userCurrent(HttpServletRequest request, String id, String username, String email, String phone);


    BaseResponse userAdd(UserAddVo userAddVo, HttpServletRequest request);

    BaseResponse userEdit(UserEditVo userEditVo, HttpServletRequest request);


    BaseResponse userProfileGet(HttpServletRequest request);


}
