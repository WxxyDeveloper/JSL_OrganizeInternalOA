package com.jsl.oa.mapper;

import com.jsl.oa.model.doData.ConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <h1>信息映射</h1>
 * <hr/>
 * 用于信息的映射，映射数据库 oa_config 表
 *
 * @since v1.1.0
 * @version v1.1.0
 * @see com.jsl.oa.model.doData.ConfigDO
 */
@Mapper
public interface InfoMapper {
    /**
     * <h2>获取密钥</h2>
     * <hr/>
     * 用于获取密钥
     *
     * @return {@link ConfigDO}
     */
    @Select("SELECT * FROM organize_oa.oa_config WHERE value= 'security_key'")
    ConfigDO getSecurityKey();

    /**
     * <h2>更新密钥</h2>
     * <hr/>
     * 用于更新密钥
     *
     * @param configDO {@link ConfigDO}
     */
    @Update("UPDATE organize_oa.oa_config SET data = #{data} WHERE value = 'security_key'")
    void updateSecurityKey(ConfigDO configDO);

    /**
     * <h2>插入密钥</h2>
     * <hr/>
     * 用于插入密钥
     *
     * @param configDO {@link ConfigDO}
     */
    @Update("INSERT INTO organize_oa.oa_config (value, data, created_at) VALUES (#{value}, #{data}, #{createdAt})")
    void insertSecurityKey(ConfigDO configDO);
}
