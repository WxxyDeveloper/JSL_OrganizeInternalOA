package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserDeleteVO;
import com.jsl.oa.model.voData.UserEditProfileVO;
import com.jsl.oa.model.voData.UserLockVO;
import com.jsl.oa.model.voData.UserLoginVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    void userDelete(UserDeleteVO userDeleteVO);

    @Update("UPDATE organize_oa.oa_user SET account_no_locked = 1 ,updated_at = CURRENT_TIMESTAMP WHERE id = #{id}  ")
    void userLock(UserLockVO userLockVO);

    @Select("SELECT * FROM organize_oa.oa_user WHERE id = #{id}")
    UserDO getUserById(Long id);

    @Select("SELECT * FROM organize_oa.oa_user WHERE email = #{email}")
    UserDO getUserInfoByEmail(String email);

    @Select("SELECT * FROM organize_oa.oa_user WHERE phone = #{phone}")
    UserDO getUserInfoByPhone(String user);

    @Select("SELECT * FROM organize_oa.oa_user WHERE job_id = #{jobId}")
    UserDO getUserByJobId(String user);

    void userEditProfile(UserEditProfileVO userEditProfileVO);
}
