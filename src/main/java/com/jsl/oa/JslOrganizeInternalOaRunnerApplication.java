package com.jsl.oa;

import com.google.gson.Gson;
import com.jsl.oa.common.constant.SafeConstants;
import com.jsl.oa.mapper.InfoMapper;
import com.jsl.oa.model.doData.ConfigDO;
import com.jsl.oa.model.voData.business.InfoAboutSecurityKey;
import com.jsl.oa.utils.Processing;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * <h1>启动类</h1>
 * <hr/>
 * 用于启动项目
 *
 * @version v1.1.0
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @since v1.1.0
 * @author xiaofeng
 */
@Component
@RequiredArgsConstructor
public class JslOrganizeInternalOaRunnerApplication implements SmartInitializingSingleton {
    private final Gson gson = new Gson();
    private final InfoMapper infoMapper;


    /**
     * <h1>获取安全密钥</h1>
     * <hr/>
     * 从数据库中获取安全密钥
     */
    @Override
    public void afterSingletonsInstantiated() {
        // 获取数据库中的 SecurityKey
        try {
            SafeConstants.SECRET_KEY = infoMapper.getSecurityKey().getData();
        } catch (NullPointerException exception) {
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
            infoMapper.insertSecurityKey(configDO);
            SafeConstants.SECRET_KEY = key;
        }
    }
}
