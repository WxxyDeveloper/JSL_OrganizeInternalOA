package com.jsl.oa.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsl.oa.common.constant.BusinessConstants;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.RoleDO;
import com.jsl.oa.model.doData.RoleUserDO;
import com.jsl.oa.utils.redis.RoleRedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleDAO {
    public final RoleMapper roleMapper;
    private final Gson gson;
    private final RoleRedisUtil<String> roleRedisUtil;

    public void roleAddUser(Long uid, Long rid) {
        log.info("\t> 执行 DAO 层 RoleDAO.roleAddUser 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        roleMapper.roleAddUser(uid, rid);
    }

    public void roleRemoveUser(Long uid) {
        log.info("\t> 执行 DAO 层 RoleDAO.roleRemoveUser 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        roleMapper.roleRemoveUser(uid);
    }

    public List<RoleDO> getRolesById(String id) {
        log.info("\t> 执行 DAO 层 RoleDAO.getRolesById 方法");
        ArrayList<RoleDO> getRoleList = new ArrayList<>();
        log.info("\t\t> 从 MySQL 获取数据");
        getRoleList.add(roleMapper.getRoleById(Long.valueOf(id)));
        return getRoleList;
    }

    public RoleDO getRoleById(Long id) {
        log.info("\t> 执行 DAO 层 RoleDAO.getRoleById 方法");
        String getRedisData = roleRedisUtil.getData(BusinessConstants.NONE, "all");
        if (getRedisData == null) {
            log.info("\t\t> 从 MySQL 获取数据");
            return roleMapper.getRoleById(id);
        } else {
            log.info("\t\t> 从 Redis 获取数据");
            List<RoleDO> roleList = gson.fromJson(getRedisData, new TypeToken<List<RoleDO>>() {}.getType());
            for (RoleDO roleDO : roleList) {
                if (roleDO.getId().equals(id)) {
                    return roleDO;
                }
            }
            return null;
        }
    }

    public List<RoleDO> getRole() {
        log.info("\t> 执行 DAO 层 RoleDAO.getRole 方法");
        String getRedisData = roleRedisUtil.getData(BusinessConstants.NONE, "all");
        if (getRedisData == null) {
            log.info("\t\t> 从 MySQL 获取数据");
            List<RoleDO> roleList = roleMapper.getRole();
            roleRedisUtil.setData(BusinessConstants.NONE, "all", gson.toJson(roleList), 1440);
            return roleList;
        } else {
            log.info("\t\t> 从 Redis 获取数据");
            return gson.fromJson(getRedisData, new TypeToken<List<RoleDO>>() {}.getType());
        }
    }

    public void roleAdd(RoleDO roleDO) {
        log.info("\t> 执行 DAO 层 RoleDAO.roleAdd 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        roleMapper.roleAdd(roleDO);
        List<RoleDO> roleList = roleMapper.getRole();
        roleRedisUtil.setData(BusinessConstants.NONE, "all", gson.toJson(roleList), 1440);

    }

    public boolean roleEdit(RoleDO getRole) {
        log.info("\t> 执行 DAO 层 RoleDAO.roleEdit 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        if (roleMapper.roleEdit(getRole)) {
            List<RoleDO> roleList = roleMapper.getRole();
            roleRedisUtil.setData(BusinessConstants.NONE, "all", gson.toJson(roleList), 1440);
            return true;
        } else {
            return false;
        }
    }

    public boolean roleDelete(Long id) {
        log.info("\t> 执行 DAO 层 RoleDAO.roleDelete 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        if (roleMapper.roleDelete(id)) {
            List<RoleDO> roleList = roleMapper.getRole();
            roleRedisUtil.setData(BusinessConstants.NONE, "all", gson.toJson(roleList), 1440);
            return true;
        } else {
            return false;
        }
    }

    public boolean isExistRoleByRoleName(String roleName) {
        log.info("\t> 执行 DAO 层 RoleDAO.isExistRoleByRoleName 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        RoleDO roleDO = roleMapper.getRoleByRoleName(roleName);
        return roleDO != null;
    }

    public boolean roleChangeUser(Long uid, Long rid) {
        log.info("\t> 执行 DAO 层 RoleDAO.roleChangeUser 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return roleMapper.roleChangeUser(uid, rid);
    }

    public RoleUserDO getRoleUserByUid(Long uid) {
        log.info("\t> 执行 DAO 层 RoleDAO.getRoleUserByUid 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        return roleMapper.getRoleUserByUid(uid);
    }
}
