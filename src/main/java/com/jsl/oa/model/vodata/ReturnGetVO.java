package com.jsl.oa.model.vodata;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor


/**
 * 模糊查询返回，系统/项目/模块 名字和id

 */
public class ReturnGetVO {
    private String name;
    private Long id;
}
