package com.jsl.oa.dao;

import com.jsl.oa.mapper.PermissionMapper;
import com.jsl.oa.model.doData.PermissionDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public List<String> getPermission(Long uid) {
        log.info("\t> 执行 DAO 层 PermissionDAO.getPermission 方法");
        List<PermissionDO> permissionList = permissionMapper.permissionUserPid(uid);
        List<String> getPermissionForString = new ArrayList<>();
        for (PermissionDO permission : permissionList) {
            // 寻找是否存在父亲
            StringBuilder permissionString = new StringBuilder();
            if (permission.getPid() != null) {
                // 存在父亲
                this.getFatherPermission(permissionString, permission.getPid());
                // 寻找子类
                this.getChildPermission(permissionString, permission.getId(), getPermissionForString);
                getPermissionForString.add(permissionString.toString());
            } else {
                // 不存在父亲
                permissionString.append(permission.getName());
                this.getChildPermission(permissionString, permission.getId(), getPermissionForString);
                getPermissionForString.add(permissionString.toString());
            }
        }
        // 存入 Redis

        return getPermissionForString;
    }

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
