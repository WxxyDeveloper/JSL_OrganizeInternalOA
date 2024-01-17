package com.jsl.oa.dao;

import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserEditProfileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDAO {

    private final UserMapper userMapper;

    /**
     * <h2>用户名获取用户信息</h2>
     * <hr/>
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @author 筱锋xiao_lfeng
     * @return {@link UserDO}
     */
    public UserDO getUserInfoByUsername(String username) {
        UserDO userDO = null;
        // 从 Redis 获取数据
        // TODO: 10000-Redis: 从 Redis 获取数据
        // 从数据库获取用户信息
        if (userDO == null) {
            userDO = userMapper.getUserInfoByUsername(username);
        }
        return userDO;
    }

    /**
     * 根据id判断用户是否存在
     * @param id
     * @return
     */
    public Boolean isExistUser(Long id){
        if(userMapper.getUserById(id)==null) {
            return false;
        }else return true;
    }
    /**
     * 用户账号删除
     * @param id
     */
    public void userDelete(Long id) {
        userMapper.userDelete(id);
    }

    /**
     * 用户账号锁定
     * @param id
     */
    public void userLock(Long id) {
        userMapper.userLock(id);
    }

    public void userEditProfile(UserEditProfileVO userEditProfileVO) {
        userMapper.userEditProfile(userEditProfileVO);
    }
}
