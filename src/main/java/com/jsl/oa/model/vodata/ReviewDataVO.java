package com.jsl.oa.model.vodata;


import lombok.Data;

import java.util.List;

@Data
public class ReviewDataVO {

    private Integer totalCount;

    private Integer currentPage;

    private Integer pageSize;

    private List<ReviewVO> reviews;
}


