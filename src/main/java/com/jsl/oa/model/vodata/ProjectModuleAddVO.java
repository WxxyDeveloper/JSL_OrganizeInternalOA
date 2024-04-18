package com.jsl.oa.model.vodata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectModuleAddVO {
    @NotNull(message = "子系统id不能为空")
    private Long projectChildId;
    private Long principalId;
    @NotNull(message = "工作量不能为空")
    private Integer workLoad;
    @NotNull(message = "名字不能为空")
    private String name;
    private String description;
    private String status;
    @NotNull(message = "截止时间不能为空")
    private Timestamp deadLine;
}
