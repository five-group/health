package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

/**
 * 角色持久层接口
 * @author wangxin
 * @version 1.0
 */
public interface RoleDao {
    /**
     * 根据用户id查询角色列表
     * @param userId
     * @return
     */
    Set<Role> findByUserId(Integer userId);
}
