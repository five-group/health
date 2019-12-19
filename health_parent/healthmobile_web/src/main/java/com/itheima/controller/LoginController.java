package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 移动端-手机号快速登录
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;


    @Reference
    private MemberService memberService;

    /**
     * 登录方法/check.do
     */
    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public Result login(@RequestBody Map map, HttpServletResponse response){
        //map:手机号 验证码  reponse:将登录成功后手机号信息写入cookie
        //1 后台接收（手机号+验证码-map）
        try {
            String validateCode = (String) map.get("validateCode");
            String telephone = (String) map.get("telephone");
            String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone);
            // 2校验校验码
            if(StringUtils.isEmpty(validateCode) || StringUtils.isEmpty(redisCode)  || !validateCode.equals(redisCode)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            // 后台还行判断用户是否是会员，不是则自动注册
            Member member = memberService.findByTelephone(telephone);
            if(member == null){
                //自动注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            // 如果是会员，通过java代码方式将用户的信息写入cookie
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//所有页面都能使用cookie
            cookie.setMaxAge(60*60*24*30);//过期时间 30天有效
            response.addCookie(cookie);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.LOGIN_FAIL);

        }

    }
}
