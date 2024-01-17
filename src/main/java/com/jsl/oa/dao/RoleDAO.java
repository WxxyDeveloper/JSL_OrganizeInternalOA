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

    public void roleAddUser(Long uid,Long rid) {
        roleMapper.roleAddUser(uid,rid);
    }

    public void roleRemoveUser(Long uid) {
        roleMapper.roleRemoveUser(uid);
    }
}
