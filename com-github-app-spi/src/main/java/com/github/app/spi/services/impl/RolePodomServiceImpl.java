package com.github.app.spi.services.impl;

import com.github.app.spi.dao.domain.*;
import com.github.app.spi.dao.mapper.PopedomMapper;
import com.github.app.spi.dao.mapper.RoleMapper;
import com.github.app.spi.dao.mapper.RolePopedomMapper;
import com.github.app.spi.services.RolePodomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class RolePodomServiceImpl implements RolePodomService {

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RolePopedomMapper rolePopedomMapper;
	@Autowired
	private PopedomMapper popedomMapper;

	@Override
	public Role getRoleById(Integer roleId) {
		return roleMapper.selectByPrimaryKey(roleId);
	}

	@Override
	public void saveOrUpdate(Role role) {
		if (ObjectUtils.isEmpty(role.getRoleId())) {
			roleMapper.insert(role);
		} else {
			roleMapper.updateByPrimaryKey(role);
		}
	}

	@Override
	public void deleteRoleById(Integer roleId) {
		roleMapper.deleteByPrimaryKey(roleId);
	}

	@Override
	public List<Role> list(Integer offset, Integer rows) {
		RoleExample roleExample = new RoleExample();
		roleExample.setOffset(offset);
		roleExample.setRows(rows);
		return roleMapper.selectByExample(roleExample);
	}

	@Override
	public long count() {
		return roleMapper.countByExample(new RoleExample());
	}

	@Override
	public List<Role> listAllRole() {
		return roleMapper.selectByExample(new RoleExample());
	}

	@Override
	public void addRolePopedoms(List<RolePopedom> rolePopedomList) {
		deleteRolePopedomById(rolePopedomList.get(0).getRoleId(), null);
		rolePopedomMapper.batchInsert(rolePopedomList);
	}

	@Override
	public void deleteRolePopedomById(Integer roleId, Integer popedomId) {
		RolePopedomExample example = new RolePopedomExample();
		if (!ObjectUtils.isEmpty(roleId)) {
			example.createCriteria().andRoleIdEqualTo(roleId);
		}
		if (!ObjectUtils.isEmpty(popedomId)) {
			example.createCriteria().andPopedomIdEqualTo(popedomId);
		}
		rolePopedomMapper.deleteByExample(example);
	}

	@Override
	public boolean isAuthOperation(String code, Integer roleId) {
		PopedomExample popedomExample = new PopedomExample();
		popedomExample.createCriteria().andCodeEqualTo(code);

		Popedom popedom = popedomMapper.selectOneByExample(popedomExample);

		if(ObjectUtils.isEmpty(popedom))
			return false;

		RolePopedomExample rolePopedomExample = new RolePopedomExample();
		rolePopedomExample.createCriteria().andPopedomIdEqualTo(popedom.getPopedomId());
		rolePopedomExample.createCriteria().andRoleIdEqualTo(roleId);

		long count = rolePopedomMapper.countByExample(rolePopedomExample);
		if(count == 1)
			return true;

		return false;
	}

	@Override
	public void savePopedom(List<Popedom> list) {
		popedomMapper.batchInsert(list);
	}

	@Override
	public List<Popedom> findPopedomByRoleId(Integer roleId) {
		return popedomMapper.findPopedomByRoleId(roleId);
	}

	@Override
	public List<Popedom> findVuePopedomByRoleId(Integer roleId) {
		return popedomMapper.findVuePopedomByRoleId(roleId);
	}

	@Override
	public List<Popedom> findApiPopedomByRoleId(Integer roleId) {
		return popedomMapper.findApiPopedomByRoleId(roleId);
	}

	@Override
	public List<Popedom> findAllPopedom() {
		return popedomMapper.selectByExample(new PopedomExample());
	}

	@Override
	public void truncate() {
		popedomMapper.truncate();
		rolePopedomMapper.truncate();
		roleMapper.truncate();
	}

}
