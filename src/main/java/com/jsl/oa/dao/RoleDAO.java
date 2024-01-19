package com.jsl.oa.dao;

import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.RoleDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleDAO {
    public final RoleMapper roleMapper;

    public void roleAddUser(Long uid,Long rid) {
        roleMapper.roleAddUser(uid,rid);
    }

    public void roleRemoveUser(Long uid) {
        roleMapper.roleRemoveUser(uid);
    }

    public List<RoleDO> getRolesById(String id) {
        ArrayList<RoleDO> getRoleList = new ArrayList<>();
        getRoleList.add(roleMapper.getRoleById(Long.valueOf(id)));
        return getRoleList;
    }

    public RoleDO getRoleById(Long id) {
        return roleMapper.getRoleById(id);
    }

    public List<RoleDO> getRole() {
        return roleMapper.getRole();
    }

    public void roleAdd(RoleDO roleDO) {  roleMapper.roleAdd(roleDO);}

    public boolean roleEdit(RoleDO getRole) {
        return roleMapper.roleEdit(getRole);
    }

    public boolean roleDelete(Long id) {
        return roleMapper.roleDelete(id);
    }

    public boolean isExistRoleByRoleName(String roleName){
        RoleDO roleDO = roleMapper.getRoleByRoleName(roleName);
        return roleDO != null;
    }

    public boolean roleChangeUser(Long uid, Long rid) {
       return roleMapper.roleChangeUser(uid,rid);
    }
}
