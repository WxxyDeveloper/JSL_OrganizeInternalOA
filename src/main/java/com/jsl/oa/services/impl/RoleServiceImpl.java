package com.jsl.oa.services.impl;

import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.exception.ClassCopyException;
import com.jsl.oa.model.doData.RoleDO;
import com.jsl.oa.model.voData.RoleAddVo;
import com.jsl.oa.model.voData.RoleEditVO;
import com.jsl.oa.services.RoleService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO;

    @Override
    public BaseResponse roleAddUser(HttpServletRequest request, Long uid, Long rid) {
        if (Processing.checkUserIsAdmin(request, roleDAO.roleMapper)) {
            roleDAO.roleAddUser(uid, rid);
            return ResultUtil.success();
        } else return ResultUtil.error(ErrorCode.NOT_ADMIN);
    }

    @Override
    public BaseResponse roleRemoveUser(HttpServletRequest request, Long uid) {
        if (Processing.checkUserIsAdmin(request, roleDAO.roleMapper)) {
            roleDAO.roleRemoveUser(uid);
            return ResultUtil.success();
        } else return ResultUtil.error(ErrorCode.NOT_ADMIN);
    }

    @Override
    public BaseResponse roleChangeUser(HttpServletRequest request, Long uid, Long rid) {
        if (Processing.checkUserIsAdmin(request, roleDAO.roleMapper)) {
            // TODO: 2023-01-20|List:10002-未判断用户是否存在
            // TODO: 2023-01-20|List:10003-保险起见，默认用户主键为 1 的用户为超级管理员
            //  （不可以修改自己权限组，避免修改后不存在管理员，无管理组）
            if (!roleDAO.roleChangeUser(uid, rid)) {
                return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
            }
            return ResultUtil.success();
        } else return ResultUtil.error(ErrorCode.NOT_ADMIN);
    }

    @Override
    public BaseResponse roleGet(HttpServletRequest request, String id) {
        // 检查用户权限
        if (!Processing.checkUserIsAdmin(request, roleDAO.roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        // 获取 Role 权限组
        ArrayList<RoleDO> getRoleList;
        if (id != null && !id.isEmpty()) {
            if (Pattern.matches("^[0-9]+$", id)) {
                getRoleList = (ArrayList<RoleDO>) roleDAO.getRolesById(id);
            } else {
                ArrayList<String> error = new ArrayList<>();
                error.add("id 只能为数字");
                return ResultUtil.error(ErrorCode.PARAMETER_ERROR, error);
            }
        } else {
            getRoleList = (ArrayList<RoleDO>) roleDAO.getRole();
            getRoleList.add(getRoleList.size(), new RoleDO().setId(0L).setRoleName("none"));
        }
        // 返回数据
        return ResultUtil.success(getRoleList);
    }

    @Override
    public BaseResponse roleEdit(HttpServletRequest request, RoleEditVO roleEditVO) {
        // 检查用户权限
        if (!Processing.checkUserIsAdmin(request, roleDAO.roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        // 获取 Role 相关信息
        RoleDO getRole = roleDAO.getRoleById(roleEditVO.getId());
        // 判断是否存在该 Role
        if (getRole != null) {
            // 替换 Role 信息
            getRole.setRoleName(roleEditVO.getName()).setDisplayName(roleEditVO.getDisplayName());
            // 更新 Role 信息
            if (roleDAO.roleEdit(getRole)) {
                return ResultUtil.success();
            } else {
                return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
            }
        } else {
            return ResultUtil.error(ErrorCode.ROLE_NOT_FOUNDED);
        }
    }

    @Override
    public BaseResponse roleDelete(HttpServletRequest request, Long id) {
        // 检查用户权限
        if (!Processing.checkUserIsAdmin(request, roleDAO.roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        // 获取 Role 相关信息
        RoleDO getRole = roleDAO.getRoleById(id);
        // 判断是否存在该 Role
        if (getRole != null) {
            // 删除 Role 信息
            if (roleDAO.roleDelete(id)) {
                return ResultUtil.success();
            } else {
                return ResultUtil.error(ErrorCode.DATABASE_DELETE_ERROR);
            }
        } else {
            return ResultUtil.error(ErrorCode.ROLE_NOT_FOUNDED);
        }

    }

    @Override
    public BaseResponse addRole(HttpServletRequest request, RoleAddVo roleAddVO) throws ClassCopyException {
        // 检查用户权限
        if (!Processing.checkUserIsAdmin(request, roleDAO.roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        // 检查权限名称是否重复
        String roleName = roleAddVO.getName();
        RoleDO roleDO = new RoleDO();
        if (!roleDAO.isExistRoleByRoleName(roleName)) {
            Processing.copyProperties(roleAddVO, roleDO);
            roleDO.setRoleName(roleAddVO.getName());
        } else {
            return ResultUtil.error(ErrorCode.ROLE_NAME_REPEAT);
        }
        //向数据库中插入数据
        roleDAO.roleAdd(roleDO);

        return ResultUtil.success();
    }


}
