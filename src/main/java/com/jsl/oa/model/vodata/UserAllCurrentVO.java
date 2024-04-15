package com.jsl.oa.model.vodata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAllCurrentVO {
    private Long page;
    private Long limit;
    private String search;
    private String role;
}
