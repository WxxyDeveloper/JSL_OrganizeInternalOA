package com.jsl.oa.dao;

import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.doData.UserDO;
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
}
