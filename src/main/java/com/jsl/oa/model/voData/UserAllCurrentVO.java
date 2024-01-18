package com.jsl.oa.model.voData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAllCurrentVO {
    private Long page;
    private Long limit;
    private String search;
    private Long role;
}
