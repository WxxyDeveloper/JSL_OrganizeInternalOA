package com.jsl.oa.services.impl;

import com.jsl.oa.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.Account;

@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;

    @Override
    public Account findByUsername(String username) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username",username);
        return accountMapper.selectOne(wrapper);
    }
}
