package com.jsl.oa.controllers;

import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PostMapping("/user/register")
    public BaseResponse userRegister() {
        return ResultUtil.error(ErrorCode.WRONG_PASSWORD);
    }
}
