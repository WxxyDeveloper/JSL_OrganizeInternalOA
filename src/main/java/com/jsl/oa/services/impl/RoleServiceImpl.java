package com.jsl.oa.services.impl;

import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.model.voData.RoleAddUserVO;
import com.jsl.oa.model.voData.RoleRemoveUserVO;
import com.jsl.oa.services.RoleService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO;
    private final UserDAO userDAO;

    @Override
    public BaseResponse roleAddUser(Long uid,Long rid) {
        if(userDAO.isExistUser(uid)) {
            roleDAO.roleAddUser(uid,rid);
            return ResultUtil.success();
        } else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse roleRemoveUser(Long uid) {
        if(userDAO.isExistUser(uid)) {
            roleDAO.roleRemoveUser(uid);
            return ResultUtil.success();
        } else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }
}
