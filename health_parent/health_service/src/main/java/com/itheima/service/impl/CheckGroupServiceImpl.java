package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组业务层
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组
     * @param checkGroup 检查组
     * @param checkItemIds  检查项ids
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        //1.保存检查组
        checkGroupDao.add(checkGroup);
        //2.获取检查组id
        Integer groupId = checkGroup.getId();
        //3.设置检查组和检查组关系(单独抽取一个方法-->编辑检查组也需要用到此方法)
        this.setCheckGroupAndCheckItem(groupId,checkItemIds);
    }

    /**
     * 公共方法 （新增检查组 或 编辑检查组）
     * @param groupId
     * @param checkItemIds
     */
    public void setCheckGroupAndCheckItem(Integer groupId,Integer[] checkItemIds){
        if(checkItemIds != null && checkItemIds.length>0){
            //循环遍历得到检查项checkItemId
            Map<String,Integer> map = new HashMap<>();
            for (Integer checkItemId : checkItemIds) {
                //设置检查组和检查项中间表关系
                map.put("groupId",groupId);
                map.put("checkItemId",checkItemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
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
        Page<CheckGroup> checkGroupPage = checkGroupDao.selectByCondition(queryString);
        return new PageResult(checkGroupPage.getTotal(),checkGroupPage.getResult());
    }

    /**
     * 根据检查组id 查询检查组对象
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组id查询关联的检查项ids
     * @param groupId
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer groupId) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(groupId);
    }

    /**
     * 编辑检查组
     * @param ids 检查项ids
     * @param checkGroup 检查组对象
     */
    @Override
    public void edit(Integer[] ids, CheckGroup checkGroup) {
        Integer groupId = checkGroup.getId();
        // 1.修改检查组对象
        checkGroupDao.edit(checkGroup);
        // 2.根据检查组id删除已经关联的检查项ids
        checkGroupDao.deleteByCheckGroupId(groupId);
        // 3.重新建立检查组和检查项关系
        this.setCheckGroupAndCheckItem(groupId,ids);
    }

    /**
     * 查询所有检查组列表数据
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
