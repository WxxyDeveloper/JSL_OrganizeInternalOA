package com.jsl.oa.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsl.oa.common.constant.BusinessConstants;
import com.jsl.oa.mapper.PermissionMapper;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.dodata.PermissionDO;
import com.jsl.oa.model.dodata.RoleDO;
import com.jsl.oa.utils.redis.RoleRedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>权限数据表</h1>
 * <hr/>
 * 内容进入自定义实体类
 *
 * @author xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionDAO {
    private final PermissionMapper permissionMapper;
    private final RoleRedisUtil<String> roleRedisUtil;
    private final Gson gson;
    private final RoleMapper roleMapper;

    /**
     * <h2>获取用户权限信息</h2>
     * <hr/>
     * 通过用户 ID 获取用户权限信息
     *
     * @param uid 用户ID
     * @return {@link List<String>}
     */
    public List<String> getPermission(@NotNull Long uid) {
        // 查询用户所在角色组
        RoleDO getRole = roleMapper.getRoleByUserId(uid);
        if (getRole != null) {
            List<String> getPermissionForString;
            String permissionRedisString = roleRedisUtil.getData(BusinessConstants.NONE, uid.toString());
            if (permissionRedisString == null) {
                String permissionList = permissionMapper.getPermissionByRole(getRole.getRoleName());
                getPermissionForString = gson.fromJson(permissionList, new TypeToken<List<String>>() { }.getType());
                // 存入 Redis
                roleRedisUtil.setData(
                        BusinessConstants.NONE,
                        uid.toString(),
                        gson.toJson(getPermissionForString),
                        1440
                );
            } else {
                getPermissionForString = gson
                        .fromJson(permissionRedisString, new TypeToken<List<String>>() { }.getType());
            }
            return getPermissionForString;
        } else {
            return null;
        }
    }

    public List<String> getAllPermissionBuildString() {
        List<String> getPermission;
        String getRedisData = roleRedisUtil.getData(BusinessConstants.ALL_PERMISSION, "string");
        if (getRedisData == null) {
            getPermission = new ArrayList<>();
            List<PermissionDO> permissionList = permissionMapper.getAllPermission();
            permissionList.forEach(it -> getPermission.add(it.getName()));
            // 存入 Redis
            roleRedisUtil.setData(
                    BusinessConstants.ALL_PERMISSION,
                    "string",
                    gson.toJson(permissionList),
                    1440);
        } else {
            getPermission = gson.fromJson(getRedisData, new TypeToken<List<String>>() { }.getType());
        }
        return getPermission;
    }

    public List<PermissionDO> getAllPermission() {
        return permissionMapper.getAllPermission();
    }
}
