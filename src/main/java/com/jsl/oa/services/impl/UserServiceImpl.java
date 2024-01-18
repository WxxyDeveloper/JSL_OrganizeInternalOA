package com.jsl.oa.services.impl;

import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.exception.BusinessException;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.RoleDO;
import com.jsl.oa.model.doData.RoleUserDO;
import com.jsl.oa.model.doData.UserCurrentDO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.*;
import com.jsl.oa.services.UserService;
import com.jsl.oa.utils.*;
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
    public BaseResponse userDelete(HttpServletRequest request,Long id) {
        //判断用户是否存在
        if (userDAO.isExistUser(id)) {
            if(!Processing.checkUserIsAdmin(request,roleMapper)){
                return ResultUtil.error(ErrorCode.NOT_ADMIN);
            }
            userDAO.userDelete(id);
            return ResultUtil.success("删除成功");
        } else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse userLock(HttpServletRequest request,Long id) {
        //判断用户是否存在
        if (userDAO.isExistUser(id)) {
            if (!Processing.checkUserIsAdmin(request,roleMapper)){
                return ResultUtil.error(ErrorCode.NOT_ADMIN);
            }
            userDAO.userLock(id);
            return ResultUtil.success("锁定成功");
        } else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse userEditProfile(@NotNull UserEditProfileVO userEditProfileVO) {
        if (userDAO.isExistUser(userEditProfileVO.getId())) {
            userDAO.userEditProfile(userEditProfileVO);
            return ResultUtil.success("修改成功");
        } else return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
    }

    @Override
    public BaseResponse userCurrentAll(HttpServletRequest request, @NotNull UserAllCurrentVO userAllCurrentVO) {
        // 检查是否是管理员用户
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
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
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
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




    @Override
    public BaseResponse userAdd(UserAddVo userAddVo, HttpServletRequest request) {

        //检测用户是否为管理员
        BaseResponse checkManagerResult = isManager(request);
        if(checkManagerResult.getCode() != 200){
            return checkManagerResult;
        }

        //如果用户不重复，添加用户
        if(!userDAO.isRepeatUser(userAddVo.getUsername())){
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
                    .setSex(userAddVo.getSex())
                    .setAccountNoLocked(false);
            // 插入数据
            if (userDAO.userAdd(userDO)) {
                userDO.setPassword(null);
                return ResultUtil.success("添加用户成功", userDO);
            } else {
                throw new BusinessException(ErrorCode.DATABASE_INSERT_ERROR);
            }
        }else return ResultUtil.error(ErrorCode.USER_EXIST);
    }



    @Override
    public BaseResponse userEdit(UserEditVo userEditVo, HttpServletRequest request) {
        //检测用户是否为管理员
        BaseResponse checkManagerResult = isManager(request);
        if(checkManagerResult.getCode() != 200){
            return checkManagerResult;
        }
        //根据id获取用户信息
        UserDO userDO = userDAO.getUserById(userEditVo.getId());
        if(userDO == null){
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }

        //修改非空属性
        try {
            Processing.copyProperties(userEditVo,userDO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //向数据库中修改属性
        userDAO.userEdit(userDO);

        return ResultUtil.success("用户信息修改成功");
    }

    @Override
    public BaseResponse userProflieGet(Long id) {

        UserDO userDO = userDAO.getUserById(id);
        if(userDO == null){
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }
        UserProfile userProfile = new UserProfile();
        try {
            Processing.copyProperties(userDO,userProfile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        userProfile.setSex(Processing.getSex(userDO.getSex()));
        return ResultUtil.success(userProfile);
    }


    /**
     * @Description: TODO 判断用户是否为管理员
     * @Date: 2024/1/18
     * @Param request: 请求头
     **/
    public BaseResponse isManager(HttpServletRequest request){
        //获取token
        String originalAuthorization = request.getHeader("Authorization");
        String token = originalAuthorization.replace("Bearer ", "");
        //获取操作用户的权限
        RoleUserDO roleUserDO = userDAO.getRoleFromUser(JwtUtil.getUserId(token));
        //用户权限不为空
        if(roleUserDO == null){
            return ResultUtil.error(ErrorCode.USER_ROLE_NOT_EXIST);
        }
        //用户权限应为管理员
        if(!userDAO.isManagerByRoleId(roleUserDO.getRid())){
            return ResultUtil.error(ErrorCode.USER_ROLE_NOT_MANAGER);
        }
        return ResultUtil.success();
    }


}
