package com.itheima.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义认证和授权类
 * @author wangxin
 * @version 1.0
 */
public class MySpringSecurtiyService implements UserDetailsService{

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 自定义认证和授权方法实现
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户查询用户信息 （username用户页面输入的）
        //目前造假数据  不真的去调用数据库
        com.itheima.pojo.User dbUser = findUserByUname(username);

        //2.用户对象判断，如果为空true return null;
        if(dbUser == null){
            return null;
        }
        //3.如果不为空 返回框架中User对象
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        //授权ROLE_ADMIN权限  SimpleGrantedAuthority("可以是角色关键字 或  权限关键字")
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));//后续从数据库中查询
        grantedAuthorityList.add(new SimpleGrantedAuthority("add"));
        //String username:用户名 String password:数据库中密码  Collection<? extends GrantedAuthority> authorities:权限列表（认证成功了 授权）
        return new User(username,dbUser.getPassword(),grantedAuthorityList);
    }


    //模拟从数据库查询
    private com.itheima.pojo.User findUserByUname(String username) {
        if ("admin".equals(username)) {
            com.itheima.pojo.User user = new com.itheima.pojo.User();
            user.setUsername(username);
            //encode：对明文进行加密
            //matches:验证密码是否正确方法
            user.setPassword(encoder.encode("123456"));
            return user;
        }
        return null;
    }
}
