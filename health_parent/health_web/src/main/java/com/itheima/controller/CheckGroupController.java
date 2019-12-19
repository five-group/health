package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查组控制层
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 新增检查组
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] ids){
        try {
            checkGroupService.add(checkGroup,ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 根据检查组id查询检查组数据
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
    /**
     * 根据检查组id查询关联的检查项ids
     */
    @RequestMapping(value = "/findCheckItemIdsByCheckGroupId",method = RequestMethod.GET)
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer groupId){

        List<Integer> integerList = null;
        try {
            integerList = checkGroupService.findCheckItemIdsByCheckGroupId(groupId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return integerList;
    }
    /**
     * 编辑检查组数据
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(Integer[] ids,@RequestBody CheckGroup checkGroup){
        try {
            //把sex值 0 1 2 抛出统一异常（参数异常）
            checkGroupService.edit(ids,checkGroup);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 查询所有检查组列表数据
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){
        try {
            List<CheckGroup> checkGroupList =  checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
