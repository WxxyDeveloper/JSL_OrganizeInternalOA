package com.jsl.oa.mapper;

import com.jsl.oa.model.voData.RoleAddUser;
import com.jsl.oa.model.voData.RoleRemoveUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper {

    @Insert("insert into organize_oa.oa_role_user (uid, rid) VALUE (#{uid},#{rid})")
    void roleAddUser(RoleAddUser roleAddUser);

    @Delete("delete from organize_oa.oa_role_user where uid=#{uid}")
    void roleRemoveUser(RoleRemoveUser roleRemoveUser);
}
