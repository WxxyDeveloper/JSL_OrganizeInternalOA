package com.jsl.oa.dao;

import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserDeleteVO;
import com.jsl.oa.model.voData.UserLockVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDAO {

    private final UserMapper userMapper;

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

    public Boolean isExistUser(Long id){
        if(userMapper.getUserById(id)==null) {
            return false;
        }else return true;
    }
    /**
     * 用户账号删除
     * @param userDeleteVO
     */
    public void userDelete(UserDeleteVO userDeleteVO) {
        userMapper.userDelete(userDeleteVO);
    }

    /**
     * 用户账号锁定
     * @param userLockVO
     */
    public void userLock(UserLockVO userLockVO) {
        userMapper.userLock(userLockVO);
    }
}
