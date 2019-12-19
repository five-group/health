package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

/**
 * 权限持久层接口
 * @author wangxin
 * @version 1.0
 */
public interface PermissionDao {
    /**
     * 根据角色id查询权限列表
     * @param roleId
     * @return
     */
    Set<Permission> findByRoleId(Integer roleId);
}
