package com.jsl.oa.model.voData;

import lombok.Data;


@Data
public class UserProfileVo {

    private Long id;
    private String username;
    private String address;
    private String phone;
    private String email;
    private Short age;
    private String signature;
    private String avatar;
    private String nickname;
    private String sex;
    private String description;

}


