package com.jsl.oa.model.vodata;


import lombok.Data;

import java.util.List;

@Data
public class ReviewDataVO {

    private Integer dataTotal;

    private List<ReviewVO> data;
}


