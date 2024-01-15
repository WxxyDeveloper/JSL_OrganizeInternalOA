package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserDeleteVO;
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
            "(job_id, username, password, address, phone, email, age, signature, avatar, nickname, account_no_locked, description, updated_at) " +
            "VALUES (#{jobId}, #{username}, #{password}, #{address}, #{phone}, #{email}, #{age}, #{signature}, #{avatar}, #{nickname}, #{accountNoLocked}, #{description}, #{updatedAt})")
    boolean insertUser(UserDO userDO);

    @Select("SELECT password FROM organize_oa.oa_user WHERE job_id = #{jobId}")
    String loginPassword(UserLoginVO userLoginVO);

    @Select("SELECT * FROM organize_oa.oa_user WHERE job_id = #{jobId}")
    UserDO login(UserLoginVO userLoginVO);

    @Update("update organize_oa.oa_user set enabled = 0 where id = #{id}  ")
    void userDelete(UserDeleteVO userDeleteVO);

    @Update("update organize_oa.oa_user set account_no_locked = 1 where id = #{id}  ")
    void userLock(UserLockVO userLockVO);

    @Select("select * from organize_oa.oa_user where id = #{id}")
    UserDO getUserById(Long id);
}
