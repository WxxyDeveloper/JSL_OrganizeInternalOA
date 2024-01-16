package com.jsl.oa.dao;

import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.voData.RoleAddUser;
import com.jsl.oa.model.voData.RoleRemoveUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDAO {
    private final RoleMapper roleMapper;

    public void roleAddUser(RoleAddUser roleAddUser) {
        roleMapper.roleAddUser(roleAddUser);
    }

    public void roleRemoveUser(RoleRemoveUser roleRemoveUser) {
        roleMapper.roleRemoveUser(roleRemoveUser);
    }
}
