package com.jsl.oa.services.impl;

import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserDeleteVO;
import com.jsl.oa.model.voData.UserLockVO;
import com.jsl.oa.services.UserService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        if(userDAO.isExistUser(userDeleteVO.getId())){
        userDAO.userDelete(userDeleteVO);
        return ResultUtil.success("删除成功");
        }else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse userLock(UserLockVO userLockVO) {
        //判断用户是否存在
        if(userDAO.isExistUser(userLockVO.getId())){
        userDAO.userLock(userLockVO);
        return ResultUtil.success("锁定成功");
        }else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }
}
