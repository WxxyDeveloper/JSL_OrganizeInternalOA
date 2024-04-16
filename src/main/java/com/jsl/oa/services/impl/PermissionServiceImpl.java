package com.jsl.oa.services.impl;

import com.jsl.oa.dao.PermissionDAO;
import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.model.dodata.PermissionDO;
import com.jsl.oa.model.dodata.RoleUserDO;
import com.jsl.oa.model.vodata.PermissionContentVO;
import com.jsl.oa.services.PermissionService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>权限服务层实现类</h1>
 * <hr/>
 * 用于权限服务层的实现类,实现权限的增删改查,以及用户权限的获取
 *
 * @author xiao_lfeng | xiangZr-hhh | 176yunxuan
 * @version v1.1.0
 * @since v1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final RoleDAO roleDAO;
    private final PermissionDAO permissionDAO;
    private final UserDAO userDAO;

    @Override
    public BaseResponse permissionUser(HttpServletRequest request, Long uid) {
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
    public BaseResponse permissionGet(HttpServletRequest request) {
        //获取所有权限数据
        List<PermissionDO> permissionDOList = permissionDAO.getAllPermission();
        List<PermissionContentVO> permissionContentVO = new ArrayList<>();
        BeanUtils.copyProperties(permissionDOList, permissionContentVO);
        return ResultUtil.success(permissionContentVO);
    }
}
