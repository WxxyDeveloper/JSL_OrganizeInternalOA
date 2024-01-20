package com.jsl.oa.services;

import com.jsl.oa.model.voData.PermissionEditVo;
import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public interface PermissionService {

    BaseResponse permissionAdd(HttpServletRequest request, Long rid, Long pid);

    BaseResponse permissionUser(HttpServletRequest request, Long uid);

    BaseResponse permissionGet(HttpServletRequest request);

    BaseResponse permissionEdit(PermissionEditVo permissionEditVo, HttpServletRequest request);

    BaseResponse permissionDelete(HttpServletRequest request, Long pid);
}
