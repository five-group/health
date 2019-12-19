package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 新增套餐
     * @param setmeal
     * @param checkGroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        //1.保存套餐
        setmealDao.add(setmeal);
        //2.获取套餐id
        Integer setmealId = setmeal.getId();
        //3.设置套餐和检查组关系
        this.setMealAndCheckGroup(setmealId,checkGroupIds);

    }

    /**
     * 分页查询
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //第一步：设置分页参数
        PageHelper.startPage(currentPage,pageSize);//当前页码 每页显示记录数
        //第二步：需要分页表查询语句 有条件的需要带条件
        Page<Setmeal> setmealPage = setmealDao.selectByCondition(queryString);
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }

    /**
     * 套餐列表页面动态展示
     * @return
     */
    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    /**
     * 根据套餐id查询套餐详情数据
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }


    /**
     * 公共方法
     * @param setmealId
     * @param checkGroupIds
     */
    public void setMealAndCheckGroup(Integer setmealId,Integer[] checkGroupIds){
        if(checkGroupIds != null && checkGroupIds.length>0){
            //循环遍历得到检查项checkGroupId
            Map<String,Integer> map = new HashMap<>();
            for (Integer checkGroupId : checkGroupIds) {
                //设置检查组和检查项中间表关系
                map.put("setmealId",setmealId);
                map.put("checkGroupId",checkGroupId);
                setmealDao.setMealAndCheckGroup(map);
            }
        }
    }
}
