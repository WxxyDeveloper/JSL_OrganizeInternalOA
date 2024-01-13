package com.jsl.oa.services;

import org.apache.shiro.authc.Account;

public interface AccountService {
    Account findByUsername(String username);
}
