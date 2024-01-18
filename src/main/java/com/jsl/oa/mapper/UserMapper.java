package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.RoleDO;
import com.jsl.oa.model.doData.RoleUserDO;
import com.jsl.oa.model.doData.UserCurrentDO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserAllCurrentVO;
import com.jsl.oa.model.voData.UserEditProfileVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM organize_oa.oa_user WHERE username = #{username}")
    UserDO getUserInfoByUsername(String username);

    @Select("SELECT * FROM organize_oa.oa_user WHERE job_id = #{jobId}")
    UserDO getUserByUserNum(String jobId);

    @Insert("INSERT INTO organize_oa.oa_user " +
            "(job_id, username, password, address, phone, email, age, sex) " +
            "VALUES (#{jobId}, #{username}, #{password}, #{address}, #{phone}, #{email}, #{age}, #{sex})")
    boolean insertUser(UserDO userDO);

    @Update("UPDATE organize_oa.oa_user SET enabled = 0 ,updated_at = CURRENT_TIMESTAMP WHERE id = #{id}  ")
    void userDelete(Long id);

    @Update("UPDATE organize_oa.oa_user SET account_no_locked = 1 ,updated_at = CURRENT_TIMESTAMP WHERE id = #{id}  ")
    void userLock(Long id);

    @Select("SELECT * FROM organize_oa.oa_user WHERE id = #{id}")
    UserDO getUserById(Long id);

    @Select("SELECT * FROM organize_oa.oa_user WHERE email = #{email}")
    UserDO getUserInfoByEmail(String email);

    @Select("SELECT * FROM organize_oa.oa_user WHERE phone = #{phone}")
    UserDO getUserInfoByPhone(String user);

    @Select("SELECT * FROM organize_oa.oa_user WHERE job_id = #{jobId}")
    UserDO getUserByJobId(String user);

    void userEditProfile(UserEditProfileVO userEditProfileVO);

    @Update("UPDATE organize_oa.oa_user SET password = #{newPassword} WHERE id = #{id}")
    boolean updateUserPassword(Long id, String newPassword);

    @Select("SELECT * FROM organize_oa.oa_user ORDER BY `id` DESC LIMIT #{page},#{limit}")
    List<UserCurrentDO> getAllUser(UserAllCurrentVO userAllCurrentVO);

    @Select("SELECT * FROM organize_oa.oa_user " +
            "WHERE username LIKE CONCAT('%',#{search},'%') " +
            "OR email LIKE CONCAT('%',#{search},'%') " +
            "OR phone LIKE CONCAT('%',#{search},'%') " +
            "ORDER BY `id` LIMIT #{page},#{limit}")
    List<UserCurrentDO> getAllUserBySearch(UserAllCurrentVO userAllCurrentVO);

    @Select("SELECT * FROM organize_oa.oa_user WHERE id = #{uid}")
    UserCurrentDO getUserCurrentById(Long uid);

    @Select("SELECT * FROM organize_oa.oa_user WHERE username = #{username}")
    UserCurrentDO getUserCurrentByUsername(String username);

    @Select("SELECT * FROM organize_oa.oa_user WHERE email = #{email}")
    UserCurrentDO getUserCurrentByEmail(String email);

    @Select("SELECT * FROM organize_oa.oa_user WHERE phone = #{phone}")
    UserCurrentDO getUserCurrentByPhone(String phone);


    @Select("SELECT * FROM organize_oa.oa_role_user WHERE uid = #{userId}")
    RoleUserDO getRoleIdByUserId(Long userId);

    @Select("SELECT * FROM organize_oa.oa_role WHERE id = #{roleId}")
    RoleDO getRoleById(Long roleId);



    @Update("UPDATE organize_oa.oa_user " +
            "SET address = #{address}, phone = #{phone}, email = #{email}, age = #{age}, " +
            "signature = #{signature}, sex = #{sex}, avatar = #{avatar}, nickname = #{nickname}, " +
            "description = #{description} " +
            "WHERE id = #{id}")
    void updateUser(UserDO userDO);

}
