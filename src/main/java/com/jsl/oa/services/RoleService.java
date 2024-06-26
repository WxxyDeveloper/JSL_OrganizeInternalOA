package com.jsl.oa.services;

import com.jsl.oa.exception.ClassCopyException;
import com.jsl.oa.model.vodata.RoleAddVo;
import com.jsl.oa.model.vodata.RoleEditVO;
import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>角色控制器接口</h1>
 * <hr/>
 * 该接口用于定义角色控制器的方法
 *
 * @version 1.1.0
 * @since v1.1.0
 */
public interface RoleService {
    BaseResponse roleAddUser(HttpServletRequest request, Long uid, Long rid);

    BaseResponse roleRemoveUser(HttpServletRequest request, Long uid);

    BaseResponse roleGet(HttpServletRequest request, String id);

    BaseResponse roleEdit(HttpServletRequest request, RoleEditVO roleEditVO);

    BaseResponse roleDelete(HttpServletRequest request, Long id);

    BaseResponse addRole(HttpServletRequest request, RoleAddVo roleAddVO) throws ClassCopyException;

    BaseResponse roleChangeUser(HttpServletRequest request, Long uid, Long rid);
}
