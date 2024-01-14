package com.jsl.oa.services.impl;

import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public UserDO getUserInfoByUsername(String username) {
        return userDAO.getUserInfoByUsername(username);
    }
}
