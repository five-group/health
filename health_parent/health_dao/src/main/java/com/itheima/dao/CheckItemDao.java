package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检查项持久层
 * @author wangxin
 * @version 1.0
 */
public interface CheckItemDao {

    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 先跟检查项id 到中间表查询 是否已经关联检查组
     * @param id
     * @return
     */
    int findCountByCheckItemId(Integer id);

    /**
     * 删除检查项
     * @param id
     */
    void deleteById(Integer id);

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
