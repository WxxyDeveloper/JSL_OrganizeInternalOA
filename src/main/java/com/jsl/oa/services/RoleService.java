package com.jsl.oa.services;

import com.jsl.oa.model.voData.RoleAddUserVO;
import com.jsl.oa.model.voData.RoleRemoveUserVO;
import com.jsl.oa.utils.BaseResponse;

public interface RoleService {
    BaseResponse roleAddUser(RoleAddUserVO roleAddUserVO);

    BaseResponse roleRemoveUser(RoleRemoveUserVO roleRemoveUserVO);
}
