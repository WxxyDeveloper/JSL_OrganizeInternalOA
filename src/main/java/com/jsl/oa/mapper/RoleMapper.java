package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.RoleDO;
import com.jsl.oa.model.doData.RoleUserDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Insert("insert into organize_oa.oa_role_user (uid, rid) VALUE (#{uid},#{rid})")
    void roleAddUser(Long uid, Long rid);

    @Select("INSERT INTO organize_oa.oa_role (role_name,display_name) VALUES (#{roleName}, ${display_name})")
    void roleAdd(RoleDO roleDO);

    @Delete("delete from organize_oa.oa_role_user where uid=#{uid}")
    void roleRemoveUser(Long uid);

    @Select("SELECT * FROM organize_oa.oa_role_user WHERE uid=#{uid}")
    RoleUserDO getRoleUserByUid(Long uid);

    @Select("SELECT * FROM organize_oa.oa_role WHERE role_name=#{roleName}")
    RoleDO getRoleByRoleName(String roleName);

    @Select("SELECT * FROM organize_oa.oa_role WHERE id=#{id}")
    RoleDO getRoleById(Long id);

    @Select("SELECT * FROM organize_oa.oa_role ORDER BY id DESC")
    List<RoleDO> getRole();

    @Update("UPDATE organize_oa.oa_role SET role_name=#{roleName},display_name=#{displayName},updated_at=CURRENT_TIMESTAMP WHERE id=#{id}")
    boolean roleEdit(RoleDO getRole);

    @Update("UPDATE organize_oa.oa_role_user SET rid = #{rid},updated_at = current_timestamp WHERE uid = #{uid}")
    boolean roleChangeUser(Long uid, Long rid);

    @Delete("DELETE FROM organize_oa.oa_role WHERE id=#{id}")
    boolean roleDelete(Long id);





}
