package com.jsl.oa.model.doData;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
public class UserCurrentDO {
    private Long id;
    private String jobId;
    private String username;
    private String address;
    private String phone;
    private String email;
    private Short age;
    private String signature;
    private String avatar;
    private String nickname;
    private Short sex;
    private Boolean enabled;
    private Boolean accountNoExpired;
    private Boolean credentialsNoExpired;
    private Boolean recommend;
    private Boolean accountNoLocked;
    private String description;
    private RoleUserDO role;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
