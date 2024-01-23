package com.jsl.oa.model.doData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <h1>分页数据对象</h1>
 * <hr/>
 * 用于分页数据的封装
 *
 * @since v1.1.0
 * @version v1.1.0
 * @author 176yunxuan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBeanDO<R> {
    //总记录数
    private Long total;
    //数据列表
    private List<R> rows;
}
