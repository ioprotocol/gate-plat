package com.github.app.spi.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Properties;

@Intercepts(value = {
    @Signature(type = Executor.class, method = "update",
        args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
            CacheKey.class, BoundSql.class}),
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class SqlPerformanceMonitor implements Interceptor {
  private static Logger logger = LogManager.getLogger(SqlPerformanceMonitor.class);

  private long slowTimeInMs = 3000;

  public SqlPerformanceMonitor(long slowTimeInMs) {
    this.slowTimeInMs = slowTimeInMs;
  }

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    long start = System.currentTimeMillis();
    Object returnValue = invocation.proceed();
    long end = System.currentTimeMillis();

    if (end - start > slowTimeInMs) {
      Object[] args = invocation.getArgs();
      MappedStatement ms = (MappedStatement) args[0];
      Object p = args[1];
      String boundSql = ms.getBoundSql(p).getSql().trim();

      logger.warn("exhaust:{} ms, sql:{}, args:{}", end - start, boundSql, Arrays.toString(args));
    }
    return returnValue;
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {

  }
}
