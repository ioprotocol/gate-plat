package com.github.app.api.services.impl;

import com.github.app.api.dao.domain.Account;
import com.github.app.api.dao.domain.Log;
import com.github.app.api.dao.domain.LogExample;
import com.github.app.api.dao.mapper.LogMapper;
import com.github.app.api.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LogServiceImpl implements LogService {
    private static AtomicInteger logIdRandom = new AtomicInteger();

    @Autowired
    private LogMapper logMapper;

    @Override
    public void addLog(Account account, String code, String remark) {
        Log log = new Log();
        log.setLogId(System.currentTimeMillis() * 1000 + logIdRandom.getAndIncrement() % 1000);
        if(logIdRandom.get() > Integer.MAX_VALUE)
            logIdRandom.set(0);
        log.setRoleName(account.getRole().getName());
        log.setRoleId(account.getRoleId());
        log.setOpAccount(account.getAccount());
        log.setOpName(account.getName());
        log.setCode(code);
        log.setDescription(remark);
        addLog(log);
    }

    @Override
    public void addLog(String account, String code, String remark) {
        Log log = new Log();
        log.setLogId(System.currentTimeMillis() * 1000 + logIdRandom.getAndIncrement() % 1000);
        if(logIdRandom.get() > Integer.MAX_VALUE)
            logIdRandom.set(0);
        log.setRoleName(null);
        log.setRoleId(null);
        log.setOpAccount(account);
        log.setOpName(account);
        log.setCode(code);
        log.setDescription(remark);
        addLog(log);
    }

    @Override
    public void addLog(Log log) {
        logMapper.insert(log);
    }

    @Override
    public void addLog(List<Log> logs) {
        logMapper.batchInsert(logs);
    }

    @Override
    public List<Log> find(String code, String account, Long startTime, Long endTime, Integer offset, Integer rows) {
        LogExample example = new LogExample();
        if (!StringUtils.isEmpty(code)) {
            example.createCriteria().andCodeEqualTo(code);
        }
        if (!StringUtils.isEmpty(account)) {
            example.createCriteria().andOpAccountEqualTo(account);
        }

        if (!ObjectUtils.isEmpty(startTime)) {
            example.createCriteria().andLogIdGreaterThan(startTime * 1000);
        }
        if (!ObjectUtils.isEmpty(endTime)) {
            example.createCriteria().andLogIdLessThan(endTime * 1000);
        }

        if (ObjectUtils.isEmpty(offset)) {
            offset = Integer.valueOf(0);
        }
        if (ObjectUtils.isEmpty(rows)) {
            rows = Integer.valueOf(20);
        }

        example.setOffset(offset);
        example.setRows(rows);
        example.setOrderByClause(" log_id desc ");
        return logMapper.selectByExample(example);
    }

    @Override
    public long count(String code, String account, Long startTime, Long endTime) {
        LogExample example = new LogExample();
        if (!StringUtils.isEmpty(code)) {
            example.createCriteria().andCodeEqualTo(code);
        }
        if (!StringUtils.isEmpty(account)) {
            example.createCriteria().andOpAccountEqualTo(account);
        }
        if (!ObjectUtils.isEmpty(startTime)) {
            example.createCriteria().andLogIdGreaterThan(startTime * 1000);
        }
        if (!ObjectUtils.isEmpty(endTime)) {
            example.createCriteria().andLogIdLessThan(endTime * 1000);
        }
        return logMapper.countByExample(example);
    }

    @Override
    public void truncate() {
        logMapper.truncate();
    }
}
