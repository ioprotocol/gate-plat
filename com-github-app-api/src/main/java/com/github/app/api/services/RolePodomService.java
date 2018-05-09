package com.github.app.api.services;

import com.github.app.api.dao.domain.Popedom;
import com.github.app.api.dao.domain.Role;
import com.github.app.api.dao.domain.RolePopedom;

import java.util.List;

public interface RolePodomService {

	/**
	 * 角色相关操作
	 *
	 * @param roleId
	 * @return
	 */
	Role getRoleById(Integer roleId);

	void saveOrUpdate(Role role);

	void deleteRoleById(Integer roleId);

	List<Role> list(Integer offset, Integer rows);

	long count();

	List<Role> listAllRole();

	/**
	 * 对系统某个角色赋予一组权限
	 *
	 * @param rolePopedomList
	 */
	void addRolePopedoms(List<RolePopedom> rolePopedomList);

	/**
	 * 删除某角色或者某已资源的权限赋值记录
	 *
	 * @param roleId
	 * @param popedomId
	 */
	void deleteRolePopedomById(Integer roleId, Integer popedomId);

	/**
	 * 验证角色是否对某项资源有操作权限
	 *
	 * @param code
	 * @param roleId
	 * @return
	 */
	boolean isAuthOperation(String code, Integer roleId);

	/**
	 *
	 * @param list
	 */
	void savePopedom(List<Popedom> list);

	/**
	 * 查询角色拥有的所有系统操作权限
	 *
	 * @param roleId
	 * @return
	 */
	List<Popedom> findPopedomByRoleId(Integer roleId);

	/**
	 * 查询vue前端用到的权限集合
	 * 
	 * @param roleId
	 * @return
	 */
	List<Popedom> findVuePopedomByRoleId(Integer roleId);

	/**
	 * 查询接口权限集合
	 * 
	 * @param roleId
	 * @return
	 */
	List<Popedom> findApiPopedomByRoleId(Integer roleId);

	List<Popedom> findAllPopedom();

	/**
	 * 清空角色/权限/权限分配三个表中的所有内容
	 */
	void truncate();
}
