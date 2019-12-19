package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 发送验证码
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送验证码
     */
    @RequestMapping(value = "/send4Order",method = RequestMethod.POST)
    public Result send4Order(String telephone){
        try {
            //1.生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //2.调用发送短信验证码接口
            //templateCode:模板 String phoneNumbers手机号码 String param 参数（验证码）
            //用于不会执行
            System.out.println("手机号码：：：：："+telephone+":::::::"+code);
            if(false){
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);
            }
            //3.将验证码存入redis 5分钟过期（为了后续体检预约验证）
            //key:保证唯一 telephone 忘记密码 体检预约 等都有可能发送验证码
            //key :SENDTYPE_ORDER+telephone  SENDTYPE_LOGIN+telephone
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone,5*60,code);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);

    }


    /**
     * 发送登录验证码
     */
    @RequestMapping(value = "/send4Login",method = RequestMethod.POST)
    public Result send4Login(String telephone){
        try {
            //1.生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //2.调用发送短信验证码接口
            //templateCode:模板 String phoneNumbers手机号码 String param 参数（验证码）
            //用于不会执行
            System.out.println("手机号码：：：：："+telephone+":::::::"+code);
            if(false){
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);
            }
            //3.将验证码存入redis 5分钟过期（为了后续体检预约验证）
            //key:保证唯一 telephone 忘记密码 体检预约 等都有可能发送验证码
            //key :SENDTYPE_ORDER+telephone  SENDTYPE_LOGIN+telephone
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone,5*60,code);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);

    }
}
