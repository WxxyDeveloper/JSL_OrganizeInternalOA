package com.jsl.oa.model.vodata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDailyDataVO {

    private Integer totalCount;

    private Integer currentPage;

    private Integer pageSize;

    private List<ProjectDailyVO> list;

}


