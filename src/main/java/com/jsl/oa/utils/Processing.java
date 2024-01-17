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
 * @version v1.0.0
 * @since v1.0.0
 */
public class Processing {

    /**
     * <h1>获取参数校验错误信息</h1>
     * <hr/>
     * 用于获取参数校验错误信息
     *
     * @param bindingResult 参数校验结果
     * @return {@link ArrayList<String>}
     * @since v1.0.0
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
     * @param type 0:学生 1:教师 2:其他
     * @return {@link String}
     * @since v1.0.0
     */
    public static @NotNull String createJobNumber(Short type) {
        return createJobNumber(type, (short) 10);
    }

    /**
     * <h1>生成工号</h1>
     * <hr/>
     * 用于生成工号
     *
     * @param type 0:学生 1:教师 2:其他
     * @param size 工号长度
     * @return {@link String}
     * @since v1.0.0
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
        for (int i = 0; i < size - 3; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    /**
     * <h1>生成验证码</h1>
     * <hr/>
     * 用于生成验证码，默认长度为6
     *
     * @return {@link Integer}
     */
    public static @NotNull Integer createCode(Integer size) {
        if (size == null) {
            size = 6;
        }
        StringBuilder stringBuilder = new StringBuilder();
        // 生成验证码
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return Integer.valueOf(stringBuilder.toString());
    }

    /**
     * <h1>生成256位字符串</h1>
     * <hr/>
     * 用于生成256位字符串（用于加密）
     *
     * @param input 输入
     * @return 返回加密后的字符串
     */
    public static String generateKey(Long input) {
        String inputString = String.valueOf(input);
        // 使用简单的伪随机算法生成256位字符串
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            int charIndex;
            int math;
            if (i == 0) {
                math = Math.abs(inputString.hashCode());
            } else {
                math = Math.abs(inputString.hashCode() % i);
            }
            if (i < 62) {
                charIndex = (math % 62);
            } else {
                charIndex = (math / 6 % 62);
            }
            char nextChar = getCharFromIndex(charIndex);
            result.append(nextChar);
        }

        return result.toString();
    }

    private static char getCharFromIndex(int index) {
        // 生成字符集合，可以根据需要自定义
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return charset.charAt(index);
    }
}
