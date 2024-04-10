package com.jsl.oa.model.vodata;


import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
public class NewsAddVO {

    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "内容不能为空")
    private String content;
    @NotBlank(message = "标签不能为空")
    private String tags;
    @NotNull(message = "状态不能为空")
    private Short status;

}


