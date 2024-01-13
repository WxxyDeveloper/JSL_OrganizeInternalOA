package com.jsl.oa.services;

import com.jsl.oa.model.voData.UserLoginVO;
import com.jsl.oa.model.voData.UserRegisterVO;
import com.jsl.oa.utils.BaseResponse;

import java.text.ParseException;

public interface AuthService {
    BaseResponse authRegister(UserRegisterVO userRegisterVO) throws ParseException;
    BaseResponse authLogin(UserLoginVO userLoginVO);
}
