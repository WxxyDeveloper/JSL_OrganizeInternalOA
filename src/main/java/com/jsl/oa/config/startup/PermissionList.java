package com.jsl.oa.config.startup;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Getter
public class PermissionList {
    @Getter
    @RequiredArgsConstructor
    public static class PermissionVO {
        private final String name;
        private final String desc;
    }
    private final ArrayList<PermissionList.PermissionVO> permissionList = new ArrayList<>();
    private final ArrayList<PermissionList.PermissionVO> permissionPrincipal = new ArrayList<>();
    private final ArrayList<PermissionList.PermissionVO> permissionDeveloper = new ArrayList<>();


    public PermissionList() {
        permissionList.add(new PermissionVO("auth:change_password", "修改密码"));
        permissionList.add(new PermissionVO("info:get_header_image", "获取头部图片"));
        permissionList.add(new PermissionVO("info:edit_header_image", "编辑头部图片"));
        permissionList.add(new PermissionVO("info:delete_header_image", "删除头部图片"));

        permissionPrincipal.add(new PermissionVO("auth:change_password", "修改密码"));
        permissionPrincipal.add(new PermissionVO("info:get_header_image", "获取头部图片"));
        permissionPrincipal.add(new PermissionVO("info:edit_header_image", "编辑头部图片"));
        permissionPrincipal.add(new PermissionVO("info:delete_header_image", "删除头部图片"));

        permissionDeveloper.add(new PermissionVO("auth:change_password", "修改密码"));
        permissionDeveloper.add(new PermissionVO("info:get_header_image", "获取头部图片"));
        permissionDeveloper.add(new PermissionVO("info:edit_header_image", "编辑头部图片"));
        permissionDeveloper.add(new PermissionVO("info:delete_header_image", "删除头部图片"));
    }
}
