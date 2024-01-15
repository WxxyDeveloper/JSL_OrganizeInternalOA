package com.jsl.oa.services;

import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserDeleteVO;
import com.jsl.oa.model.voData.UserEditProfile;
import com.jsl.oa.model.voData.UserLockVO;
import com.jsl.oa.utils.BaseResponse;

/**
 * <h1>用户控制器接口</h1>
 * <hr/>
 *
 * <p>该接口用于定义用户控制器的方法</p>
 *
 * @version 1.1.0
 * @since v1.1.0
 * @author 筱锋xiao_lfeng
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
     * @param userDeleteVO
     * @return
     */
    BaseResponse userDelete(UserDeleteVO userDeleteVO);

    /**
     * 用户账号锁定
     * @param userLockVO
     * @return
     */
    BaseResponse userLock(UserLockVO userLockVO);

    BaseResponse userEditProfile(UserEditProfile userEditProfile);
}
