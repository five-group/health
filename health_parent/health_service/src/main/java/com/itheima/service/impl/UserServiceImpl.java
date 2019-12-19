package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 用户业务处理层
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @Override
    public User findByUserName(String username) {
        //通过用户关联查询角色，再通过角色查询权限
        //方式一：通过映射文件方式
        //方式二：编码方式来查询
        //1.查询用户信息
        User user = userDao.findByUserName(username);
        if(user != null){
            Integer userId = user.getId();//用户id
            //2.根据用户信息查询角色信息
            Set<Role> roleSet = roleDao.findByUserId(userId);
            if(roleSet != null && roleSet.size()>0){
                user.setRoles(roleSet);//角色设置到用户对象中  1个用户对应多个角色
                //3.根据角色信息查询权限信息  设置到用户对象中
                for (Role role : roleSet) {
                    Integer roleId = role.getId();//角色id
                    Set<Permission> permissionSet = permissionDao.findByRoleId(roleId);
                    if(permissionSet!=null && permissionSet.size()>0){
                        role.setPermissions(permissionSet);
                    }
                }
            }
        }
        return user;
    }
}
