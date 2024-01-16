package com.jsl.oa.services;

import com.jsl.oa.model.voData.RoleAddUser;
import com.jsl.oa.model.voData.RoleRemoveUser;
import com.jsl.oa.utils.BaseResponse;

public interface RoleService {
    BaseResponse roleAddUser(RoleAddUser roleAddUser);

    BaseResponse roleRemoveUser(RoleRemoveUser roleRemoveUser);
}
