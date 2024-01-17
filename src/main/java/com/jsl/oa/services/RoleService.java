package com.jsl.oa.services;

import com.jsl.oa.model.voData.RoleAddUserVO;
import com.jsl.oa.model.voData.RoleRemoveUserVO;
import com.jsl.oa.utils.BaseResponse;

public interface RoleService {
    BaseResponse roleAddUser(Long uid,Long rid);

    BaseResponse roleRemoveUser(Long uid);
}
