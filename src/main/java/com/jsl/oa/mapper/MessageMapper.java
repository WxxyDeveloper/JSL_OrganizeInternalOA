package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.MessageDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface MessageMapper {

    @Update("UPDATE organize_oa.oa_message " +
            "SET `read` = 1, deleted_at = CURRENT_TIMESTAMP " +
            "WHERE id = #{mid}")
    boolean deleteMessage(Long mid);

    @Select("SELECT * FROM organize_oa.oa_message where id = #{mid}")
    MessageDO getMessageById(Long mid);
}
