package com.jsl.oa.mapper;

import com.jsl.oa.common.doData.UserDO;
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
            "(#{userNum}, #{username}, #{password}, #{sex}, #{age}, #{unit}, #{filed}, #{hometown}, #{kind}, #{status})")
    Boolean insertUser(UserDO userDO);
}
