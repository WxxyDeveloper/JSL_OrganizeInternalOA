package com.jsl.oa.config.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class PrepareData {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 检查角色
     * <hr/>
     * 检查检查指定的角色是否存在，如果不存在则创建
     */
    public void checkRole(String roleName, String displayName) {
        try {
            jdbcTemplate.queryForObject(
                    "SELECT id FROM organize_oa.oa_role WHERE role_name = ? LIMIT 1",
                    Long.class,
                    roleName
            );
        } catch (DataAccessException e) {
            // 创建角色
            log.debug("[Preparation] 创建角色 [{}] {}", roleName, displayName);
            jdbcTemplate.update(
                    "INSERT INTO organize_oa.oa_role (role_name, display_name) VALUES (?,?)",
                    roleName,
                    displayName
            );
        }
    }
}
