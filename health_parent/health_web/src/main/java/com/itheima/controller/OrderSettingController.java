package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置控制层
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量预约设置（文件上传）
     * 1.预约管理-预约设置-点击"模板下载"
     * 2.在模板中输入预约日期和可预约数量
     * 3.点击“上传文件”将模板上传后台
     * 4.后台控制层MultipartFile接收
     * 5.调用POIUtils解析Excel返回List<String[]>
     * 6.List<String[]>==>List<OrderSetting>
     * 7.调用service服务存入数据库
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(MultipartFile excelFile){
       //System.out.println(excelFile.getOriginalFilename());
        try {
            //将excel对象解析得到list集合
            List<String[]> listRow = POIUtils.readExcel(excelFile);
            if(listRow != null && listRow.size()>0){
                //List<String[]>==>List<OrderSetting>
                List<OrderSetting> orderSettingList = new ArrayList<>();
                //row:Excel每一行数据
                for (String[] row : listRow) {
                    String orderDate = row[0];
                    String number =  row[1];
                    OrderSetting orderSetting = new OrderSetting(new Date(orderDate),Integer.parseInt(number));
                    orderSettingList.add(orderSetting);
                }
                //调用service服务存入数据库
                orderSettingService.batchAdd(orderSettingList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    /**
     * 单个预约设置
     * @param orderSetting
     * @return
     */
    @RequestMapping(value = "/editNumberByDate",method = RequestMethod.POST)
    public Result editNumberByOrderDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByOrderDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);

    }

    /**
     * 显示日历组件预约数据
     */
    @RequestMapping(value = "/getOrderSettingByMonth",method = RequestMethod.GET)
    public Result getOrderSettingByMonth(String date){
        List<Map> list = null;
        try {
            list = orderSettingService.getOrderSettingByMonth(date);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_ORDERSETTING_FAIL);
        }
        //第一种方式Result
        //第二种方式List<Map>
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
    }
}
