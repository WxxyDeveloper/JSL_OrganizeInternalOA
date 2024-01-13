package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.UserLoginVO;
import com.jsl.oa.model.voData.UserRegisterVO;
import com.jsl.oa.services.AuthService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class PermissionController {
}
