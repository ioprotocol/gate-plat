package com.github.app.api.services;

import com.github.app.api.dao.domain.Account;
import com.github.app.api.dao.domain.AccountTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AccountServiceServiceTest extends BaseServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testAll() {
        Account oldEntity = AccountTest.create();
        accountService.saveOrUpdate(oldEntity);

        Account newEntity = accountService.getAccountByAccountOrMobileOrEmail(oldEntity.getAccount(), null, null);

        oldEntity.setAccountId(newEntity.getAccountId());
        oldEntity.setRole(newEntity.getRole());

        isEqual(oldEntity, newEntity);

        Account newEntity1 = accountService.getAccountByAccountOrMobileOrEmail(null, oldEntity.getMobile(), null);
        isEqual(newEntity, newEntity1);

        Account newEntity2 = accountService.getAccountByAccountOrMobileOrEmail(null, null, oldEntity.getEmail());
        isEqual(newEntity, newEntity2);

        List<Account> list1 = accountService.findByKeyWord(oldEntity.getRoleId(), null, null, null);
        Assert.assertTrue(list1.size() > 0);

        List<Account> list2 = accountService.findByKeyWord(null, oldEntity.getAccount(), null, null);
        Assert.assertTrue(list2.size() > 0);

        List<Account> list3 = accountService.findByKeyWord(null, oldEntity.getMobile(), null, null);
        Assert.assertTrue(list3.size() > 0);

        List<Account> list4 = accountService.findByKeyWord(null, oldEntity.getEmail(), null, null);
        Assert.assertTrue(list4.size() > 0);

        Assert.assertTrue(accountService.countByKeyWord(null, oldEntity.getAccount()) == 1);

        accountService.resetPassword(newEntity.getAccountId());

        newEntity = accountService.getAccountByAccountOrMobileOrEmail(oldEntity.getAccount(), null, null);
        newEntity.setPassword(oldEntity.getPassword());
        isEqual(oldEntity, newEntity);

        accountService.deleteByAccountId(newEntity.getAccountId());
    }
}