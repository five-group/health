package com.itheima.jobs;

import java.util.Date;

/**
 * 定义任务类
 * @author wangxin
 * @version 1.0
 */
public class HelloService {
    /**
     * 定时任务方法
     */
    public void run(){
        System.out.println("hello quartz...."+new Date());
    }
}
