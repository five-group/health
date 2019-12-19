package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约设置-业务逻辑处理层实现类
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量预约设置
     * @param orderSettingList
     */
    @Override
    public void batchAdd(List<OrderSetting> orderSettingList) {
        if(orderSettingList != null && orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                //建议：业务逻辑 如果预约时间<=当前时间 不能往数据库插入了
                //建议：如果预约记录已经存在 将要修改的预约数量 > 已经预约数量
                //1.插入数据库之前 判断当前预约日期 是否存在记录
                //条件 预约日期
                editNumberByOrderDate(orderSetting);
            }
        }
    }

    /**
     * 根据预约日期修改预约人数 ctrl+alt+m
     * @param orderSetting
     */
    public void editNumberByOrderDate(OrderSetting orderSetting) {
        int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count == 0){
            //2.不存在
            orderSettingDao.add(orderSetting);
        }
        else {
            //3.存在 则更新
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }
    }

    /**
     * 显示日历组件预约数据
     * @param date 2019-12
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //2019-08
        //方式一：模糊查询 select * from t_ordersetting where orderdate like '2019-08%'
        //方式二：select * from t_ordersetting where orderdate between ‘2019-08-01’ and '2019-08-31'

        String beginDate = date+"-01";
        String endDate = date+"-31";
        Map map = new HashMap();
        map.put("beginDate",beginDate);
        map.put("endDate",endDate);
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> rsMap = new ArrayList<>();
        if(orderSettingList != null && orderSettingList.size()>0){
            //将orderSettingList转为List<Map>
            for (OrderSetting orderSetting : orderSettingList) {
                //{ date: 1, number: 120, reservations: 1 }
                Map oMap = new HashMap();
                oMap.put("date",orderSetting.getOrderDate().getDate());//获取几号
                oMap.put("number",orderSetting.getNumber());//获取可预约人数
                oMap.put("reservations",orderSetting.getReservations());//获取已经预约人数
                rsMap.add(oMap);
            }
        }
        return rsMap;
    }

}
