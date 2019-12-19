package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 体检预约
     * {"setmealId":"13","sex":"1","orderDate":"2019-12-14","name":"11",
     * "telephone":"15915439292","validateCode":"1234","idCard":"512501197203035172"}
     * @param map
     * @return
     */
    @Override
    public Result submitOrder(Map map) throws Exception {
        String orderDateStr = (String)map.get("orderDate");//预约日期
        String telephone = (String) map.get("telephone");//手机号码
        String name = (String) map.get("name");//姓名
        String sex = (String) map.get("sex");//性别
        String idCard = (String) map.get("idCard");//身份证
        String setmealId = (String) map.get("setmealId");//套餐id
        String orderType = (String) map.get("orderType");//预约类型

        Date orderDate = DateUtils.parseString2Date(orderDateStr);
        //1.检查预约日期 DateUtils.parseString2Date(orderDateStr)
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if(orderSetting == null){
            //无法预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2.检查预约日期是否约满
        int number = orderSetting.getNumber();//可预约人数 100
        int reservations = orderSetting.getReservations();//已经预约人数 101
        if(reservations>=number){
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //3.查询是否 是会员
        Member member = memberDao.findByTelephone(telephone);
        if(member != null){
            //查询当前会员是否重复预约 会员id+预约日期+套餐id
            Order order = new Order();
            order.setMemberId(member.getId());//会员id
            order.setOrderDate(orderDate);//预约日期
            order.setSetmealId(Integer.parseInt(setmealId));//套餐id设置
            List<Order> listOrder = orderDao.findByCondition(order);//是否重复预约查询
            if(listOrder!=null && listOrder.size()>0){
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        else
        {
            //4.自动创建会员
            member = new Member();//创建会员对象
            member.setName(name);//姓名
            member.setSex(sex);//性别
            member.setIdCard(idCard);//身份证
            member.setPhoneNumber(telephone);//手机号码
            member.setRegTime(new Date());//注册时间
            memberDao.add(member);
        }
        System.out.println("会员id"+member.getId());
        //5.往预约表 插入预约记录
        Order dbOrder = new Order();
        dbOrder.setMemberId(member.getId());//会员id
        dbOrder.setOrderDate(orderDate);//预约日期
        dbOrder.setOrderType(orderType);//预约类型 电话预约 或 微信预约
        dbOrder.setOrderStatus(Order.ORDERSTATUS_NO);//预约状态默认：未到诊
        dbOrder.setSetmealId(Integer.parseInt(setmealId));
        orderDao.add(dbOrder);
        //6.预约设置表 预约人数+1
        orderSetting.setReservations(orderSetting.getReservations()+1);//人数+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS,dbOrder);
    }

    /**
     * 体检预约成功页面数据查询
     * @param id
     * @return
     */
    @Override
    public Map findById4Detail(Integer id) {
        return orderDao.findById4Detail(id);
    }
}
