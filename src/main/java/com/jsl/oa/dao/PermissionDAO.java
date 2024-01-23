package com.jsl.oa.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsl.oa.common.constant.BusinessConstants;
import com.jsl.oa.mapper.PermissionMapper;
import com.jsl.oa.model.doData.PermissionDO;
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

    /**
     * <h2>获取用户权限信息</h2>
     * <hr/>
     * 通过用户 ID 获取用户权限信息
     *
     * @param uid 用户ID
     * @return {@link List<String>}
     */
    public List<String> getPermission(@NotNull Long uid) {
        log.info("\t> 执行 DAO 层 PermissionDAO.getPermission 方法");
        List<String> getPermissionForString;
        String permissionRedisString = roleRedisUtil.getData(BusinessConstants.NONE, uid.toString());
        if (permissionRedisString == null) {
            log.info("\t\t> 从 MySQL 获取数据");
            List<PermissionDO> permissionList = permissionMapper.permissionUserPid(uid);
            getPermissionForString = new ArrayList<>();
            forPermissionToBuildString(permissionList, getPermissionForString);
            // 存入 Redis
            roleRedisUtil.setData(BusinessConstants.NONE, uid.toString(), gson.toJson(getPermissionForString), 1440);
        } else {
            log.info("\t\t> 从 Redis 获取数据");
            getPermissionForString = gson.fromJson(permissionRedisString, new TypeToken<List<String>>() {}.getType());
        }
        return getPermissionForString;
    }

    public List<String> getAllPermissionBuildString() {
        log.info("\t> 执行 DAO 层 PermissionDAO.getPermission 方法");
        List<String> getPermissionForString;
        String getRedisData = roleRedisUtil.getData(BusinessConstants.ALL_PERMISSION, "string");
        if (getRedisData == null) {
            log.info("\t\t> 从 MySQL 获取数据");
            List<PermissionDO> permissionList = permissionMapper.getAllPermission();
            permissionList.removeIf(it -> it.getPid() != null);
            getPermissionForString = new ArrayList<>();
            forPermissionToBuildString(permissionList, getPermissionForString);
            // 存入 Redis
            roleRedisUtil.setData(BusinessConstants.ALL_PERMISSION, "string", gson.toJson(getPermissionForString), 1440);
        } else {
            log.info("\t\t> 从 Redis 获取数据");
            getPermissionForString = gson.fromJson(getRedisData, new TypeToken<List<String>>() {}.getType());
        }
        return getPermissionForString;
    }

    public List<PermissionDO> getRootPermission() {
        log.info("\t> 执行 DAO 层 PermissionDAO.getRootPermission 方法");
        String getRedisData = roleRedisUtil.getData(BusinessConstants.ALL_PERMISSION, "all");
        if (getRedisData == null) {
            log.info("\t\t> 从 MySQL 获取数据");
            List<PermissionDO> permissionList = permissionMapper.getAllPermission();
            if (!permissionList.isEmpty()) {
                List<PermissionDO> getPermissionList = new ArrayList<>();
                for (PermissionDO permission : permissionList) {
                    if (permission.getPid() == null) {
                        getPermissionList.add(permission);
                    }
                }
                roleRedisUtil.setData(BusinessConstants.ALL_PERMISSION, "all", gson.toJson(getPermissionList), 1440);
                return getPermissionList;
            } else {
                return null;
            }
        } else {
            log.info("\t\t> 从 Redis 获取数据");
            return gson.fromJson(getRedisData, new TypeToken<List<PermissionDO>>() {
            }.getType());
        }
    }

    /**
     * <h2>获取全部权限信息</h2>
     * <hr/>
     * 获取全部权限信息
     *
     * @param permissionList         权限信息
     * @param getPermissionForString 存储权限信息
     */
    private void forPermissionToBuildString(@NotNull List<PermissionDO> permissionList, List<String> getPermissionForString) {
        for (PermissionDO permission : permissionList) {
            // 寻找是否存在父亲
            StringBuilder permissionString = new StringBuilder();
            if (permission.getPid() != null) {
                // 存在父亲
                this.getFatherPermission(permissionString, permission.getPid());
            } else {
                // 不存在父亲
                permissionString.append(permission.getName());
            }
            // 寻找子类
            this.getChildPermission(permissionString, permission.getId(), getPermissionForString);
            getPermissionForString.add(permissionString.toString());
        }
    }

    /**
     * <h2>获取子类权限信息</h2>
     * <hr/>
     * 通过父类 ID 获取子类权限信息<br/>
     * 递归调用
     *
     * @param permissionString       父类权限信息
     * @param id                     父类 ID
     * @param getPermissionForString 存储权限信息
     */
    private void getChildPermission(StringBuilder permissionString, Long id, List<String> getPermissionForString) {
        // 获取子类权限信息
        List<PermissionDO> permissionList = permissionMapper.getChildPermission(id);
        // 判断是否存在子类
        if (!permissionList.isEmpty()) {
            // 存在子类
            for (PermissionDO permission : permissionList) {
                StringBuilder childPermissionString = new StringBuilder(permissionString);
                // 遍历数据检查是否依旧存在子类
                List<PermissionDO> childPermissionList = permissionMapper.getChildPermission(permission.getId());
                if (!childPermissionList.isEmpty()) {
                    // 存在子类
                    permissionString.append(".").append(permission.getName());
                    this.getChildPermission(permissionString, permission.getId(), getPermissionForString);
                } else {
                    // 不存在子类
                    permissionString.append(".").append(permission.getName());
                    getPermissionForString.add(permissionString.toString());
                }
                permissionString = childPermissionString;
            }
        }
    }

    /**
     * <h2>获取父类权限信息</h2>
     * <hr/>
     * 通过子类 ID 获取父类权限信息<br/>
     * 递归调用
     *
     * @param permissionString 父类权限信息
     * @param pid              父类 ID
     */
    public void getFatherPermission(StringBuilder permissionString, Long pid) {
        // 获取权限信息
        PermissionDO permissionDO = permissionMapper.getPermissionById(pid);
        // 判断是否存在父亲
        if (permissionDO.getPid() != null) {
            // 存在父亲
            this.getFatherPermission(permissionString, permissionDO.getPid());
        } else {
            // 不存在父亲
            permissionString.append(permissionDO.getCode());
        }
    }
}
