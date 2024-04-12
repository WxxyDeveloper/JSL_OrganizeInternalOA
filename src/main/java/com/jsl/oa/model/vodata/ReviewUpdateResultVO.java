package com.jsl.oa.model.vodata;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ReviewUpdateResultVO {

    @NotNull
    private Long id;

    @NotNull
    @Min(value = 0, message = "未通过:0,已通过:1,待审批:2")
    @Max(value = 2, message = "未通过:0,已通过:1,待审批:2")
    private Short result;


}


