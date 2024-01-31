package com.jsl.oa.services.impl;

import com.jsl.oa.annotations.CheckUserAbleToUse;
import com.jsl.oa.common.constant.BusinessConstants;
import com.jsl.oa.dao.PermissionDAO;
import com.jsl.oa.dao.RoleDAO;
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
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <h1>用户认证服务实现类</h1>
 * <hr/>
 * 用户认证服务实现类，包含用户注册、用户登录、用户登出接口
 *
 * @version v1.1.0
 * @see AuthService
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final RoleDAO roleDAO;
    private final PermissionDAO permissionDAO;

    private final MailService mailService;
    private final EmailRedisUtil<Integer> emailRedisUtil;
    private final TokenRedisUtil<String> tokenRedisUtil;

    @Override
    public BaseResponse authRegister(@NotNull UserRegisterVO userRegisterVO) {
        log.info("\t> 执行 Service 层 AuthService.userEdit 方法");
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
            //默认角色为学生
            UserDO userDO1 = userMapper.getUserByUserNum(userDO.getUsername());
            roleDAO.addRoleUser(userDO1.getId(), 2L);
            return ResultUtil.success("注册成功", userDO);
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_INSERT_ERROR);
        }
    }

    @Override
    public BaseResponse authLogin(@NotNull UserLoginVO userLoginVO) {
        log.info("\t> 执行 Service 层 AuthService.userLogin 方法");
        // 检查用户是否存在
        UserDO userDO;
        if (Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", userLoginVO.getUser())) {
            // 是否为手机号
            log.info("\t\t> 手机号登陆");
            userDO = userMapper.getUserInfoByPhone(userLoginVO.getUser());
        } else if (Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", userLoginVO.getUser())) {
            // 是否为邮箱
            log.info("\t\t> 邮箱登陆");
            return ResultUtil.error(ErrorCode.EMAIL_LOGIN_NOT_SUPPORT);
        } else if (Pattern.matches("^(STU|TEA|OTH)[0-9]{7}", userLoginVO.getUser())) {
            // 工号
            log.info("\t\t> 工号登陆");
            userDO = userMapper.getUserByJobId(userLoginVO.getUser());
        } else {
            // 是否为用户名
            log.info("\t\t> 用户名登陆");
            userDO = userMapper.getUserInfoByUsername(userLoginVO.getUser());
        }
        if (userDO != null) {
            // 账户是否有效
            if (userDO.getEnabled()) {
                if (userDO.getAccountNoLocked()) {
                    // 获取用户并登陆
                    if (BCrypt.checkpw(userLoginVO.getPassword(), userDO.getPassword())) {
                        log.info("\t\t> 登陆成功，用户 [{}]{}", userDO.getId(), userDO.getUsername());
                        return this.encapsulateDisplayContent(userDO);
                    } else {
                        return ResultUtil.error(ErrorCode.WRONG_PASSWORD);
                    }
                } else {
                    return ResultUtil.error(ErrorCode.USER_IS_LOCKED);
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
        log.info("\t> 执行 Service 层 AuthService.authLoginByEmail 方法");
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
        log.info("\t> 执行 Service 层 AuthService.authLoginSendEmailCode 方法");
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
                    mailService.sendMailAboutUserLogin(email, code);
                    return ResultUtil.success("验证码已发送");
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
    @CheckUserAbleToUse
    public BaseResponse authChangePassword(HttpServletRequest request, @NotNull UserChangePasswordVO userChangePasswordVO) {
        log.info("\t> 执行 Service 层 AuthService.authChangePassword 方法");
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
    @CheckUserAbleToUse
    public BaseResponse authLogout(HttpServletRequest request) {
        log.info("\t> 执行 Service 层 AuthService.authLogout 方法");
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
    public BaseResponse authForgetPassword(@NotNull UserForgetPasswordVO userForgetPasswordVO) {
        log.info("\t> 执行 Service 层 AuthService.authForgetPassword 方法");
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
        RoleUserDO roleUserDO = roleDAO.getRoleUserByUid(userDO.getId());
        List<String> getPermissionForString;
        if (roleUserDO != null) {
            // 获取全部根权限
            getPermissionForString = permissionDAO.getAllPermissionBuildString();
        } else {
            // 获取权限列表信息
            getPermissionForString = permissionDAO.getPermission(userDO.getId());
        }
        // 获取用户角色
        RoleUserDO getUserRole = roleDAO.roleMapper.getRoleUserByUid(userDO.getId());
        if (getUserRole == null) {
            getUserRole = new RoleUserDO();
            getUserRole.setRid(0L)
                    .setCreatedAt(new Timestamp(System.currentTimeMillis()));
        } else {
            getUserRole.setUid(null);
        }
        userReturnBackVO.setUser(new UserReturnBackVO.ReturnUser()
                        .setId(userDO.getId())
                        .setJobId(userDO.getJobId())
                        .setUsername(userDO.getUsername())
                        .setEmail(userDO.getEmail())
                        .setPhone(userDO.getPhone()))
                .setRole(new UserReturnBackVO.ReturnUserRole()
                        .setRid(getUserRole.getRid()))
                .setToken(token)
                .setPermission(getPermissionForString);
        return ResultUtil.success("登陆成功", userReturnBackVO);
    }
}
