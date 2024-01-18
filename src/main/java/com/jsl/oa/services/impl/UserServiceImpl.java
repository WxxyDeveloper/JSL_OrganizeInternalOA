package com.jsl.oa.services.impl;

import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.RoleDO;
import com.jsl.oa.model.doData.RoleUserDO;
import com.jsl.oa.model.doData.UserCurrentDO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserAllCurrentVO;
import com.jsl.oa.model.voData.UserEditProfileVO;
import com.jsl.oa.services.UserService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final RoleMapper roleMapper;

    @Override
    public UserDO getUserInfoByUsername(String username) {
        return userDAO.getUserInfoByUsername(username);
    }

    @Override
    public BaseResponse userDelete(Long id) {
        //判断用户是否存在
        if (userDAO.isExistUser(id)) {
            userDAO.userDelete(id);
            return ResultUtil.success("删除成功");
        } else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse userLock(Long id) {
        //判断用户是否存在
        if (userDAO.isExistUser(id)) {
            userDAO.userLock(id);
            return ResultUtil.success("锁定成功");
        } else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse userEditProfile(@NotNull UserEditProfileVO userEditProfileVO) {
        if (userDAO.isExistUser(userEditProfileVO.getId())) {
            if (userEditProfileVO.getPassword() != null) {
                userEditProfileVO.setPassword(BCrypt.hashpw(userEditProfileVO.getPassword(), BCrypt.gensalt()));
            }
            userDAO.userEditProfile(userEditProfileVO);
            return ResultUtil.success("修改成功");
        } else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse userCurrentAll(HttpServletRequest request, @NotNull UserAllCurrentVO userAllCurrentVO) {
        // 检查是否是管理员用户
        if (!checkUserIsAdmin(request)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
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
        List<UserCurrentDO> userAllCurrentVOList;
        if (userAllCurrentVO.getSearch() != null && !userAllCurrentVO.getSearch().isEmpty()) {
            if (Pattern.matches("^[0-9A-Za-z_@]+$", userAllCurrentVO.getSearch())) {
                userAllCurrentVOList = userDAO.userCurrentAllLike(userAllCurrentVO);
            } else {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("只允许 0-9、A-Z、a-z、_和@进行查询");
                return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, arrayList);
            }
        } else {
            userAllCurrentVOList = userDAO.userCurrentAll(userAllCurrentVO);
        }
        // 检查是否存在 Role 筛选
        if (userAllCurrentVO.getRole() != null) {
            userAllCurrentVOList.removeIf(it -> !userAllCurrentVO.getRole().equals(it.getRole().getRid()));
        }
        return ResultUtil.success(userAllCurrentVOList);
    }

    @Override
    public BaseResponse userCurrent(HttpServletRequest request, String id, String username, String email, String phone) {
        // 检查是否是管理员用户
        if (!checkUserIsAdmin(request)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        // 根据顺序优先级进行用户信息获取
        UserCurrentDO userCurrentDO = null;
        if (id != null && !id.isEmpty()) {
            userCurrentDO = userDAO.userCurrentById(Long.valueOf(id));
        } else if (username != null && !username.isEmpty()) {
            userCurrentDO = userDAO.userCurrentByUsername(username);
        } else if (email != null && !email.isEmpty()) {
            userCurrentDO = userDAO.userCurrentByEmail(email);
        } else if (phone != null && !phone.isEmpty()) {
            userCurrentDO = userDAO.userCurrentByPhone(phone);
        }
        // 返回结果
        if (userCurrentDO != null) {
            return ResultUtil.success(userCurrentDO);
        } else {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
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
    private @NotNull Boolean checkUserIsAdmin(HttpServletRequest request) {
        RoleUserDO roleUserDO = roleMapper.getRoleUserByUid(Processing.getAuthHeaderToUserId(request));
        if (roleUserDO != null) {
            RoleDO roleDO = roleMapper.getRoleByRoleName("admin");
            return roleUserDO.getRid().equals(roleDO.getId());
        } else {
            return false;
        }
    }
}
