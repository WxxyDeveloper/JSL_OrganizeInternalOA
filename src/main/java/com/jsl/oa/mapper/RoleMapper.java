package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.RoleUserDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper {

    @Insert("insert into organize_oa.oa_role_user (uid, rid) VALUE (#{uid},#{rid})")
    void roleAddUser(Long uid,Long rid);

    @Delete("delete from organize_oa.oa_role_user where uid=#{uid}")
    void roleRemoveUser(Long uid);

    @Select("SELECT * FROM organize_oa.oa_role_user WHERE uid=#{uid}")
    RoleUserDO getRoleByUid(Long uid);
}
