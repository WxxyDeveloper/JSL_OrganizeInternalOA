package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.PermissionDO;
import com.jsl.oa.model.doData.RolePermissionDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PermissionMapper {

    @Insert("INSERT INTO organize_oa.oa_role_permissions(rid, pid) VALUE (#{rid},#{pid})")
    void permissionAdd(Long rid, Long pid);

    @Select("SELECT * FROM organize_oa.oa_permissions WHERE id IN " +
            "(SELECT pid FROM organize_oa.oa_role_permissions WHERE rid IN " +
                "(SELECT rid FROM organize_oa.oa_role_user WHERE uid = #{uid}))")
    List<PermissionDO> permissionUserPid(Long uid);

    @Select("SELECT * FROM organize_oa.oa_permissions where id=#{id}")
    PermissionDO getPermissionById(Long id);

    @Select("SELECT * FROM organize_oa.oa_role_permissions where pid=#{pid}")
    RolePermissionDO rolePermissionGetByPid(Long pid);

    @Select("SELECT * FROM organize_oa.oa_permissions")
    List<PermissionDO> getAllPermission();

    @Update("UPDATE organize_oa.oa_permissions SET pid = #{pid}, name = #{name}, code = #{code}, " +
            "type = #{type} WHERE id = #{id}")
    boolean updatePermission(PermissionDO permissionDO);

    @Delete("DELETE FROM organize_oa.oa_permissions where id=#{pid}")
    boolean deletePermission(Long pid);

    @Select("SELECT * FROM organize_oa.oa_permissions WHERE id IN (#{permissionList})")
    List<PermissionDO> permissionGet(String permissionList);

    @Select("SELECT * FROM organize_oa.oa_permissions WHERE id = #{pid}")
    PermissionDO getPermissionByPid(Long pid);

    @Select("SELECT * FROM organize_oa.oa_permissions WHERE pid = #{id}")
    List<PermissionDO> getChildPermission(Long id);
}
