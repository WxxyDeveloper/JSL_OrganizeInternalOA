package com.jsl.oa.dao;

import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.doData.RoleDO;
import com.jsl.oa.model.doData.RoleUserDO;
import com.jsl.oa.model.doData.UserCurrentDO;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.voData.UserAllCurrentVO;
import com.jsl.oa.model.voData.UserEditProfileVO;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDAO {

    private final UserMapper userMapper;
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
        if (userMapper.getUserById(id) == null) {
            return false;
        } else return true;
    }

    /**
     * 用户账号删除
     *
     * @param id
     */
    public void userDelete(Long id) {
        userMapper.userDelete(id);
    }

    /**
     * 用户账号锁定
     *
     * @param id
     */
    public void userLock(Long id,Long isLock) {
        userMapper.userLock(id,isLock);
    }

    public void userEditProfile(UserEditProfileVO userEditProfileVO) {
        userMapper.userEditProfile(userEditProfileVO);
    }

    public List<UserCurrentDO> userCurrentAll(UserAllCurrentVO userAllCurrentVO) {
        List<UserCurrentDO> userCurrentDO = userMapper.getAllUser(userAllCurrentVO);
        return this.userCurrentAll(userCurrentDO);

    }

    public List<UserCurrentDO> userCurrentAllLike(UserAllCurrentVO userAllCurrentVO) {
        List<UserCurrentDO> userCurrentDO = userMapper.getAllUserBySearch(userAllCurrentVO);
        return this.userCurrentAll(userCurrentDO);
    }

    @Contract("_ -> param1")
    private @NotNull List<UserCurrentDO> userCurrentAll(@NotNull List<UserCurrentDO> userCurrentDO) {
        userCurrentDO.forEach(it -> {
            it.setRole(roleMapper.getRoleUserByUid(it.getId()));
            if (it.getRole() == null) {
                RoleUserDO newRoleUserDO = new RoleUserDO();
                newRoleUserDO.setRid(0L)
                        .setUid(it.getId())
                        .setCreatedAt(new Timestamp(System.currentTimeMillis()));
                it.setRole(newRoleUserDO);
            }
        });
        return userCurrentDO;
    }

    /**
     * 获取当前用户信息
     *
     * @param uid 用户id
     * @return {@link UserCurrentDO}
     */
    public UserCurrentDO userCurrentById(Long uid) {
        UserCurrentDO userCurrentDO = userMapper.getUserCurrentById(uid);
        return getUserCurrentForRole(userCurrentDO);
    }

    /**
     * 获取当前用户信息
     *
     * @param username 用户名
     * @return {@link UserCurrentDO}
     */
    public UserCurrentDO userCurrentByUsername(String username) {
        UserCurrentDO userCurrentDO = userMapper.getUserCurrentByUsername(username);
        return getUserCurrentForRole(userCurrentDO);
    }

    /**
     * 获取当前用户信息
     *
     * @param email 邮箱
     * @return {@link UserCurrentDO}
     */
    public UserCurrentDO userCurrentByEmail(String email) {
        UserCurrentDO userCurrentDO = userMapper.getUserCurrentByEmail(email);
        return getUserCurrentForRole(userCurrentDO);
    }

    /**
     * 获取当前用户信息
     *
     * @param phone 手机号
     * @return {@link UserCurrentDO}
     */
    public UserCurrentDO userCurrentByPhone(String phone) {
        UserCurrentDO userCurrentDO = userMapper.getUserCurrentByPhone(phone);
        return getUserCurrentForRole(userCurrentDO);
    }

    private UserCurrentDO getUserCurrentForRole(UserCurrentDO userCurrentDO) {
        if (userCurrentDO != null) {
            RoleUserDO newRoleUserDO = new RoleUserDO();
            newRoleUserDO.setRid(0L)
                    .setUid(userCurrentDO.getId())
                    .setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userCurrentDO.setRole(newRoleUserDO);
            return userCurrentDO;
        } else {
            return null;
        }
    }


    /**
     * @return
     * @Description 用户添加
     * @Date: 2024/1/16
     * @Param userDO: user 数据库表实体类
     */
    public boolean userAdd(UserDO userDO) {
        return userMapper.insertUser(userDO);
    }

    public void userEdit(UserDO userDO) {
        userMapper.updateUser(userDO);
    }


    /**
     * @Description 根据username检测用户是否重复
     * @Date: 2024/1/16
     * @Param username: 用户名
     **/
    public Boolean isRepeatUser(String username) {
        if (userMapper.getUserInfoByUsername(username) == null) {
            return false;
        }
        return true;
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
}
