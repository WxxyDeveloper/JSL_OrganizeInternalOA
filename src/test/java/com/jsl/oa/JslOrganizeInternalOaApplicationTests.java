package com.jsl.oa;

import com.jsl.oa.utils.Processing;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JslOrganizeInternalOaApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(Processing.generateKey(1L));
        System.out.println(Processing.generateKey(1L));
        System.out.println(Processing.generateKey(2L));
        System.out.println(Processing.generateKey(2L));
        System.out.println(Processing.generateKey(3L));
        System.out.println(Processing.generateKey(3L));
        System.out.println(Processing.generateKey(3L));
    }

}
