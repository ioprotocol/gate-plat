package com.github.app.spi.services.impl;

import com.github.app.spi.dao.domain.Account;
import com.github.app.spi.dao.domain.AccountExample;
import com.github.app.spi.dao.mapper.AccountMapper;
import com.github.app.spi.services.AccountService;
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
    public Account authLogin(String account, String password) {
        if(StringUtils.isEmpty(account))
            return null;
        if(StringUtils.isEmpty(password))
            return null;

        AccountExample example = new AccountExample();
        example.createCriteria().andAccountEqualTo(account).andIsEnableEqualTo((short) 1);
        Account acc = accountMapper.selectOneByExample(example);

        if(acc == null)
            return null;

        if (!MD5Utils.validateMd5WithSalt(password, acc.getPassword())) {
            return null;
        }

        return acc;
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
        example.createCriteria().andRoleIdAndKeyword(roleId, keyword);

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
        example.createCriteria().andRoleIdAndKeyword(roleId, keyword);

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
    public void changeAccountStatus(Integer accountId) {
        Account account = getAccountByAccountId(accountId);
        if (!ObjectUtils.isEmpty(account)) {
            if(account.getIsEnable() == 1) {
                account.setIsEnable((short) 0);
            } else {
                account.setIsEnable((short) 1);
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

    @Override
    public boolean isRoleHasAccount(Integer roleId) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andRoleIdEqualTo(roleId);
        long count = accountMapper.countByExample(accountExample);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void truncate() {
        accountMapper.truncate();
    }

}
