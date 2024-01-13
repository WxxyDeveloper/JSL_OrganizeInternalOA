package com.jsl.oa.services.impl;

import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserLoginVO;
import com.jsl.oa.model.voData.UserRegisterVO;
import com.jsl.oa.exception.BusinessException;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.services.AuthService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;

    /**
     * <h1>用户注册</h1>
     * <hr/>
     * 用户注册服务类操作
     *
     * @param userRegisterVO 用户注册信息
     * @return {@link BaseResponse}
     * @throws ParseException 日期转换异常
     */
    @Override
    public BaseResponse authRegister(UserRegisterVO userRegisterVO) throws ParseException {
        // 用户检查是否存在
        UserDO getUserByUsername = userMapper.getUserByUsername(userRegisterVO.getUsername());
        // 用户名已存在
        if (getUserByUsername != null) {
            return ResultUtil.error(ErrorCode.USER_EXIST);
        }

        // 生成工号
        String userNum;
        do {
            userNum = Processing.createJobNumber((short) 2);
        } while (userMapper.getUserByUserNum(userNum) != null);

        // 数据上传
        Date getDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(userRegisterVO.getAge()).getTime());
        UserDO userDO = new UserDO();
        userDO.πsetUserNum(userNum)
                .setUsername(userRegisterVO.getUsername())
                .setPassword(BCrypt.hashpw(userRegisterVO.getPassword(), BCrypt.gensalt()))
                .setSex(userRegisterVO.getSex())
                .setAge(getDate)
                .setUnit(userRegisterVO.getUnit())
                .setFiled(userRegisterVO.getFiled())
                .setHometown(userRegisterVO.getHometown())
                .setKind("0")
                .setState("0");
        // 插入数据
        if (userMapper.insertUser(userDO)) {
            userDO.setPassword(null);
            return ResultUtil.success("注册成功", userDO);
        } else {
            throw new BusinessException(ErrorCode.DATABASE_INSERT_ERROR);
        }
    }

    @Override
    public BaseResponse authLogin(UserLoginVO userLoginVO) {
        String pwd = userLoginVO.getPassword();
        String encodePwd = userMapper.loginPassword(userLoginVO);
        if (encodePwd == null) {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
        if (BCrypt.checkpw(pwd, encodePwd)) {
            return ResultUtil.success("登陆成功", userMapper.login(userLoginVO));
        } else return ResultUtil.error(ErrorCode.WRONG_PASSWORD);

    }
}
