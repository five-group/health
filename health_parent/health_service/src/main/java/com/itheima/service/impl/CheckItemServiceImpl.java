package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService
{

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 新增检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
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
        Page<CheckItem> checkItemPage = checkItemDao.selectByCondition(queryString);
        return new PageResult(checkItemPage.getTotal(),checkItemPage.getResult());
    }

    /**
     * 根据检查项id删除检查项
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //1.先跟检查项id 到中间表查询 是否已经关联检查组
        //select count(*) from t_checkgroup_checkitem where checkitem_id = 28
        int count = checkItemDao.findCountByCheckItemId(id);
        if(count>0){
            //2.如果有关联，则给用户错误提示  当前检查已经跟检查组关联，无法直接删除
            throw new RuntimeException("当前检查项跟检查组已经关联，无法直接删除");
        }
        //3.如果没有关联 则直接可以删除检查项
        //delete from t_checkitem where id =
        checkItemDao.deleteById(id);
    }

    /**
     * 根据检查项id查询检查项数据
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * 修改检查项
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    /**
     * 查询所有检查项
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
