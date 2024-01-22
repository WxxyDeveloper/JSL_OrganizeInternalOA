package com.jsl.oa.services.impl;

import com.jsl.oa.dao.PermissionDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.PermissionMapper;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.PermissionDO;
import com.jsl.oa.model.voData.PermissionContentVo;
import com.jsl.oa.model.voData.PermissionEditVO;
import com.jsl.oa.services.PermissionService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;
    private final RoleMapper roleMapper;
    private final PermissionDAO permissionDAO;
    private final UserDAO userDAO;

    @Override
    public BaseResponse permissionAdd(HttpServletRequest request, Long rid, Long pid) {
        log.info("\t> 执行 Service 层 PermissionService.permissionAdd 方法");
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        permissionMapper.permissionAdd(rid, pid);
        return ResultUtil.success();
    }

    @Override
    public BaseResponse permissionUser(HttpServletRequest request, Long uid) {
        log.info("\t> 执行 Service 层 PermissionService.permissionUserPid 方法");
        if (userDAO.isExistUser(uid)) {
            // 获取权限列表信息
            List<String> getPermissionForString = permissionDAO.getPermission(uid);
            return ResultUtil.success(getPermissionForString);
        }
        return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }


    @Override
    public BaseResponse permissionGet(HttpServletRequest request) {
        log.info("\t> 执行 Service 层 PermissionService.permissionGet 方法");
        //检验用户权限是否为管理员
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        //获取所有权限数据
        List<PermissionDO> permissionDOList = permissionMapper.getAllPermission();
        //将数据按父子类封装
        List<PermissionContentVo> permissionContentVos = Processing.convertToVoList(permissionDOList);

        return ResultUtil.success(permissionContentVos);
    }

    @Override
    public BaseResponse permissionEdit(PermissionEditVO permissionEditVo, HttpServletRequest request) {
        log.info("\t> 执行 Service 层 PermissionService.permissionEdit 方法");
        //检验用户权限是否为管理员
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        //根据id获取对应permission数据
        PermissionDO permissionDO = permissionMapper.getPermissionById(permissionEditVo.getId());
        if (permissionDO == null) {
            return ResultUtil.error(ErrorCode.PERMISSION_NOT_EXIST);
        }
        //传递要编辑的数据
        Processing.copyProperties(permissionEditVo, permissionDO);
        //更新permission
        if (!permissionMapper.updatePermission(permissionDO)) {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
        return ResultUtil.success();
    }

    @Override
    public BaseResponse permissionDelete(HttpServletRequest request, Long pid) {
        log.info("\t> 执行 Service 层 PermissionService.permissionDelete 方法");
        //检验用户权限是否为管理员
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        //删除权限
        if (!permissionMapper.deletePermission(pid)) {
            return ResultUtil.error(ErrorCode.DATABASE_DELETE_ERROR);
        }
        return ResultUtil.success();
    }
}
