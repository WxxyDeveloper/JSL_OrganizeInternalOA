package com.jsl.oa.mapper;

import com.jsl.oa.model.dodata.MessageDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;


@Mapper
public interface MessageMapper {

    @Update("UPDATE organize_oa.oa_message "
            + "SET `read` = 1, deleted_at = CURRENT_TIMESTAMP "
            + "WHERE id = #{mid}")
    boolean deleteMessage(Long mid);

    @Select("SELECT * FROM organize_oa.oa_message where id = #{mid}")
    MessageDO getMessageById(Long mid);

    @Select("select count(*) from organize_oa.oa_message where uid = #{uid}")
    Long count(Long uid);

    @Select("select * from organize_oa.oa_message where uid = #{uid}")
        //and created_at between #{begin} and #{end} limit #{start},#{pageSize}")
    List<MessageDO> page(LocalDate begin, LocalDate end, Long uid, Long start, Long pageSize);
}
