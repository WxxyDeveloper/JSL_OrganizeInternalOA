package com.jsl.oa.config.startup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsl.oa.common.constant.SafeConstants;
import com.jsl.oa.model.dodata.ConfigDO;
import com.jsl.oa.model.vodata.business.InfoAboutSecurityKey;
import com.jsl.oa.utils.Processing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * 系统启动时进行的一些初始化操作
 * <hr/>
 * 1. 检查数据库完整性
 * 2. 检查系统配置
 * 3. 检查系统权限
 * 4. 检查系统数据
 *
 * @author xiao_lfeng
 * @version v1.2.0
 * @since v1.2.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class StartupConfiguration {
    private final JdbcTemplate jdbcTemplate;
    private final PermissionList getPermission = new PermissionList();
    private PrepareData prepareData;

    @Bean
    @Order(1)
    public CommandLineRunner startUpPreparation() {
        return args -> {
            log.info(">===========================================================");
            log.info("[Preparation] 系统进行准备检查");
            prepareData = new PrepareData(jdbcTemplate);
        };
    }

    /**
     * 对数据库进行完整性检查
     * <hr/>
     * 对数据库进行完整性检查，检查数据库是否出现缺失数据表的情况，若出现缺失数据表的情况将会对数据表进行创建，若数据保持完整将不进行任何操作
     */
    @Bean
    @Order(2)
    public CommandLineRunner checkDatabaseExist() {
        return args -> {
            log.info("[Preparation] 系统进行数据库完整性检查");
            // 数据表的检查
            prepareData.checkDatabase("oa_config");
            prepareData.checkDatabase("oa_user");
            prepareData.checkDatabase("oa_role");
            prepareData.checkDatabase("oa_permissions");
            prepareData.checkDatabase("oa_news");
            prepareData.checkDatabase("oa_project_tags");
            prepareData.checkDatabase("oa_project");
            prepareData.checkDatabase("oa_project_child");
            prepareData.checkDatabase("oa_project_modules");
            prepareData.checkDatabase("oa_review");
            prepareData.checkDatabase("oa_message");
            prepareData.checkDatabase("oa_news_user");
            prepareData.checkDatabase("oa_project_daily");
            prepareData.checkDatabase("oa_role_user");
            prepareData.checkDatabase("oa_user_tags");
        };
    }

    @Bean
    @Order(3)
    public CommandLineRunner permissionDataPreparation() {
        return args -> {
            log.info("[Preparation] 系统进行权限表完整性检查");
            getPermission.getPermissionList().forEach(permissionVO -> {
                try {
                    jdbcTemplate.queryForObject(
                            "SELECT id FROM organize_oa.oa_permissions WHERE name = ?",
                            Long.class,
                            permissionVO.getName()
                    );
                } catch (DataAccessException e) {
                    log.debug("[Preparation] 缺失 {} 权限，正在创建", permissionVO.getName());
                    jdbcTemplate.update(
                            "INSERT INTO organize_oa.oa_permissions (name, description) VALUES (?,?)",
                            permissionVO.getName(),
                            permissionVO.getDesc()
                    );
                }
            });
        };
    }

    /**
     * 对数据表进行完整性检查
     * <hr/>
     * 对数据表进行完整性检查，检查数据表是否有数据缺失等信息
     */
    @Bean
    @Order(4)
    public CommandLineRunner roleDataPreparation() {
        return args -> {
            log.info("[Preparation] 系统进行角色表完整性检查");
            // 检查角色信息是否完整
            prepareData.checkRole("console", "超级管理员");
            prepareData.checkRole("principal", "负责人");
            prepareData.checkRole("developer", "开发者");

            // 对权限的检查
            prepareData.checkPermission("console", getPermission.getPermissionList());
            prepareData.checkPermission("principal", getPermission.getPermissionPrincipal());
            prepareData.checkPermission("developer", getPermission.getPermissionDeveloper());
        };
    }

    /**
     * 检查默认管理员账户
     * <hr/>
     * 为程序进行检查默认管理员程序，检查默认管理员是否存在，若管理员存在且权限正确则检查通过，若检查失败或该用户不存在将会创建一个超级管理员
     * 账户。
     */
    @Bean
    @Order(5)
    public CommandLineRunner defaultConsoleDataPreparation() {
        return args -> {
            log.info("[Preparation] 系统进行默认超级管理员信息检查");
            // 检查默认的信息是否完整
            Long getDefaultUser;
            try {
                getDefaultUser = jdbcTemplate.queryForObject(
                        "SELECT id FROM organize_oa.oa_user WHERE job_id = 'OTH0000001'",
                        Long.class);
            } catch (DataAccessException e) {
                log.debug("[Preparation] 超级管理员不存在，创建 console_user 超级管理员账户，密码 jsl_nbxt");
                jdbcTemplate.update(
                        "INSERT INTO organize_oa.oa_user "
                                + "(job_id, username, password, address, phone, email, age) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                        "OTH0000001",
                        "console_user",
                        BCrypt.hashpw("jsl_nbxt", BCrypt.gensalt()),
                        "江苏省/无锡市",
                        "18888888888",
                        "console@jsl.com",
                        "18"
                );
                getDefaultUser = jdbcTemplate.queryForObject(
                        "SELECT id FROM organize_oa.oa_user WHERE job_id = 'OTH0000001'",
                        Long.class);
            }
            // 检查超级管理员权限是否正确
            Long getConsoleRole = jdbcTemplate
                    .queryForObject("SELECT id FROM organize_oa.oa_role WHERE role_name='console' LIMIT 1", Long.class);
            assert getConsoleRole != null;
            Long getConsoleRoleUser;
            try {
                getConsoleRoleUser = jdbcTemplate.queryForObject(
                        "SELECT rid FROM organize_oa.oa_role_user WHERE uid=?",
                        Long.class,
                        getDefaultUser
                );
                if (!getConsoleRole.equals(getConsoleRoleUser)) {
                    log.debug("[Preparation] 超级管理员账户权限错误，正在恢复权限至 console 角色组");
                    jdbcTemplate.update(
                            "UPDATE organize_oa.oa_role_user SET rid=? WHERE uid=?",
                            getConsoleRole,
                            getDefaultUser
                    );
                }
            } catch (DataAccessException e) {
                log.debug("[Preparation] 为超级管理员账户添加 console 角色组权限");
                // 插入超级管理员权限
                jdbcTemplate.update(
                        "INSERT INTO organize_oa.oa_role_user (uid, rid) VALUES (?,?)",
                        getDefaultUser,
                        getConsoleRole
                );
            }
        };
    }

    @Bean
    @Order(6)
    public CommandLineRunner prepareDefaultConfigData(Gson gson) {
        return args -> {
            // 检查加密密钥是否存在
            try {
                jdbcTemplate
                        .queryForObject("SELECT id FROM organize_oa.oa_config WHERE value='security_key'", Long.class);
            } catch (DataAccessException e) {
                HashMap<String, String> insertData = new HashMap<>();
                insertData.put("key", Processing.generateKey(1233061823L));
                insertData.put("updateTime", String.valueOf(new Date().getTime()));
                jdbcTemplate.update(
                        "INSERT INTO organize_oa.oa_config (value, data) VALUES (?,?)",
                        "security_key",
                        gson.toJson(insertData)
                );
            }
            // 检查 carousel
            try {
                jdbcTemplate.queryForObject("SELECT id FROM organize_oa.oa_config WHERE value='carousel'", Long.class);
            } catch (DataAccessException e) {
                jdbcTemplate.update(
                        "INSERT INTO organize_oa.oa_config (value, data) VALUES (?,?)",
                        "carousel",
                        gson.toJson("")
                );
            }
            // 检查 project_show
            try {
                jdbcTemplate
                        .queryForObject("SELECT id FROM organize_oa.oa_config WHERE value='project_show'", Long.class);
            } catch (DataAccessException e) {
                jdbcTemplate.update(
                        "INSERT INTO organize_oa.oa_config (value, data) VALUES (?,?)",
                        "project_show",
                        gson.toJson("")
                );
            }
        };
    }

    /**
     * 准备安全密钥
     * <hr/>
     * 准备安全密钥，用于加密解密等操作
     */
    @Bean
    @Order(7)
    public CommandLineRunner prepareKey() {
        return args -> {
            log.info("[Preparation] 系统进行安全密钥准备");
            Gson gson = new Gson();
            // 获取数据库中的安全密钥
            String getSecurityKey = jdbcTemplate.queryForObject(
                    "SELECT data FROM organize_oa.oa_config WHERE value = 'security_key' LIMIT 1",
                    String.class);
            if (getSecurityKey != null) {
                HashMap<String, String> getData = gson.fromJson(
                        getSecurityKey,
                        new TypeToken<HashMap<String, String>>() {
                        }.getType());
                SafeConstants.setSecretKey(getData.get("key"));
            } else {
                // 生成密钥
                String key = Processing.generateKey(System.currentTimeMillis());
                InfoAboutSecurityKey infoAboutSecurityKey = new InfoAboutSecurityKey();
                infoAboutSecurityKey.setKey(key)
                        .setUpdateTime(System.currentTimeMillis());
                String json = gson.toJson(infoAboutSecurityKey, InfoAboutSecurityKey.class);
                // 更新密钥
                ConfigDO configDO = new ConfigDO();
                configDO.setValue("security_key")
                        .setData(json)
                        .setCreatedAt(new Timestamp(System.currentTimeMillis()));
                // 初始化密钥
                jdbcTemplate.update("INSERT INTO organize_oa.oa_config (value, data) VALUES (?, ?)",
                        configDO.getValue(),
                        configDO.getData()
                );
                SafeConstants.setSecretKey(key);
            }
        };
    }

    @Bean
    @Order(100)
    public CommandLineRunner preparationCompleted() {
        return args -> {
            log.info("[Preparation] 系统准备检查完毕");
            log.info("===========================================================>");
        };
    }
}
