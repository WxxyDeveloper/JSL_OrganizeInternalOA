package com.jsl.oa.services;

import com.jsl.oa.common.doData.UserDO;
import com.jsl.oa.common.voData.UserRegisterVO;
import com.jsl.oa.utils.BaseResponse;

import java.text.ParseException;

public interface UserService {
    BaseResponse userRegister(UserRegisterVO userRegisterVO) throws ParseException;

    BaseResponse userLogin(UserDO userDO);
}
