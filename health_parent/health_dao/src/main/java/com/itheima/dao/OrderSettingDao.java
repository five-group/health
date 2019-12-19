package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map; /**
 * 预约设置持久层
 * @author wangxin
 * @version 1.0
 */
public interface OrderSettingDao {
    /**
     * 预约设置
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据预约日期查询是否已经预约设置
     * @param orderDate
     * @return
     */
    int findCountByOrderDate(Date orderDate);

    /**
     * 根据预约日期更新预约数量
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 根据起始时间和结束时间查询 预约数据
     * @param map
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map map);

    OrderSetting findByOrderDate(Date date);



    /**
     * 根据预约日期更新预约数量
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
