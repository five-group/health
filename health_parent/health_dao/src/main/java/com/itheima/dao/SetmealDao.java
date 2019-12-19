package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map; /**
 * 套餐持久层接口
 * @author wangxin
 * @version 1.0
 */
public interface SetmealDao {
    /**
     * 新增套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 设置套餐和检查组关系
     * @param map
     */
    void setMealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> selectByCondition(String queryString);

    /**
     * 套餐列表页面动态展示
     * @return
     */
    List<Setmeal> getSetmeal();

    /**
     * 根据套餐id查询套餐详情数据
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 获取每个套餐对应的预约数量
     * @return
     */
    List<Map> setmealCount();

    /**
     * 热门套餐列表
     * @return
     */
    List<Map> hotSetmeal();
}
