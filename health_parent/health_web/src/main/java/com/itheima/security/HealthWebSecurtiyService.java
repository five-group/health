package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 自定义认证和授权类
 * @author wangxin
 * @version 1.0
 */
@Component
public class HealthWebSecurtiyService implements UserDetailsService{

    @Reference
    private UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.查询用户信息
        com.itheima.pojo.User user = userService.findByUserName(username);

        //2.user为空return null
        if(user == null){
            return null;
        }
        //3.最终将密码交给框架
        String password = user.getPassword();

        //4.用户认证成功后，就可以为用户授权权限
        List<GrantedAuthority> authorities = new ArrayList<>();//定义空的权限集合
        Set<Role> roles = user.getRoles();
        if(roles != null &&roles.size()>0){
            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getKeyword()));
                Set<Permission> permissions = role.getPermissions();//角色表权限关键字
                if(permissions != null && permissions.size()>0){
                    for (Permission permission : permissions) {
                        authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));//权限表权限关键字
                    }
                }
            }
        }
        return new User(username,password,authorities);
    }
}
