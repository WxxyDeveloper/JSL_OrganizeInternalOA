package com.jsl.oa.services.impl;

import com.jsl.oa.common.constant.BusinessConstants;
import com.jsl.oa.exception.BusinessException;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.doData.RoleUserDO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.*;
import com.jsl.oa.services.AuthService;
import com.jsl.oa.services.MailService;
import com.jsl.oa.utils.*;
import com.jsl.oa.utils.redis.EmailRedisUtil;
import com.jsl.oa.utils.redis.TokenRedisUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.regex.Pattern;

/**
 * <h1>用户认证服务实现类</h1>
 * <hr/>
 * 用户认证服务实现类，包含用户注册、用户登录、用户登出接口
 *
 * @version v1.1.0
 * @see AuthService
 * @since v1.0.0
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    private final MailService mailService;
    private final EmailRedisUtil<Integer> emailRedisUtil;
    private final TokenRedisUtil<String> tokenRedisUtil;

    @Override
    public BaseResponse authRegister(@NotNull UserRegisterVO userRegisterVO) {
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
        UserDO userDO;
        if (Pattern.matches("^[0-9A-Za-z_]{3,40}$", userLoginVO.getUser())) {
            // 是否为用户名
            userDO = userMapper.getUserInfoByUsername(userLoginVO.getUser());
        } else if (Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", userLoginVO.getUser())) {
            // 是否为手机号
            userDO = userMapper.getUserInfoByPhone(userLoginVO.getUser());
        } else if (Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", userLoginVO.getUser())) {
            // 是否为邮箱
            return ResultUtil.error(ErrorCode.EMAIL_LOGIN_NOT_SUPPORT);
        } else {
            // 工号
            userDO = userMapper.getUserByJobId(userLoginVO.getUser());
        }
        if (userDO != null) {
            // 账户是否有效
            if (userDO.getEnabled()) {
                // 获取用户并登陆
                if (BCrypt.checkpw(userLoginVO.getPassword(), userDO.getPassword())) {
                    return this.encapsulateDisplayContent(userDO);
                } else {
                    return ResultUtil.error(ErrorCode.WRONG_PASSWORD);
                }
            } else {
                return ResultUtil.error(ErrorCode.USER_IS_DEACTIVATED);
            }
        } else {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
    }

    @Override
    public BaseResponse authLoginByEmail(String email, Integer code) {
        // 获取验证码是否有效
        Integer redisCode = emailRedisUtil.getData(BusinessConstants.BUSINESS_LOGIN, email);
        if (redisCode != null) {
            if (redisCode.equals(code)) {
                // 删除验证码
                if (emailRedisUtil.delData(BusinessConstants.BUSINESS_LOGIN, email)) {
                    // 邮箱获取用户
                    UserDO userDO = userMapper.getUserInfoByEmail(email);
                    return this.encapsulateDisplayContent(userDO);
                } else {
                    return ResultUtil.error(ErrorCode.DATABASE_DELETE_ERROR);
                }
            }
        }
        return ResultUtil.error(ErrorCode.VERIFICATION_INVALID);
    }

    @Override
    public BaseResponse authLoginSendEmailCode(String email) {
        // 获取用户信息
        UserDO userDO = userMapper.getUserInfoByEmail(email);
        if (userDO != null) {
            // 账户是否有效
            if (userDO.getEnabled()) {
                // 生成验证码
                Integer code = Processing.createCode(null);
                // 存储验证码
                if (emailRedisUtil.setData(BusinessConstants.BUSINESS_LOGIN, email, code, 5)) {
                    // 发送邮件
                    if (mailService.sendMailAboutUserLogin(email, code)) {
                        return ResultUtil.success("验证码已发送");
                    } else {
                        return ResultUtil.error(ErrorCode.EMAIL_LOGIN_NOT_SUPPORT);
                    }
                } else {
                    return ResultUtil.error(ErrorCode.DATABASE_INSERT_ERROR);
                }
            } else {
                return ResultUtil.error(ErrorCode.USER_IS_DEACTIVATED);
            }
        } else {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
    }

    @Override
    public BaseResponse authChangePassword(HttpServletRequest request, @NotNull UserChangePasswordVO userChangePasswordVO) {
        // 检查新密码输入无误
        if (!userChangePasswordVO.getNewPassword().equals(userChangePasswordVO.getConfirmPassword())) {
            return ResultUtil.error(ErrorCode.PASSWORD_NOT_SAME);
        }
        // 检查用户
        UserDO userDO = userMapper.getUserById(Processing.getAuthHeaderToUserId(request));
        if (userDO != null) {
            // 检查旧密码
            if (BCrypt.checkpw(userChangePasswordVO.getOldPassword(), userDO.getPassword())) {
                // 更新密码
                if (userMapper.updateUserPassword(userDO.getId(), BCrypt.hashpw(userChangePasswordVO.getNewPassword(), BCrypt.gensalt()))) {
                    return ResultUtil.success("修改成功");
                } else {
                    return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
                }
            } else {
                return ResultUtil.error(ErrorCode.WRONG_PASSWORD);
            }
        } else {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
    }

    @Override
    public BaseResponse authLogout(HttpServletRequest request) {
        // 获取用户
        UserDO userDO = userMapper.getUserById(Processing.getAuthHeaderToUserId(request));
        // 删除Token
        if (tokenRedisUtil.delData(BusinessConstants.BUSINESS_LOGIN, userDO.getId().toString())) {
            return ResultUtil.success("登出成功");
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_DELETE_ERROR);
        }
    }

    @Override
    public BaseResponse authForgetPassword(UserForgetPasswordVO userForgetPasswordVO) {
        // 获取验证码是否有效
        Integer redisCode = emailRedisUtil.getData(BusinessConstants.BUSINESS_LOGIN, userForgetPasswordVO.getEmail());
        if (redisCode != null) {
            if (redisCode.equals(userForgetPasswordVO.getCheck())) {
                // 删除验证码
                if (emailRedisUtil.delData(BusinessConstants.BUSINESS_LOGIN, userForgetPasswordVO.getEmail())) {
                    // 邮箱获取用户
                    UserDO userDO = userMapper.getUserInfoByEmail(userForgetPasswordVO.getEmail());
                    // 更新密码
                    if (userMapper.updateUserPassword(userDO.getId(), BCrypt.hashpw(userForgetPasswordVO.getNewPassword(), BCrypt.gensalt()))) {
                        return ResultUtil.success("修改成功");
                    } else {
                        return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
                    }
                }
            }
        }
        return ResultUtil.error(ErrorCode.VERIFICATION_INVALID);
    }

    /**
     * <h2>封装返回内容</h2>
     * <hr/>
     * 封装返回内容
     *
     * @param userDO 用户信息
     * @return {@link BaseResponse}
     */
    private @NotNull BaseResponse encapsulateDisplayContent(@NotNull UserDO userDO) {
        // 授权 Token
        String token = JwtUtil.generateToken(userDO.getId());
        UserReturnBackVO userReturnBackVO = new UserReturnBackVO();
        // Token 上传到 Redis
        tokenRedisUtil.setData(BusinessConstants.BUSINESS_LOGIN, userDO.getId().toString(), token, 1440);
        // 获取用户角色
        RoleUserDO getUserRole = roleMapper.getRoleUserByUid(userDO.getId());
        if (getUserRole == null) {
            getUserRole = new RoleUserDO();
            getUserRole.setRid(0L)
                    .setCreatedAt(new Timestamp(System.currentTimeMillis()));
        } else {
            getUserRole.setUid(null);
        }
        userReturnBackVO.setAddress(userDO.getAddress())
                .setAge(userDO.getAge())
                .setEmail(userDO.getEmail())
                .setJobId(userDO.getJobId())
                .setPhone(userDO.getPhone())
                .setSex(userDO.getSex())
                .setUsername(userDO.getUsername())
                .setToken(token)
                .setRole(getUserRole);
        return ResultUtil.success("登陆成功", userReturnBackVO);
    }
}
