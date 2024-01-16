package com.jsl.oa.services.impl;

import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserDeleteVO;
import com.jsl.oa.model.voData.UserEditProfileVO;
import com.jsl.oa.model.voData.UserLockVO;
import com.jsl.oa.services.UserService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public UserDO getUserInfoByUsername(String username) {
        return userDAO.getUserInfoByUsername(username);
    }

    @Override
    public BaseResponse userDelete(UserDeleteVO userDeleteVO) {
        //判断用户是否存在
        if(userDAO.isExistUser(userDeleteVO.getId())) {
            userDAO.userDelete(userDeleteVO);
            return ResultUtil.success("删除成功");
        }else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse userLock(@NotNull UserLockVO userLockVO) {
        //判断用户是否存在
        if(userDAO.isExistUser(userLockVO.getId())) {
            userDAO.userLock(userLockVO);
            return ResultUtil.success("锁定成功");
        }else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse userEditProfile(@NotNull UserEditProfileVO userEditProfileVO) {
        if(userDAO.isExistUser(userEditProfileVO.getId())) {
            if(userEditProfileVO.getPassword()!=null) {
                userEditProfileVO.setPassword(BCrypt.hashpw(userEditProfileVO.getPassword(), BCrypt.gensalt()));
            }
            userDAO.userEditProfile(userEditProfileVO);
            return ResultUtil.success("修改成功");
        }else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }
}
