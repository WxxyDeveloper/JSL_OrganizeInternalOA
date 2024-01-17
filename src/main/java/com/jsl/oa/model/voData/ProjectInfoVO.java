package com.jsl.oa.model.voData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProjectInfoVO {
    @NotNull(message = "id不能为空")
    private Long id;

    @NotBlank(message = "项目名不为空")
    private String name;

    @NotBlank(message = "简介不能为空")
    private String description;

    @NotBlank(message = "描述不能为空")
    private String introduction;

    //@NotNull(message = "填写是否开放")
    private Short codeOpen;

    private String coreCode;

    private String git;

    //@NotNull(message = "难度等级不能为空")
    private Short difficultyLevel;

    @NotNull(message = "项目类型不能为空")
    private Integer type;

    private Long reward;

    //@NotNull(message = "项目状态不能为空")
    private Short status;
}
