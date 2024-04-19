package com.jsl.oa.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.exception.ClassCopyException;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.RoleDO;
import com.jsl.oa.model.dodata.RoleUserDO;
import com.jsl.oa.model.dodata.UserDO;
import com.jsl.oa.model.vodata.ProjectSimpleVO;
import com.jsl.oa.model.vodata.UserCurrentBackVO;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * @return 如果为 true 是管理员，false 不是管理员
     */
    public static @NotNull Boolean checkUserIsConsole(HttpServletRequest request, @NotNull RoleDAO roleDAO) {
        RoleUserDO roleUserDO = roleDAO.getRoleUserByUid(Processing.getAuthHeaderToUserId(request));
        if (roleUserDO != null) {
            RoleDO roleDO = roleDAO.getRoleByRoleName("console");
            return roleUserDO.getRid().equals(roleDO.getId());
        } else {
            return false;
        }
    }

    /**
     * 检查用户是否是老师
     *
     * @param request 请求
     * @return 如果为 true 是老师，false 不是老师
     */
    public static @NotNull Boolean checkUserIsPrincipal(HttpServletRequest request, @NotNull RoleDAO roleDAO) {
        RoleUserDO roleUserDO = roleDAO.getRoleUserByUid(Processing.getAuthHeaderToUserId(request));
        if (roleUserDO != null) {
            RoleDO roleDO = roleDAO.getRoleByRoleName("principal");
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
     * 将属性从源对象复制到目标对象。
     *
     * @param <T>    目标对象的类型。
     * @param <S>    源对象的类型。
     * @param source 从中复制属性的源对象。
     * @param target 属性将复制到的目标对象。
     * @throws ClassCopyException 如果在复制过程中出现错误。
     */
    @Contract(pure = true)
    public static <T, S> void copyProperties(@NotNull S source, @NotNull T target) throws ClassCopyException {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        try {
            Field[] sourceFields = sourceClass.getDeclaredFields();
            for (Field sourceField : sourceFields) {
                String fieldName = sourceField.getName();
                Field targetField;
                try {
                    targetField = targetClass.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    // 目标对象不存在该属性，忽略
                    continue;
                }

                sourceField.setAccessible(true);
                targetField.setAccessible(true);

                Object value = sourceField.get(source);

                if (value == null) {
                    continue;
                }

                //如果获取的值不为数字且等于“”，则跳过
                if ("".equals(value)) {
                    continue;
                }

                if (!sourceField.getType().equals(targetField.getType())) {
                    continue;
                }

                targetField.set(target, value);
            }
        } catch (IllegalAccessException ignored) {
            throw new ClassCopyException();
        }
    }

    /**
     * <h2>获取性别</h2>
     * <hr/>
     * 用于获取性别
     *
     * @param sex 性别ID
     * @return 返回中文性别
     */
    @Contract(pure = true)
    public static @NotNull String getSex(short sex) {
        switch (sex) {
            case 1:
                return "男";
            case 2:
                return "女";
            default:
                return "保密";
        }
    }

    /**
     * <h2>封装返回内容</h2>
     * <hr/>
     * 封装返回内容
     *
     * @param userDO 用户信息
     * @return {@link BaseResponse}
     */
    public static @NotNull UserCurrentBackVO.UserCurrent returnUserInfo(
            @NotNull UserDO userDO, RoleDAO roleDAO, Gson gson) {
        UserCurrentBackVO.UserCurrent userCurrent = new UserCurrentBackVO.UserCurrent();
        // 获取用户权限
        RoleDO getRole = roleDAO.getRoleByUserId(userDO.getId());
        List<String> getPermissionForString;
        if (getRole != null) {
            // 获取全部根权限
            getPermissionForString = gson.fromJson(getRole.getPermissions(), new TypeToken<List<String>>() {
            }.getType());
        } else {
            getPermissionForString = null;
        }
        UserCurrentBackVO.ReturnUser returnUser = new UserCurrentBackVO.ReturnUser();
        BeanUtils.copyProperties(userDO, returnUser);
        userCurrent
                .setUser(returnUser)
                .setRole(getRole != null ? getRole.getRoleName() : "default")
                .setPermission(getPermissionForString);
        return userCurrent;
    }

    public static List<UserDO> orderUser(List<UserDO> userDOS, String order, String orderBy) {

        Comparator<UserDO> comparator = null;

        if (order.equals("asc")) {
            if (orderBy.equals("userName")) {
                comparator = Comparator.comparing(UserDO::getUsername);
            } else if (orderBy.equals("userId")) {
                comparator = Comparator.comparingLong(UserDO::getId);
            }
        } else if (order.equals("desc")) {
            if (orderBy.equals("userName")) {
                comparator = Comparator.comparing(UserDO::getUsername).reversed();
            } else if (orderBy.equals("userId")) {
                comparator = Comparator.comparingLong(UserDO::getId).reversed();
            }
        }

        userDOS.sort(comparator);
        return userDOS;
    }

    public static void projectTosimply(
            ProjectSimpleVO projectSimpleVO,
            ProjectDO projectDO,
            UserDAO userDAO,
            ObjectMapper objectMapper
    ) {
        projectSimpleVO.setId(projectDO.getId());
        projectSimpleVO.setName(projectDO.getName());
        projectSimpleVO.setTags(projectDO.getTags());
        projectSimpleVO.setCycle(projectDO.getCycle().longValue());
        projectSimpleVO.setWorkLoad(projectDO.getWorkLoad().longValue());
        projectSimpleVO.setFiles(projectDO.getFiles());
        projectSimpleVO.setStatus(projectDO.getStatus());
        projectSimpleVO.setDeadLine(projectDO.getDeadline());
        projectSimpleVO.setPrincipalUser(userDAO.getUserById(projectDO.getPrincipalId()).getUsername());
        // 解析JSON字符串
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(projectDO.getDescription());
            // 访问特定的key
            JsonNode targetNode = rootNode.get("description");
            if (targetNode != null && !rootNode.isNull()) {
                projectSimpleVO.setDescription(targetNode.asText());
            } else {
                projectSimpleVO.setDescription("null");
            }
        } catch (JsonProcessingException ignored) {

        }

        //return ProjectSimpleVO;
    }


    /**
     * @Description: 转换审核的类别属性为字符串
     * @Date: 2024/4/11
     * @Param category:
     **/
    public static String turnReviewCategory(short category) {
        switch (category) {
            case 0:
                return "子系统";
            case 1:
                return "模块";
            default:
                return "其他";
        }
    }

    public static String turnReviewResult(short result) {
        switch (result) {
            case 0:
                return "已拒绝";
            case 1:
                return "已审批";
            case 2:
                return "待审核";
            default:
                return "其他";
        }
    }


    /**
     * @Description: list分页方法
     * @Date: 2024/4/18
     * @Param list:
     * @Param pageNumber: 当前页码
     * @Param pageSize: 每页几条
     **/
    public static <T> List<T> getPage(List<T> list, int pageNumber, int pageSize) {
        int fromIndex = (pageNumber - 1) * pageSize;
        if (fromIndex >= list.size()) {
            return new ArrayList<>(); // 返回空列表，表示超出范围
        }
        int toIndex = Math.min(fromIndex + pageSize, list.size());
        return list.subList(fromIndex, toIndex);
    }


    /**
     * @Description: 字符与时间类型转换方法
     * @Date: 2024/4/19
     * @Param dateString:
     **/
    public static Date convertStringToDate(String dateString) {
        if (dateString.isEmpty()) {
            return null; // 如果字符串为空，返回空的Date对象
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return dateFormat.parse(dateString);
            } catch (ParseException e) {
                return null;
            }
        }
    }



}
