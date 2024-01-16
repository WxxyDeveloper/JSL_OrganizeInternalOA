package com.jsl.oa.dao;

import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.voData.RoleAddUserVO;
import com.jsl.oa.model.voData.RoleRemoveUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDAO {
    private final RoleMapper roleMapper;

    public void roleAddUser(RoleAddUserVO roleAddUserVO) {
        roleMapper.roleAddUser(roleAddUserVO);
    }

    public void roleRemoveUser(RoleRemoveUserVO roleRemoveUserVO) {
        roleMapper.roleRemoveUser(roleRemoveUserVO);
    }
}
