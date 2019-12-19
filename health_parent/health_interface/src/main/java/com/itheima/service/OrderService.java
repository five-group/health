package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map; /**
 * 体检预约接口服务
 * @author wangxin
 * @version 1.0
 */
public interface OrderService {
    /**
     * 体检预约请求
     * @param map
     * @return
     */
    Result submitOrder(Map map) throws Exception;

    /**
     * 体检预约成功页面数据查询
     * @param id
     * @return
     */
    Map findById4Detail(Integer id);
}
