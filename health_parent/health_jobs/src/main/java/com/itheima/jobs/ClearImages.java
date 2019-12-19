package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 清理垃圾图片任务类
 * @author wangxin
 * @version 1.0
 */
public class ClearImages {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 清理七牛云垃圾图片以及redis记录
     */
    public void delQiNiuImage(){
        //1.获取两个集合差值
        //Set<String>
        Set<String> fileNames = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //2.遍历集合，
        if(fileNames != null && fileNames.size()>0){
            for (String fileName : fileNames) {
                System.out.println("清理垃圾图片中"+fileName);
                //循环调用七牛云删除图片方法，删除垃圾图片
                QiniuUtils.deleteFileFromQiniu(fileName);
                //3.删除setmealPicResources中垃圾图片记录
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            }
        }
    }
}
