package com.jsl.oa.mapper;


import com.jsl.oa.model.doData.NewsDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewsMapper {

    @Select("SELECT * FROM organize_oa.oa_news WHERE id = #{id}")
    NewsDO selectNewsById( Long id);

    @Select("SELECT * FROM organize_oa.oa_news WHERE status = #{status}")
    List<NewsDO> selectNewsByStatus( Integer status);

    @Insert("INSERT INTO organize_oa.oa_news(title, content, tags, status) " +
            "VALUES(#{title}, #{content}, #{tags}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addNews(NewsDO news);

    @Update("UPDATE organize_oa.oa_news SET title = #{title}, content = #{content}, tags = #{tags}, " +
            "likes = #{likes}, comments = #{comments}, status = #{status}, update_at = CURRENT_TIMESTAMP " +
            "WHERE id = #{id}")
    void updateNews(NewsDO news);

    @Delete("DELETE FROM organize_oa.oa_news WHERE id = #{id}")
    void deleteNewsById( Long id);

    @Insert("INSERT INTO organize_oa.oa_news_user(uid,nid)"+
            "VALUES(#{uid}, #{nid})")
    void addNewsUser(Long uid,Long nid);

}
