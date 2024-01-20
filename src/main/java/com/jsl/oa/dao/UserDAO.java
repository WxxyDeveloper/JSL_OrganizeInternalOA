package com.jsl.oa.dao;

import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.doData.RoleDO;
import com.jsl.oa.model.doData.RoleUserDO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserAllCurrentVO;
import com.jsl.oa.model.voData.UserCurrentBackVO;
import com.jsl.oa.model.voData.UserEditProfileVO;
import com.jsl.oa.utils.Processing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDAO {

    public final UserMapper userMapper;
    private final RoleMapper roleMapper;

    /**
     * <h2>用户名获取用户信息</h2>
     * <hr/>
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return {@link UserDO}
     * @author 筱锋xiao_lfeng
     */
    public UserDO getUserInfoByUsername(String username) {
        log.info("\t> 执行 DAO 层 UserDAO.getUserInfoByUsername 方法");
        UserDO userDO = null;
        // 从 Redis 获取数据
        // TODO: 10000-Redis: 从 Redis 获取数据
        // 从数据库获取用户信息
        if (userDO == null) {
            userDO = userMapper.getUserInfoByUsername(username);
        }
        return userDO;
    }

    /**
     * 根据id判断用户是否存在
     *
     * @param id
     * @return
     */
    public Boolean isExistUser(Long id) {
        log.info("\t> 执行 DAO 层 UserDAO.isExistUser 方法");
        return userMapper.getUserById(id) != null;
    }

    /**
     * 用户账号删除
     *
     * @param id
     */
    public void userDelete(Long id) {
        log.info("\t> 执行 DAO 层 UserDAO.userDelete 方法");
        userMapper.userDelete(id);
    }

    public boolean userGetDelete(Long id) {
        log.info("\t> 执行 DAO 层 UserDAO.userGetDelete 方法");
        return userMapper.userGetDelete(id);
    }

    /**
     * 用户账号锁定
     *
     * @param id
     */
    public void userLock(Long id,Long isLock) {
        log.info("\t> 执行 DAO 层 UserDAO.userLock 方法");
        userMapper.userLock(id,isLock);
    }

    public void userEditProfile(UserEditProfileVO userEditProfileVO) {
        log.info("\t> 执行 DAO 层 UserDAO.userEditProfile 方法");
        userMapper.userEditProfile(userEditProfileVO);
    }

    public List<UserCurrentBackVO> userCurrentAll(UserAllCurrentVO userAllCurrentVO) {
        log.info("\t> 执行 DAO 层 UserDAO.userCurrentAll 方法");
        List<UserDO> userCurrentDO = userMapper.getAllUser(userAllCurrentVO);
        List<UserCurrentBackVO> userCurrentDOList = new ArrayList<>();
        userCurrentDO.forEach(it -> {
            userCurrentDOList.add(Processing.ReturnUserInfo(it, roleMapper));
        });
        return userCurrentDOList;

    }

    public List<UserCurrentBackVO> userCurrentAllLike(UserAllCurrentVO userAllCurrentVO) {
        log.info("\t> 执行 DAO 层 UserDAO.userCurrentAllLike 方法");
        List<UserDO> userCurrentDO = userMapper.getAllUserBySearch(userAllCurrentVO);
        List<UserCurrentBackVO> userCurrentDOList = new ArrayList<>();
        userCurrentDO.forEach(it -> {
            userCurrentDOList.add(Processing.ReturnUserInfo(it, roleMapper));
        });
        return userCurrentDOList;
    }


    /**
     * @return
     * @Description 用户添加
     * @Date: 2024/1/16
     * @Param userDO: user 数据库表实体类
     */
    public boolean userAdd(UserDO userDO) {
        log.info("\t> 执行 DAO 层 userAdd 方法");
        return userMapper.insertUser(userDO);
    }

    public void userEdit(UserDO userDO) {
        log.info("\t> 执行 DAO 层 userEdit 方法");
        userMapper.updateUser(userDO);
    }


    /**
     * @Description 根据username检测用户是否重复
     * @Date: 2024/1/16
     * @Param username: 用户名
     **/
    public Boolean isRepeatUser(String username) {
        log.info("\t> 执行 DAO 层 isRepeatUser 方法");
        return userMapper.getUserInfoByUsername(username) != null;
    }


    /**
     * @Description 检测用户工号是否重复
     * @Date 2024/1/18
     * @Param userNum:
     **/
    public Boolean isRepeatUserNum(String userNum) {
        if (userMapper.getUserByUserNum(userNum) != null) {
            return true;
        }
        return false;
    }

    /**
     * @Description 根据用户id获取用户数据
     * @Date 2024/1/17
     * @Param userId
     **/
    public UserDO getUserById(Long userId) {
        log.info("\t> 执行 DAO 层 getUserById 方法");
        return userMapper.getUserById(userId);
    }


    /**
     * @Description 根据用户id查询对应用户权限
     * @Date 2024/1/18
     * @Param uid:用户id
     **/
    public RoleUserDO getRoleFromUser(Long uid) {
        return userMapper.getRoleIdByUserId(uid);
    }


    /**
     * @Description 检验用户权限是否为管理员
     * @Date 2024/1/18
     * @Param null:用户id
     **/
    public Boolean isManagerByRoleId(Long roleId) {
        RoleDO role = userMapper.getRoleById(roleId);
        if (role == null) {
            return false;
        }
        if (role.getRoleName().equals("admin")) {
            return true;
        }
        return false;
    }


    public List<UserDO> getRecommendUser(){
        return userMapper.getRecommendUser();
    }

    public UserDO getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    public UserDO getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }
}
