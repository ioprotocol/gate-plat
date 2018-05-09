package com.github.app.api.services;

import com.github.app.api.dao.domain.Account;
import com.github.app.api.dao.domain.Popedom;
import com.github.app.api.dao.domain.Role;
import com.github.app.api.dao.domain.RolePopedom;
import com.github.app.api.handler.UriHandler;
import com.github.app.api.utils.AppContext;
import com.github.app.utils.ClassUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SystemInitServiceTest extends BaseServiceTest {

	@Autowired
	private AccountService accountService;
	@Autowired
	private LogService logService;
	@Autowired
	private RolePodomService rolePodomService;

	@Test
	public void systemInit() {
	    // 清表
		clearDB();
		// 创建管理员角色
		createAdminRole();
		// 创建管理员帐号
		createAdminAccount();
		// 扫描代码，生成接口权限被控资源
		genMenuMetaData();
		// 授予超级管理员所有操作权限
		genAdminAuthData();
	}

	public void clearDB() {
		logService.truncate();
		accountService.truncate();
		rolePodomService.truncate();
	}

	public void createAdminRole() {
		Role role = new Role();
		role.setName("超级管理员");
		rolePodomService.saveOrUpdate(role);
	}

	public void createAdminAccount() {

		Account account = new Account();
		account.setAccount("administrator");
		account.setPassword("123456");
		account.setRoleId(1);
		account.setName("超级管理员");
		account.setSex("男");
		account.setEmail("xsy870712@163.com");
		account.setMobile("15315086265");
		account.setIsEnable((short) 1);

		accountService.saveOrUpdate(account);
	}

	/**
	 * 生成菜单元数据
	 */
	public void genMenuMetaData() {
		String packageName = "com.github.app.api.handler.api";
		try {
			List<Popedom> list = new ArrayList<>();

			List<Class<?>> uriHandlers = ClassUtil.getClasses(packageName);
			for (Class<?> cls : uriHandlers) {
				try {
					Object bean = AppContext.getContext().getBean(cls);
					if (bean instanceof UriHandler) {
						UriHandler uriHandler = (UriHandler) bean;
						uriHandler.registePopedom(list);
					}
				} catch (Exception e) {
				}
			}

			rolePodomService.savePopedom(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给超级管理员授予全部权限
	 */
	public void genAdminAuthData() {
		List<Popedom> list = rolePodomService.findAllPopedom();
		List<RolePopedom> popedoms = new ArrayList<>();
		for (Popedom popedom : list) {
			RolePopedom rolePopedom = new RolePopedom();
			rolePopedom.setPopedomId(popedom.getPopedomId());
			rolePopedom.setRoleId(1);
			popedoms.add(rolePopedom);
		}

		rolePodomService.addRolePopedoms(popedoms);
	}
}
