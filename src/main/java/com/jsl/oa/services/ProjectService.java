package com.jsl.oa.services;

import com.jsl.oa.model.voData.ProjectInfoVO;
import com.jsl.oa.utils.BaseResponse;

public interface ProjectService {
    BaseResponse projectAdd(ProjectInfoVO projectAdd);

    BaseResponse projectEdit(ProjectInfoVO projectEdit);

    BaseResponse projectGetUserInCutting(Long uid);

    BaseResponse projectAddUserForCutting(Long uid, Long pid);
}
