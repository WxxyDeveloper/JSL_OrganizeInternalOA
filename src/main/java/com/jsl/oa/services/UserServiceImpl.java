package com.jsl.oa.services;

import com.jsl.oa.common.doData.UserDO;
import com.jsl.oa.common.voData.UserRegisterVO;
import com.jsl.oa.mapper.UserMapper;
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
public class UserServiceImpl implements UserService {
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
    public BaseResponse userRegister(UserRegisterVO userRegisterVO) throws ParseException {
        // 用户检查是否存在
        UserDO getUserByUsername = userMapper.getUserByUsername(userRegisterVO.getUsername());
        // 用户名已存在
        if (getUserByUsername != null) {
            return ResultUtil.error(ErrorCode.USERNAME_EXIST);
        }

        // 生成工号
        String userNum;
        do {
            userNum = Processing.createJobNumber((short) 2);
        } while (userMapper.getUserByUserNum(userNum) != null);

        // 数据上传
        Date getDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(userRegisterVO.getAge()).getTime());
        UserDO userDO = new UserDO();
        userDO.setUserNum(userNum)
                .setUsername(userRegisterVO.getUsername())
                .setPassword(BCrypt.hashpw(userRegisterVO.getPassword(), BCrypt.gensalt()))
                .setSex(userRegisterVO.getSex())
                .setAge(getDate)
                .setUnit(userRegisterVO.getUnit())
                .setFiled(userRegisterVO.getFiled())
                .setHometown(userRegisterVO.getHometown())
                .setKind("注册用户")
                .setStatus("注册状态");
        // 插入数据
        if (userMapper.insertUser(userDO)) {
            return ResultUtil.success("注册成功");
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_INSERT_ERROR);
        }
    }

    @Override
    public BaseResponse userLogin(UserDO userDO) {
        String pwd = userDO.getPassword();
        String encodePwd = userMapper.loginPassword(userDO);
        boolean a = BCrypt.checkpw(pwd, encodePwd);
        if(BCrypt.checkpw(pwd, encodePwd))
        {
            return ResultUtil.success(userMapper.login(userDO));
        }else return ResultUtil.error(ErrorCode.WRONG_PASSWORD);

    }
}
