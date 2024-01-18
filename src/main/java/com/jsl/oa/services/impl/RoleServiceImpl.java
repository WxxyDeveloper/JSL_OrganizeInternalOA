package com.jsl.oa.services.impl;

import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.model.doData.RoleDO;
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
    private final UserDAO userDAO;

    @Override
    public BaseResponse roleAddUser(Long uid, Long rid) {
        if (userDAO.isExistUser(uid)) {
            roleDAO.roleAddUser(uid, rid);
            return ResultUtil.success();
        } else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse roleRemoveUser(Long uid) {
        if (userDAO.isExistUser(uid)) {
            roleDAO.roleRemoveUser(uid);
            return ResultUtil.success();
        } else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
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
                getRoleList = (ArrayList<RoleDO>) roleDAO.getRoleById(id);
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
}
