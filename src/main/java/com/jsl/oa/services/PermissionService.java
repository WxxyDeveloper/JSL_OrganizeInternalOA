package com.jsl.oa.services;

import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public interface PermissionService {

    BaseResponse permissionUser(HttpServletRequest request, Long uid);

    BaseResponse permissionGet(HttpServletRequest request);
}
