package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制层
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 通过SecurityContext获取认证对象，最终通过认证对象获取用户对象
     */
    @RequestMapping(value = "/findUserName",method = RequestMethod.GET)
    public Result findUserName() {
        //getPrincipal() 用户对象 可以使用User强转
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
    }
}
