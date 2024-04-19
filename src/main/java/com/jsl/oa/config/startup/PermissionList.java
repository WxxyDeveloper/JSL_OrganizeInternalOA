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
        permissionList.add(new PermissionVO("project:child_add", "增加子系统"));
        permissionList.add(new PermissionVO("project:module_add", "增加子模块"));

        permissionPrincipal.add(new PermissionVO("auth:change_password", "修改密码"));
        permissionPrincipal.add(new PermissionVO("info:get_header_image", "获取头部图片"));
        permissionPrincipal.add(new PermissionVO("info:edit_header_image", "编辑头部图片"));
        permissionPrincipal.add(new PermissionVO("info:delete_header_image", "删除头部图片"));
        permissionPrincipal.add(new PermissionVO("project:add", "增加项目"));
        permissionPrincipal.add(new PermissionVO("project:child_add", "增加子系统"));
        permissionPrincipal.add(new PermissionVO("project:module_add", "增加子模块"));
        permissionPrincipal.add(new PermissionVO("review:add", "添加审核申请"));
        permissionPrincipal.add(new PermissionVO("daily:add", "添加日报"));
        permissionPrincipal.add(new PermissionVO("project:daily_delete", "删除日报"));

        permissionDeveloper.add(new PermissionVO("auth:change_password", "修改密码"));
        permissionDeveloper.add(new PermissionVO("info:get_header_image", "获取头部图片"));
        permissionDeveloper.add(new PermissionVO("info:edit_header_image", "编辑头部图片"));
        permissionDeveloper.add(new PermissionVO("info:delete_header_image", "删除头部图片"));
        permissionDeveloper.add(new PermissionVO("review:add", "添加审核申请"));
        permissionDeveloper.add(new PermissionVO("daily:add", "添加日报"));
        permissionPrincipal.add(new PermissionVO("project:child_add", "增加子系统"));
        permissionPrincipal.add(new PermissionVO("project:module_add", "增加子模块"));
    }
}
