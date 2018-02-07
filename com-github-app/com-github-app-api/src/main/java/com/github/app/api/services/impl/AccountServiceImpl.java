package com.github.app.api.services.impl;

import com.github.app.api.dao.domain.Account;
import com.github.app.api.dao.domain.AccountExample;
import com.github.app.api.dao.mapper.AccountMapper;
import com.github.app.api.services.AccountService;
import com.github.app.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public boolean authLogin(String account, String password) {
        if(StringUtils.isEmpty(account))
            return false;
        if(StringUtils.isEmpty(password))
            return false;

        AccountExample example = new AccountExample();
        example.createCriteria().andAccountEqualTo(account).andIsEnableEqualTo((short) 1);
        Account acc = accountMapper.selectOneByExample(example);

        if(acc == null)
            return false;

        if (!MD5Utils.validateMd5WithSalt(password, acc.getPassword())) {
            return false;
        }

        return true;
    }

    @Override
    public Account getAccountByAccountId(Integer accountId) {
        return accountMapper.selectByPrimaryKey(accountId);
    }

    @Override
    public Account getAccountByAccountOrMobileOrEmail(String account, String mobile, String email) {
        AccountExample example = new AccountExample();
        if (!StringUtils.isEmpty(account)) {
            example.or().andAccountEqualTo(account);
        }
        if (!StringUtils.isEmpty(mobile)) {
            example.or().andMobileEqualTo(mobile);
        }
        if (!StringUtils.isEmpty(email)) {
            example.or().andEmailEqualTo(email);
        }
        return accountMapper.selectOneByExample(example);
    }

    @Override
    public List<Account> findByKeyWord(Integer roleId, String keyword, Integer offset, Integer rows) {
        AccountExample example = new AccountExample();
        if (!ObjectUtils.isEmpty(roleId)) {
            example.or().andRoleIdEqualTo(roleId);
        }
        if (!ObjectUtils.isEmpty(keyword)) {
            example.or().andAccountLike(keyword);
            example.or().andMobileLike(keyword);
            example.or().andEmailLike(keyword);
        }

        if (!ObjectUtils.isEmpty(offset)) {
            example.setOffset(offset);
        }
        if (!ObjectUtils.isEmpty(rows)) {
            example.setRows(rows);
        }

        return accountMapper.selectByExample(example);
    }

    @Override
    public long countByKeyWord(Integer roleId, String keyword) {
        AccountExample example = new AccountExample();
        if (!ObjectUtils.isEmpty(roleId)) {
            example.or().andRoleIdEqualTo(roleId);
        }
        if (!ObjectUtils.isEmpty(keyword)) {
            example.or().andAccountLike(keyword);
            example.or().andMobileLike(keyword);
            example.or().andEmailLike(keyword);
        }

        return accountMapper.countByExample(example);
    }

    @Override
    public void saveOrUpdate(Account account) {
        if (account.getAccountId() == null) {
            /**
             * if account is empty, set with mobile or email
             */
            if (StringUtils.isEmpty(account.getAccount())) {
                if (StringUtils.isEmpty(account.getMobile()) && StringUtils.isEmpty(account.getEmail())) {
                    throw new RuntimeException("account mobile and email must fill one");
                }
                account.setAccount(account.getMobile());
                if (StringUtils.isEmpty(account.getAccount())) {
                    account.setAccount(account.getEmail());
                }
            }

            /**
             * if password is null, set default password value with 123456
             */
            if (StringUtils.isEmpty(account.getPassword())) {
                account.setPassword(MD5Utils.md5WithSalt(account.getAccount()));
            } else {
                account.setPassword(MD5Utils.md5WithSalt(account.getPassword()));
            }

            accountMapper.insert(account);
        } else {
            Account dbAccount = accountMapper.selectByPrimaryKey(account.getAccountId());
            if (dbAccount == null) {
                throw new RuntimeException(String.format("accountId %d not exist", account.getAccountId()));
            }

            // set a new password, must encrypt pwd again.
            if (!dbAccount.getPassword().equalsIgnoreCase(account.getPassword())) {
                account.setPassword(MD5Utils.md5WithSalt(account.getPassword()));
            }

            accountMapper.updateByPrimaryKey(account);
        }
    }

    @Override
    public void resetPassword(Integer accountId) {
        Account account = getAccountByAccountId(accountId);
        if (!ObjectUtils.isEmpty(account)) {
            account.setPassword(MD5Utils.md5WithSalt(account.getAccount()));
            accountMapper.updateByPrimaryKey(account);
        }
    }

    @Override
    public int deleteByAccountId(Integer accountId) {
        return accountMapper.deleteByPrimaryKey(accountId);
    }

}
