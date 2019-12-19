package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @author wangxin
 * @version 1.0
 */
public interface CheckItemService {
    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);


    /**
     * 检查项分页查询
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据检查项id删除检查项
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据检查项id查询检查项数据
     * @param id
     * @return
     */
    CheckItem findById(Integer id);

    /**
     * 修改检查项
     * @param checkItem
     */
    void edit(CheckItem checkItem);

    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();
}
