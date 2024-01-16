package com.jsl.oa.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Random;

/**
 * <h1>自定义快捷工具类</h1>
 * <hr/>
 *
 * @author 筱锋xiao_lfeng
 * @since v1.0.0
 * @version v1.0.0
 */
public class Processing {

    /**
     * <h1>获取参数校验错误信息</h1>
     * <hr/>
     * 用于获取参数校验错误信息
     *
     * @since v1.0.0
     * @param bindingResult 参数校验结果
     * @return {@link ArrayList<String>}
     */
    public static @NotNull ArrayList<String> getValidatedErrorList(BindingResult bindingResult) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            arrayList.add(objectError.getDefaultMessage());
        }
        return arrayList;
    }

    /**
     * <h1>生成工号</h1>
     * <hr/>
     * 用于生成工号，默认长度为10
     *
     * @since v1.0.0
     * @param type 0:学生 1:教师 2:其他
     * @return {@link String}
     */
    public static @NotNull String createJobNumber(Short type) {
        return createJobNumber(type, (short) 10);
    }

    /**
     * <h1>生成工号</h1>
     * <hr/>
     * 用于生成工号
     *
     * @since v1.0.0
     * @param type 0:学生 1:教师 2:其他
     * @param size 工号长度
     * @return {@link String}
     */
    public static @NotNull String createJobNumber(Short type, Short size) {
        StringBuilder stringBuilder = new StringBuilder();
        if (type == 0) {
            stringBuilder.append("STU");
        } else if (type == 1) {
            stringBuilder.append("TCH");
        } else {
            stringBuilder.append("OTH");
        }
        // 生成工号
        Random random = new Random();
        for (int i = 0; i < size-3; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    /**
     *
     */
    public static @NotNull Integer createCode() {
        StringBuilder stringBuilder = new StringBuilder();
        // 生成验证码
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return Integer.valueOf(stringBuilder.toString());
    }
}
