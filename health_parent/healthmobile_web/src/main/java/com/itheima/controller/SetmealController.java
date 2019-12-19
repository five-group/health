package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端-体检预约控制层
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    /**
     *  套餐列表页面动态展示
     */
    @RequestMapping(value = "/getSetmeal",method = RequestMethod.POST)
    public Result getSetmeal(){
        List<Setmeal> setmealList = null;
        try {
            setmealList = setmealService.getSetmeal();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmealList);
    }

    /**
     * 根据套餐id查询套餐详情数据
     */
    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }
}
