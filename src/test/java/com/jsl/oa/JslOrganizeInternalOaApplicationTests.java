package com.jsl.oa;

import com.jsl.oa.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JslOrganizeInternalOaApplicationTests {

    @Test
    void contextLoads() {
        String token = JwtUtil.generateToken("admin");
        if (JwtUtil.verify(token, "admin")) {
            System.out.println("验证通过");
        } else {
            System.out.println("验证失败");
        }
    }

}
