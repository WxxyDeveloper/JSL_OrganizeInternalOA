package com.jsl.oa.mapper;

import com.jsl.oa.model.dodata.MessageDO;
import com.jsl.oa.model.vodata.MessageAddVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface MessageMapper {

    @Update("UPDATE organize_oa.oa_message "
            + "SET `is_delete` = 1, deleted_at = CURRENT_TIMESTAMP "
            + "WHERE id = #{mid}")
    boolean deleteMessage(Long mid);

    @Select("SELECT * FROM organize_oa.oa_message where id = #{mid}")
    MessageDO getMessageById(Long mid);

    @Select("select * from organize_oa.oa_message where uid = #{uid} and is_delete = 0 "
            + "and created_at between #{begin} and #{end}")
    List<MessageDO> page(LocalDateTime begin, LocalDateTime end, Long uid);

    @Insert("insert into organize_oa.oa_message (uid, title, text, sid) "
            + "values(#{uid},#{title},#{text},#{sid})")
    void messageAdd(MessageAddVO messageAddVO);
}
