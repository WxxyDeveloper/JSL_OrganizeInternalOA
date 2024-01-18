package com.jsl.oa.model.voData;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;


@Data
public class UserProfile {

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


