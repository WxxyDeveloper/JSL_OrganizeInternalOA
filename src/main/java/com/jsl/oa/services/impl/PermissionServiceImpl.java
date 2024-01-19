package com.jsl.oa.services.impl;

import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.PermissionMapper;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.services.PermissionService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;
    private final RoleMapper roleMapper;
    private final UserDAO userDAO;

    @Override
    public BaseResponse permissionAdd(HttpServletRequest request, Long rid, Long pid) {
        if(!Processing.checkUserIsAdmin(request,roleMapper)){
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        permissionMapper.permissionAdd(rid,pid);
        return null;
    }

    @Override
    public BaseResponse permissionUser(HttpServletRequest request, Long uid) {
        if(userDAO.isExistUser(uid)){
            List<String> permission = permissionMapper.permissionUser(uid);
           return ResultUtil.success(permission);
        }
        return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }
}
