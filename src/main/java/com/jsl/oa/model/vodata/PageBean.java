package com.jsl.oa.model.vodata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean<T> {
    private int totalCount; // 总记录数
    private int currentPage; // 当前页码
    private int pageSize; // 每页记录数
    private List<T> list; // 当前页的数据列表
}
