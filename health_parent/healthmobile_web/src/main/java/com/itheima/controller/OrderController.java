package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 体检预约控制层
 *
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    /**
     * 体检预约
     * {"setmealId":"13","sex":"1","orderDate":"2019-12-14",
     * "name":"11","telephone":"15915439292","validateCode":"1234","idCard":"512501197203035172"}
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Result submit(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");//用户手机号码
        String validateCode = (String) map.get("validateCode");//用户输入的验证码
        //格式校验
        if (StringUtils.isEmpty(telephone) || StringUtils.isEmpty(validateCode)) {
            return new Result(false, MessageConstant.PARM_ERROR);
        }
        //校验验证码
        //1.从redis中取出redis中验证码
        String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);
        if (StringUtils.isEmpty(redisCode)) {
            return new Result(false, MessageConstant.PARM_ERROR);
        }
        //2.对比
        if (!validateCode.equals(redisCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        //3.调用service服务 最终得到预约成功还是失败
        try {
            //设置预约来源类型
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result rs = orderService.submitOrder(map);
            //4.预约结果，如果成功还需发送预约成功短信
            if (rs.isFlag()) {
                Order order = (Order) rs.getData();
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, order.getOrderDate().toString());
                } catch (ClientException e) {
                    e.printStackTrace();
                }
                //预约成功后将验证码删除
            }
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SYSTEM_ERROR);
        }

    }

    /**
     * 体检预约成功页面数据查询
     */
    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    public Result findById4Detail(Integer id){
        Map map = orderService.findById4Detail(id);
        return new Result(true,MessageConstant.ORDER_SUCCESS,map);
    }
}
