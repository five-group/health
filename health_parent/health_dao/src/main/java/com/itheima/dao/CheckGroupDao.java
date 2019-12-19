package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;
import java.util.Map; /**
 * 检查组持久层
 * @author wangxin
 * @version 1.0
 */
public interface CheckGroupDao {
    /**
     * 新增检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 设置检查组和检查项关系
     * @param map
     */
    void setCheckGroupAndCheckItem(Map map);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<CheckGroup> selectByCondition(String queryString);

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
     * 修改检查组对象
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    /**
     * 根据检查组id删除已经关联的检查项ids
     * @param groupId
     */
    void deleteByCheckGroupId(Integer groupId);

    /**
     * 查询所有检查组列表数据
     * @return
     */
    List<CheckGroup> findAll();
}
