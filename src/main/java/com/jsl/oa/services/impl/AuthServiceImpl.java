package com.jsl.oa.services.impl;

import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserLoginVO;
import com.jsl.oa.model.voData.UserReturnBackVO;
import com.jsl.oa.model.voData.UserRegisterVO;
import com.jsl.oa.exception.BusinessException;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.services.AuthService;
import com.jsl.oa.utils.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * <h1>用户认证服务实现类</h1>
 * <hr/>
 * 用户认证服务实现类，包含用户注册、用户登录、用户登出接口
 *
 * @since v1.0.0
 * @version v1.1.0
 * @see AuthService
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;

    @Override
    public BaseResponse authRegister(UserRegisterVO userRegisterVO) {
        // 检查用户说是否存在
        UserDO getUserByUsername = userMapper.getUserInfoByUsername(userRegisterVO.getUsername());
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
        UserDO userDO = new UserDO();
        userDO.setJobId(userNum)
                .setUsername(userRegisterVO.getUsername())
                .setPassword(BCrypt.hashpw(userRegisterVO.getPassword(), BCrypt.gensalt()))
                .setAddress(userRegisterVO.getAddress())
                .setPhone(userRegisterVO.getPhone())
                .setEmail(userRegisterVO.getEmail())
                .setAge(userRegisterVO.getAge())
                .setSex(userRegisterVO.getSex());
        // 插入数据
        if (userMapper.insertUser(userDO)) {
            userDO.setPassword(null);
            return ResultUtil.success("注册成功", userDO);
        } else {
            throw new BusinessException(ErrorCode.DATABASE_INSERT_ERROR);
        }
    }

    @Override
    public BaseResponse authLogin(@NotNull UserLoginVO userLoginVO) {
        // 检查用户是否存在
        UserDO userDO = userMapper.getUserInfoByUsername(userLoginVO.getUser());
        if (userDO != null) {
            // 获取用户并登陆
            if (BCrypt.checkpw(userLoginVO.getPassword(), userDO.getPassword())) {
                UserReturnBackVO userReturnBackVO = new UserReturnBackVO();
                // 授权 Token
                String token = JwtUtil.generateToken(userDO.getUsername());
                userReturnBackVO.setAddress(userDO.getAddress())
                        .setAge(userDO.getAge())
                        .setEmail(userDO.getEmail())
                        .setJobId(userDO.getJobId())
                        .setPhone(userDO.getPhone())
                        .setSex(userDO.getSex())
                        .setUsername(userDO.getUsername())
                        .setToken(token);
                return ResultUtil.success("登陆成功", userReturnBackVO);
            } else {
                return ResultUtil.error(ErrorCode.WRONG_PASSWORD);
            }
        } else {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
    }

    }
}
