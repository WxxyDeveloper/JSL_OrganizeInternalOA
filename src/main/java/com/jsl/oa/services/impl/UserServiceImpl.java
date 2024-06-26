package com.jsl.oa.services.impl;

import com.google.gson.Gson;
import com.jsl.oa.annotations.UserAbleToUse;
import com.jsl.oa.dao.RoleDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.model.dodata.UserDO;
import com.jsl.oa.model.vodata.*;
import com.jsl.oa.services.UserService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * <h1>用户服务实现类</h1>
 * <hr/>
 * 用户服务实现类，包含用户账号删除、用户账号锁定、用户编辑自己的信息接口
 *
 * @author xiao_lfeng
 * @version v1.1.0
 * @see UserService
 * @see UserEditProfileVO
 * @since v1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final Gson gson;

    @Override
    public UserDO getUserInfoByUsername(String username) {
        return userDAO.getUserInfoByUsername(username);
    }

    @Override
    public BaseResponse userDelete(HttpServletRequest request, Long id) {
        //判断用户是否存在
        if (userDAO.isExistUser(id)) {
            if (!Processing.checkUserIsConsole(request, roleDAO)) {
                return ResultUtil.error(ErrorCode.NOT_ADMIN);
            }
            // 用户是否已删除
            if (!userDAO.userGetDelete(id)) {
                userDAO.userDelete(id);
                return ResultUtil.success("删除成功");
            } else {
                return ResultUtil.error(ErrorCode.USER_ALREADY_DELETE);
            }
        } else {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
    }

    @Override
    public BaseResponse userLock(HttpServletRequest request, Long id, Long isLock) {
        if (!Processing.checkUserIsConsole(request, roleDAO)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        //判断用户是否存在
        if (userDAO.isExistUser(id)) {
            userDAO.userLock(id, isLock);
            return ResultUtil.success("更改成功");
        } else {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
    }

    @Override
    public BaseResponse userEditProfile(@NotNull UserEditProfileVO userEditProfileVO) {
        if (userDAO.isExistUser(userEditProfileVO.getId())) {
            userDAO.userEditProfile(userEditProfileVO);
            return ResultUtil.success("修改成功");
        } else {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
    }

    @Override
    public BaseResponse userCurrentAll(HttpServletRequest request, @NotNull UserAllCurrentVO userAllCurrentVO) {
        // 检查数据
        if (userAllCurrentVO.getPage() == null || userAllCurrentVO.getPage() < 1) {
            userAllCurrentVO.setPage(1L);
        }
        if (userAllCurrentVO.getLimit() == null || userAllCurrentVO.getLimit() < 1) {
            userAllCurrentVO.setLimit(20L);
        }
        // 页码转换
        if (userAllCurrentVO.getPage() > 0) {
            userAllCurrentVO.setPage((userAllCurrentVO.getPage() - 1) * userAllCurrentVO.getLimit());
        }
        // 检查是否处于模糊查询
        UserCurrentBackVO userCurrentBackVO;
        if (userAllCurrentVO.getSearch() != null && !userAllCurrentVO.getSearch().isEmpty()) {
            if (Pattern.matches("^[0-9A-Za-z_@]+$", userAllCurrentVO.getSearch())) {
                userCurrentBackVO = userDAO.userCurrentAllLike(userAllCurrentVO);
            } else {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("只允许 0-9、A-Z、a-z、_和@进行查询");
                return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, arrayList);
            }
        } else {
            userCurrentBackVO = userDAO.userCurrentAll(userAllCurrentVO);
        }
        // 检查是否存在 Role 筛选
        if (userAllCurrentVO.getRole() != null) {
            userCurrentBackVO.getUsers().removeIf(it -> !userAllCurrentVO.getRole().equals(it.getRole()));
        }
        return ResultUtil.success(userCurrentBackVO);
    }

    @Override
    @UserAbleToUse
    public BaseResponse userCurrent(
            HttpServletRequest request,
            String id,
            String username,
            String email,
            String phone
    ) {
        UserDO userDO;
        if (id == null && username == null && email == null && phone == null) {
            // Token获取信息
            userDO = userDAO.getUserById(Processing.getAuthHeaderToUserId(request));
        } else {
            // 根据顺序优先级进行用户信息获取
            userDO = null;
            if (id != null && !id.isEmpty()) {
                userDO = userDAO.getUserById(Long.valueOf(id));
            } else if (username != null && !username.isEmpty()) {
                userDO = userDAO.getUserInfoByUsername(username);
            } else if (email != null && !email.isEmpty()) {
                userDO = userDAO.getUserByEmail(email);
            } else if (phone != null && !phone.isEmpty()) {
                userDO = userDAO.getUserByPhone(phone);
            }
        }
        // 返回结果
        if (userDO != null) {
            return ResultUtil.success(Processing.returnUserInfo(userDO, roleDAO, gson));
        } else {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
    }


    @Override
    public BaseResponse userAdd(UserAddVO userAddVo, HttpServletRequest request) {
        // 检测用户是否为管理员
        if (!Processing.checkUserIsConsole(request, roleDAO)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        //如果用户不重复，添加用户
        if (!userDAO.isRepeatUser(userAddVo.getUsername())) {
            // 生成工号
            String userNum;
            do {
                userNum = Processing.createJobNumber((short) 2);
            } while (userDAO.isRepeatUserNum(userNum));

            // 数据上传
            UserDO userDO = new UserDO();
            userDO.setJobId(userNum)
                    .setUsername(userAddVo.getUsername())
                    .setPassword(BCrypt.hashpw(userAddVo.getPassword(), BCrypt.gensalt()))
                    .setAddress(userAddVo.getAddress())
                    .setPhone(userAddVo.getPhone())
                    .setEmail(userAddVo.getEmail())
                    .setAge(userAddVo.getAge())
                    .setSex(userAddVo.getSex());
            // 插入数据
            if (userDAO.userAdd(userDO)) {
                userDO.setPassword(null);
                return ResultUtil.success("添加用户成功", userDO);
            } else {
                return ResultUtil.error(ErrorCode.DATABASE_INSERT_ERROR);
            }
        } else {
            return ResultUtil.error(ErrorCode.USER_EXIST);
        }
    }


    @Override
    public BaseResponse userEdit(UserEditVO userEditVO, HttpServletRequest request) {
        // 检测用户是否为管理员
        if (!Processing.checkUserIsConsole(request, roleDAO)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        //根据id获取用户信息
        UserDO userDO = userDAO.getUserById(userEditVO.getId());
        if (userDO == null) {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
        //修改非空属性
        userDO.setAddress(userEditVO.getAddress())
                .setPhone(userEditVO.getPhone())
                .setEmail(userEditVO.getEmail())
                .setAge(userEditVO.getAge())
                .setSex(userEditVO.getSex())
                .setSignature(userEditVO.getSignature())
                .setAvatar(userEditVO.getAvatar())
                .setNickname(userEditVO.getNickname())
                .setDescription(userEditVO.getDescription())
                .setEnabled(userEditVO.getEnabled())
                .setAccountNoExpired(userEditVO.getIsExpired())
                .setCredentialsNoExpired(userEditVO.getPasswordExpired())
                .setRecommend(userEditVO.getRecommend())
                .setAccountNoLocked(userEditVO.getIsLocked());
        //向数据库中修改属性
        userDAO.userEdit(userDO);
        roleDAO.roleChangeUser(userEditVO.getId(), userEditVO.getRid());

        return ResultUtil.success("用户信息修改成功");
    }

    @Override
    public BaseResponse userProfileGet(HttpServletRequest request) {
        // 获取用户Id
        UserDO userDO = userDAO.getUserById(Processing.getAuthHeaderToUserId(request));
        UserProfileVo userProfileVo = new UserProfileVo();
        Processing.copyProperties(userDO, userProfileVo);
        userProfileVo.setRole(roleDAO.getRoleByUserId(userDO.getId()).getRoleName());
        userProfileVo.setSex(Processing.getSex(userDO.getSex()));
        return ResultUtil.success(userProfileVo);
    }


}
