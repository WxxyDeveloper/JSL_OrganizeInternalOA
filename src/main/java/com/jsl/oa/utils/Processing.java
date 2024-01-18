package com.jsl.oa.utils;

import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.RoleDO;
import com.jsl.oa.model.doData.RoleUserDO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
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

    /**
     * <h2>获取Authorization Header</h2>
     * <hr/>
     * 用于获取Authorization Header
     *
     * @param request 请求
     */
    public static @Nullable Long getAuthHeaderToUserId(@NotNull HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return null;
        } else {
            // 解析Bearer后面的令牌
            token = token.replace("Bearer ", "");
            return JwtUtil.getUserId(token);
        }
    }

    /**
     * <h2>检查用户是否是管理员</h2>
     * <hr/>
     * 该方法用于检查用户是否是管理员，类型封装后字节返回结果
     *
     * @param request 请求
     * @param roleMapper RoleMapper
     * @return 如果为 true 是管理员，false 不是管理员
     */
    public static @NotNull Boolean checkUserIsAdmin(HttpServletRequest request, @NotNull RoleMapper roleMapper) {
        RoleUserDO roleUserDO = roleMapper.getRoleUserByUid(Processing.getAuthHeaderToUserId(request));
        if (roleUserDO != null) {
            RoleDO roleDO = roleMapper.getRoleByRoleName("admin");
            return roleUserDO.getRid().equals(roleDO.getId());
        } else {
            return false;
        }
    }

    private static char getCharFromIndex(int index) {
        // 生成字符集合，可以根据需要自定义
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return charset.charAt(index);
    }


    /**
     * @Description: TODO VO类与实体类属性赋值
     * @Date: 2024/1/18
     * @Param source:
     * @Param dest:
     **/
    public static <T, S> T copyProperties(S source, T target) throws Exception {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        Field[] sourceFields = sourceClass.getDeclaredFields();
        for (Field sourceField : sourceFields) {
            String fieldName = sourceField.getName();
            Field targetField = null;
            try {
                targetField = targetClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // 目标对象不存在该属性，忽略
                continue;
            }

            sourceField.setAccessible(true);
            targetField.setAccessible(true);

            Object value = sourceField.get(source);

            if(value == null){
                continue;
            }

            //如果获取的值不为数字且等于“”，则跳过
            if ( !(value instanceof Number) && value.equals("")) {
                continue;
            }

            if (!sourceField.getType().equals(targetField.getType())) {
                continue;
            }

            targetField.set(target, value);
        }

        return target;
    }

    /**
     * @Description: TODO 将性别转为字符形式
     * @Date: 2024/1/18

     **/
    public static String getSex(short sex){
        if(sex == 0){
            return "保密";
        }
        if(sex == 1){
            return "男";
        }
        if(sex == 2){
            return "女";
        }
        return " ";
    }


}
