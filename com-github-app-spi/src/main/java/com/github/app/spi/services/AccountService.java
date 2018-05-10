package com.github.app.spi.services;

import com.github.app.spi.dao.domain.Account;

import java.util.List;

public interface AccountService {

    Account authLogin(String account, String password);

    Account getAccountByAccountId(Integer accountId);

    Account getAccountByAccountOrMobileOrEmail(String account, String mobile, String email);

    List<Account> findByKeyWord(Integer roleId, String keyword, Integer offset, Integer rows);

    long countByKeyWord(Integer roleId, String keyword);

    void saveOrUpdate(Account account);

    void changeAccountStatus(Integer accountId);

    void resetPassword(Integer accountId);

    int deleteByAccountId(Integer accountId);

    boolean isRoleHasAccount(Integer roleId);

    void truncate();
}
