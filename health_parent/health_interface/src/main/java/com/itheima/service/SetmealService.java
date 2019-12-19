package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;

/**
 * 套餐业务层接口
 * @author wangxin
 * @version 1.0
 */
public interface SetmealService {
    /**
     * 新增套餐
     * @param setmeal
     * @param ids
     */
    void add(Setmeal setmeal, Integer[] ids);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 套餐列表页面动态展示
     * @return
     */
    List<Setmeal> getSetmeal();

    /*
    根据套餐id查询套餐详情数据
     */
    Setmeal findById(Integer id);
}
