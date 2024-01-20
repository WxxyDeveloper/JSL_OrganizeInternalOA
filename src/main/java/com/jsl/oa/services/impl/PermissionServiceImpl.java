package com.jsl.oa.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.PermissionMapper;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.PermissionDO;
import com.jsl.oa.model.doData.RolePermissionDO;
import com.jsl.oa.model.voData.PermissionContentVo;
import com.jsl.oa.model.voData.PermissionEditVo;
import com.jsl.oa.services.PermissionService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return ResultUtil.success();
    }

    @Override
    public BaseResponse permissionUser(HttpServletRequest request, Long uid) {
        if(userDAO.isExistUser(uid)){
            List<String> permission = permissionMapper.permissionUser(uid);
           return ResultUtil.success(permission);
        }
        return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }


    @Override
    public BaseResponse permissionGet(HttpServletRequest request) {
        //检验用户权限是否为管理员
        if(!Processing.checkUserIsAdmin(request,roleMapper)){
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        //获取所有权限数据
        List<PermissionDO> permissionDOS = permissionMapper.getAllPermission();
        //将数据按父子类封装
        List<PermissionContentVo> permissionContentVos = Processing.convertToVoList(permissionDOS);

        return ResultUtil.success(permissionContentVos);
    }

    @Override
    public BaseResponse permissionEdit(PermissionEditVo permissionEditVo, HttpServletRequest request) {
        //检验用户权限是否为管理员
        if(!Processing.checkUserIsAdmin(request,roleMapper)){
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        //根据id获取对应permission数据
        PermissionDO permissionDO = permissionMapper.permissionGetById(permissionEditVo.getId());
        if(permissionDO == null){
            return ResultUtil.error(ErrorCode.PERMISSION_NOT_EXIST);
        }
        //传递要编辑的数据
        Processing.copyProperties(permissionEditVo,permissionDO);
        //更新permission
        if(!permissionMapper.updatePermission(permissionDO)){
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
        return ResultUtil.success();
    }


}
