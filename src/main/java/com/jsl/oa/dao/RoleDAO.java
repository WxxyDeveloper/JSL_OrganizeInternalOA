package com.jsl.oa.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsl.oa.common.constant.BusinessConstants;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.dodata.RoleDO;
import com.jsl.oa.model.dodata.RoleUserDO;
import com.jsl.oa.utils.redis.RoleRedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleDAO {
    private final RoleMapper roleMapper;
    private final Gson gson;
    private final RoleRedisUtil<String> roleRedisUtil;

    public void addRoleUser(Long uid, Long rid) {
        roleMapper.roleAddUser(uid, rid);
        roleRedisUtil.setData(
                BusinessConstants.USER,
                uid.toString(),
                gson.toJson(roleMapper.getRoleUserByUid(uid)),
                120
        );
    }

    public void delRoleUser(Long uid) {
        roleMapper.roleRemoveUser(uid);
        roleRedisUtil.delData(BusinessConstants.USER, uid.toString());
    }

    public RoleDO getRoleById(Long id) {
        String getRedisData = roleRedisUtil.getData(BusinessConstants.NONE, "all");
        if (getRedisData == null) {
            return roleMapper.getRoleById(id);
        } else {
            List<RoleDO> roleList = gson.fromJson(getRedisData, new TypeToken<List<RoleDO>>() {
            }.getType());
            for (RoleDO roleDO : roleList) {
                if (roleDO.getId().equals(id)) {
                    return roleDO;
                }
            }
            return null;
        }
    }

    public List<RoleDO> getRole() {
        String getRedisData = roleRedisUtil.getData(BusinessConstants.NONE, "all");
        if (getRedisData == null) {
            List<RoleDO> roleList = roleMapper.getRole();
            roleRedisUtil.setData(BusinessConstants.NONE, "all", gson.toJson(roleList), 1440);
            return roleList;
        } else {
            return gson.fromJson(getRedisData, new TypeToken<List<RoleDO>>() {
            }.getType());
        }
    }

    public void roleAdd(RoleDO roleDO) {
        roleMapper.roleAdd(roleDO);
        List<RoleDO> roleList = roleMapper.getRole();
        roleRedisUtil.setData(BusinessConstants.NONE, "all", gson.toJson(roleList), 120);

    }

    public RoleDO getRoleNameByUid(Long uid) {
        return roleMapper.getRoleById(getRoleUserByUid(uid).getRid());
    }


    public boolean roleEdit(RoleDO getRole) {
        if (roleMapper.roleEdit(getRole)) {
            List<RoleDO> roleList = roleMapper.getRole();
            roleRedisUtil.setData(BusinessConstants.NONE, "all", gson.toJson(roleList), 1440);
            return true;
        } else {
            return false;
        }
    }

    public boolean roleDelete(Long id) {
        if (roleMapper.roleDelete(id)) {
            List<RoleDO> roleList = roleMapper.getRole();
            roleRedisUtil.setData(BusinessConstants.NONE, "all", gson.toJson(roleList), 1440);
            return true;
        } else {
            return false;
        }
    }

    public boolean isExistRoleByRoleName(String roleName) {
        RoleDO roleDO = roleMapper.getRoleByRoleName(roleName);
        return roleDO != null;
    }

    public boolean roleChangeUser(@NotNull Long uid, Long rid) {
        if (roleMapper.roleChangeUser(uid, rid)) {
            roleRedisUtil.setData(
                    BusinessConstants.USER,
                    uid.toString(),
                    gson.toJson(roleMapper.getRoleUserByUid(uid)),
                    120
            );
            return true;
        } else {
            return false;
        }
    }

    public RoleUserDO getRoleUserByUid(@NotNull Long uid) {
        String getRedisData = roleRedisUtil.getData(BusinessConstants.USER, uid.toString());
        if (getRedisData == null) {
            return roleMapper.getRoleUserByUid(uid);
        } else {
            return gson.fromJson(getRedisData, RoleUserDO.class);
        }
    }

    /**
     * 根据角色名获取角色信息
     *
     * @param roleName 角色名
     * @return 角色信息
     */
    public RoleDO getRoleByRoleName(String roleName) {
        return roleMapper.getRoleByRoleName(roleName);
    }
}
