package com.itheima;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 任务测试类
 * @author wangxin
 * @version 1.0
 */
public class JobTest {
    public static void main(String[] args) {
        //工程启动加载配置
        new ClassPathXmlApplicationContext("classpath:applicationContext-jobs.xml");
    }
}
