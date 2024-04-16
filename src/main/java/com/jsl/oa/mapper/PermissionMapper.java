package com.jsl.oa.mapper;

import com.jsl.oa.model.dodata.PermissionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper {

    @Select("SELECT * FROM organize_oa.oa_permissions")
    List<PermissionDO> getAllPermission();

    @Select("SELECT permissions FROM organize_oa.oa_role WHERE role_name = #{roleName}")
    String getPermissionByRole(String roleName);
}
