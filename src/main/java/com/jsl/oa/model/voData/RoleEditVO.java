package com.jsl.oa.model.voData;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <h1>角色编辑VO</h1>
 * <hr/>
 * 角色编辑VO，用于接收角色编辑请求
 *
 * @version v1.1.0
 * @author 筱锋xiao_lfeng
 * @since v1.1.0
 */
@Data
public class RoleEditVO {
    private Long id;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "角色名只能为3-16位的字母、数字、下划线组成")
    private String name;
    @NotBlank
    private String displayName;
}
