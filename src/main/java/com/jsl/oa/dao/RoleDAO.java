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
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleDAO {
    public final RoleMapper roleMapper;
    private final Gson gson;
    private final RoleRedisUtil<String> roleRedisUtil;

    public void addRoleUser(Long uid, Long rid) {
        log.info("\t> 执行 DAO 层 RoleDAO.addRoleUser 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        roleMapper.roleAddUser(uid, rid);
        roleRedisUtil.setData(BusinessConstants.USER, uid.toString(), gson.toJson(roleMapper.getRoleUserByUid(uid)), 120);
    }

    public void delRoleUser(Long uid) {
        log.info("\t> 执行 DAO 层 RoleDAO.delRoleUser 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        roleMapper.roleRemoveUser(uid);
        roleRedisUtil.delData(BusinessConstants.USER, uid.toString());
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
        roleRedisUtil.setData(BusinessConstants.NONE, "all", gson.toJson(roleList), 120);

    }

    public RoleDO getRoleNameByUid(Long uid){
        log.info("\t> 执行 DAO 层 RoleDAO.getRoleNameByUid 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        RoleDO roleDO = roleMapper.getRoleById(getRoleUserByUid(uid).getRid());
        return roleDO;
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

    public boolean roleChangeUser(@NotNull Long uid, Long rid) {
        log.info("\t> 执行 DAO 层 RoleDAO.roleChangeUser 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        if (roleMapper.roleChangeUser(uid, rid)) {
            roleRedisUtil.setData(BusinessConstants.USER, uid.toString(), gson.toJson(roleMapper.getRoleUserByUid(uid)), 120);
            return true;
        } else {
            return false;
        }
    }

    public RoleUserDO getRoleUserByUid(@NotNull Long uid) {
        log.info("\t> 执行 DAO 层 RoleDAO.getRoleUserByUid 方法");
        String getRedisData = roleRedisUtil.getData(BusinessConstants.USER, uid.toString());
        if (getRedisData == null) {
            log.info("\t\t> 从 MySQL 获取数据");
            return roleMapper.getRoleUserByUid(uid);
        } else {
            log.info("\t\t> 从 Redis 获取数据");
            return gson.fromJson(getRedisData, RoleUserDO.class);
        }
    }
}
