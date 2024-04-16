package com.jsl.oa.config.startup;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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

    /**
     * 检查数据库是否完整
     * <hr/>
     * 检查数据库是否完整，若数据库保持完整则不进行任何操作，若数据库不完整将会创建对应的数据表
     * @param tableName 数据表名字
     */
    public void checkDatabase(String tableName) {
        try {
            jdbcTemplate.queryForObject(
                    "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_NAME = ?",
                    String.class,
                    tableName
            );
        } catch (DataAccessException e) {
            log.debug("[Preparation] 创建数据表 {}", tableName);
            // 读取文件
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            // 读取 resources/mysql 目录下的所有 SQL 文件
            Resource resource = resolver.getResource("classpath:/mysql/" + tableName + ".sql");
            // 创建数据表
            try {
                String sql = FileCopyUtils
                        .copyToString(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
                // 分割 SQL 语句并执行
                jdbcTemplate.execute("USE organize_oa");
                String[] sqlStatements = sql.split(";");
                for (String statement : sqlStatements) {
                    if (!statement.trim().isEmpty()) {
                        jdbcTemplate.execute(statement.trim());
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void checkPermission(String roleName, ArrayList<PermissionList.PermissionVO> permissions) {
        ArrayList<String> newPermissions = new ArrayList<>();
        permissions.forEach(it -> newPermissions.add(it.getName()));
        Gson gson = new Gson();
        String getPermissionString = gson.toJson(newPermissions);
        log.debug("[Preparation] 更新角色 {} 权限", roleName);
        jdbcTemplate.update(
                "UPDATE organize_oa.oa_role SET permissions = ? WHERE role_name = ?",
                getPermissionString,
                roleName
        );
    }
}
