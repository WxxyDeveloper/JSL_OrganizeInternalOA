package com.jsl.oa.config.startup;

import com.google.gson.Gson;
import com.jsl.oa.common.constant.SafeConstants;
import com.jsl.oa.model.dodata.ConfigDO;
import com.jsl.oa.model.vodata.business.InfoAboutSecurityKey;
import com.jsl.oa.utils.Processing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;

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
    private final PrepareData prepareData = new PrepareData(jdbcTemplate);

    @Bean
    @Order(1)
    public CommandLineRunner startUpPreparation() {
        return args -> {
            log.info("============================================================");
            log.info("[Preparation] 系统进行准备检查");
        };
    }

    /**
     * 对数据库进行完整性检查
     * <hr/>
     * 对数据库进行完整性检查，检查数据库是否有数据缺失等信息
     */
    @Bean
    @Order(2)
    public CommandLineRunner sqlDataPreparation() {
        return args -> {
            log.info("[Preparation] 系统进行数据库完整性检查");
            // 检查角色信息是否完整
            prepareData.checkRole("console", "超级管理员");
            prepareData.checkRole("principal", "负责人");
            prepareData.checkRole("developer", "开发者");
        };
    }

    /**
     * 准备安全密钥
     * <hr/>
     * 准备安全密钥，用于加密解密等操作
     */
    @Bean
    @Order(3)
    public CommandLineRunner prepareKey() {
        return args -> {
            log.info("[Preparation] 系统进行安全密钥准备");
            Gson gson = new Gson();
            // 获取数据库中的安全密钥
            ConfigDO getSecurityKey = jdbcTemplate.queryForObject(
                    "SELECT * FROM organize_oa.oa_config WHERE value = ?",
                    ConfigDO.class,
                    "security_key"
            );
            if (getSecurityKey != null) {
                SafeConstants.setSecretKey(getSecurityKey.getData());
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
}
