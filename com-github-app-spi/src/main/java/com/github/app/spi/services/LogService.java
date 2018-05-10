package com.github.app.spi.services;

import com.github.app.spi.dao.domain.Account;
import com.github.app.spi.dao.domain.Log;

import java.util.List;

public interface LogService {

    void addLog(Account account, String code, String remark);

    void addLog(String account, String code, String remark);

    void addLog(Log log);

    void addLog(List<Log> logs);

    List<Log> find(String code, String account, Long startTime, Long endTime, Integer offset, Integer rows);

    long count(String code, String account, Long startTime, Long endTime);

    void truncate();
}
