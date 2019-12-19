package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 尽量详细加注释
 * 检查项控制层
 *
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/checkitem") //路径这类CV
public class CheckItemController {
    //调用servie服务
    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }


    /**
     * 分页查询
     */
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 根据检查项id删除检查项
     */
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result delete(Integer id) {
        try {
            checkItemService.delete(id);
        } catch (RuntimeException e) {
            e.printStackTrace();//e.getMessage()  == 当前检查项跟检查组已经关联，无法直接删除
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 根据检查项id查询检查项数据
     *
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result findById(Integer id) {
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }


    /**
     * 修改检查项
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }


    /**
     * 查询所有检查项
     *
     * @return
     */
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result findAll() {
        try {
            List<CheckItem> checkItemList = checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
