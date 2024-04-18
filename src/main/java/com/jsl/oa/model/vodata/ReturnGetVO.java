package com.jsl.oa.model.vodata;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

/**
 * 模糊查询返回，系统/项目/模块 名字和id

 */
public class ReturnGetVO {
    private String name;
    private Long id;
}
