package com.jsl.oa.mapper;

import com.jsl.oa.common.doData.UserDO;
import com.jsl.oa.common.voData.UserLoginVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM organize_oa.users WHERE username = #{username}")
    UserDO getUserByUsername(String username);

    @Select("SELECT * FROM organize_oa.users WHERE user_num = #{userNum}")
    UserDO getUserByUserNum(String userNum);

    @Insert("INSERT INTO organize_oa.users (user_num, username, password, sex, age, unit, field, hometown, kind, state) " +
            "VALUES " +
            "(#{userNum}, #{username}, #{password}, #{sex}, #{age}, #{unit}, #{filed}, #{hometown}, #{kind}, #{state})")
    Boolean insertUser(UserDO userDO);

    @Select("select id, user_num, username, sex, age, unit, field, hometown, kind, state from organize_oa.users where user_num = #{userNum} ")
    UserDO login(UserLoginVO userLoginVO);

    @Select("select password from organize_oa.users where user_num = #{userNum}")
    String loginPassword(UserLoginVO userLoginVO);
}
