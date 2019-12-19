package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 套餐控制层
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;//连接池对象


    /**
     * 上传图片
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(@RequestParam("imgFile") MultipartFile multipartFile){

        try {
            // a.接收前端上传的图片MultipartFile
            // b.原始文件名 修改 为一个新唯一的文件名
            String originalFilename = multipartFile.getOriginalFilename();
            //后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            //uuid新文件名
            String newFileName = UUID.randomUUID().toString()+suffix;
            // c.最终调用工具类上传七牛云 health-itheima81
            QiniuUtils.upload2Qiniu(multipartFile.getBytes(),newFileName);
            // d.将上传成功后新的文件名返回前端
            //sadd:key=setmealPicResources value=文件名
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFileName);
            //文件上传成功 记录
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,newFileName);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }


    public static void main(String[] args) {
        String originalFilename = "aaa.jpg";
        //后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //uuid
        String newFileName = UUID.randomUUID().toString();
        System.out.println(newFileName+suffix);
    }


    /**
     * 新增套餐
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody Setmeal setmeal, Integer[] ids){
        try {
            setmealService.add(setmeal,ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        //记录新增套后 redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }
}
