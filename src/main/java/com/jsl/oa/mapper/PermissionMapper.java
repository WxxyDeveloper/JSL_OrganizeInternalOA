package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.PermissionDO;
import com.jsl.oa.model.doData.RolePermissionDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PermissionMapper {

    @Insert("insert into organize_oa.oa_role_permissions(rid, pid) VALUE (#{rid},#{pid})")
    void permissionAdd(Long rid, Long pid);

    @Select("select name from organize_oa.oa_permissions where id in(select pid " +
            "from organize_oa.oa_role_permissions where rid=" +
            "(select rid from organize_oa.oa_role_user where uid=#{uid}) )")
    List<String> permissionUser(Long uid);

    @Select("SELECT * FROM organize_oa.oa_permissions where id=#{id}")
    PermissionDO permissionGetById(Long id);

    @Select("SELECT * FROM organize_oa.oa_role_permissions where pid=#{pid}")
    RolePermissionDO rolePermissionGetByPid(Long pid);

    @Select("SELECT * FROM organize_oa.oa_permissions")
    List<PermissionDO> getAllPermission();

    @Update("UPDATE organize_oa.oa_permissions SET pid = #{pid}, name = #{name}, code = #{code}, " +
            "type = #{type} WHERE id = #{id}")
    boolean updatePermission(PermissionDO permissionDO);
}
