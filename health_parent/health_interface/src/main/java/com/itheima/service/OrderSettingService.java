package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 预约设置-业务逻辑处理层接口
 * @author wangxin
 * @version 1.0
 */
public interface OrderSettingService {
    /**
     * 批量预约设置
     * @param orderSettingList
     */
    void batchAdd(List<OrderSetting> orderSettingList);

    /**
     * 单个预约设置
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 显示日历组件预约数据
     * @param date
     * @return
     */
    List<Map> getOrderSettingByMonth(String date);
}
