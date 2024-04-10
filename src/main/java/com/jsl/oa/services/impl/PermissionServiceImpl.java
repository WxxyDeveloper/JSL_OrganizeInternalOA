package com.jsl.oa.services.impl;

import com.jsl.oa.annotations.CheckUserHasPermission;
import com.jsl.oa.dao.PermissionDAO;
import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.PermissionMapper;
import com.jsl.oa.model.dodata.PermissionDO;
import com.jsl.oa.model.dodata.RoleUserDO;
import com.jsl.oa.model.vodata.PermissionContentVo;
import com.jsl.oa.model.vodata.PermissionEditVO;
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

/**
 * <h1>权限服务层实现类</h1>
 * <hr/>
 * 用于权限服务层的实现类,实现权限的增删改查,以及用户权限的获取
 *
 * @since v1.0.0
 * @version v1.1.0
 * @author xiao_lfeng | xiangZr-hhh | 176yunxuan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;
    private final RoleDAO roleDAO;
    private final PermissionDAO permissionDAO;
    private final UserDAO userDAO;

    @Override
    @CheckUserHasPermission("permission.add")
    public BaseResponse permissionAdd(HttpServletRequest request, Long rid, Long pid) {
        log.info("\t> 执行 Service 层 PermissionService.permissionAdd 方法");
        permissionMapper.permissionAdd(rid, pid);
        return ResultUtil.success();
    }

    @Override
    @CheckUserHasPermission("permission.user")
    public BaseResponse permissionUser(HttpServletRequest request, Long uid) {
        log.info("\t> 执行 Service 层 PermissionService.permissionUserPid 方法");
        if (userDAO.isExistUser(uid)) {
            // 此用户是否为管理员
            RoleUserDO roleUserDO = roleDAO.getRoleUserByUid(uid);
            List<String> getPermissionForString;
            if (roleUserDO != null) {
                // 获取全部根权限
                getPermissionForString = permissionDAO.getAllPermissionBuildString();
            } else {
                // 获取权限列表信息
                getPermissionForString = permissionDAO.getPermission(uid);
            }
            return ResultUtil.success(getPermissionForString);
        }
        return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }


    @Override
    @CheckUserHasPermission("permission.get")
    public BaseResponse permissionGet(HttpServletRequest request) {
        log.info("\t> 执行 Service 层 PermissionService.permissionGet 方法");
        //获取所有权限数据
        List<PermissionDO> permissionDOList = permissionMapper.getAllPermission();
        //将数据按父子类封装
        List<PermissionContentVo> permissionContentVos = Processing.convertToVoList(permissionDOList);

        return ResultUtil.success(permissionContentVos);
    }

    @Override
    @CheckUserHasPermission("permission.edit")
    public BaseResponse permissionEdit(PermissionEditVO permissionEditVo, HttpServletRequest request) {
        log.info("\t> 执行 Service 层 PermissionService.permissionEdit 方法");
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
    @CheckUserHasPermission("permission.delete")
    public BaseResponse permissionDelete(HttpServletRequest request, Long pid) {
        log.info("\t> 执行 Service 层 PermissionService.permissionDelete 方法");
        //删除权限
        if (!permissionMapper.deletePermission(pid)) {
            return ResultUtil.error(ErrorCode.DATABASE_DELETE_ERROR);
        }
        return ResultUtil.success();
    }
}
