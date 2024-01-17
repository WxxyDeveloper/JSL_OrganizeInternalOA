package com.jsl.oa.model.voData.business;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>密钥信息</h1>
 * <hr/>
 * 用于密钥信息的存储
 *
 * @since v1.1.0
 * @version v1.1.0
 */
@Data
@Accessors(chain = true)
public class InfoAboutSecurityKey {
    private String key;
    private Long updateTime;
}
