package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * 检查组接口
 * @author wangxin
 * @version 1.0
 */
public interface CheckGroupService {
    /**
     * 新增检查组
     * @param checkGroup
     * @param ids
     */
    void add(CheckGroup checkGroup, Integer[] ids);

    /**
     * 检查组分页查询
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据检查组id 查询检查组对象
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组id查询关联的检查项ids
     * @param groupId
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer groupId);

    /**
     * 编辑检查组
     * @param ids 检查项ids
     * @param checkGroup 检查组对象
     */
    void edit(Integer[] ids, CheckGroup checkGroup);

    /**
     * 查询所有检查组列表数据
     * @return
     */
    List<CheckGroup> findAll();
}
