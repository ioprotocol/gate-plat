package com.github.app.spi.config;

import com.github.app.spi.plugin.SqlPerformanceMonitor;
import com.github.app.spi.utils.AppServerConfigLoader;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.github.app.spi")
@EnableTransactionManagement
@MapperScan("com.github.app.spi.dao.mapper")
public class SpringApplication {


    @Bean(name = "dataSource")
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        AppServerConfig config = AppServerConfigLoader.getServerCfg();
        try {
            dataSource.setDriverClass(config.getDataSourceDriverClassName());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        dataSource.setJdbcUrl(config.getDataSourceUrl());
        dataSource.setUser(config.getDataSourceUserName());
        dataSource.setPassword(config.getDataSourcePassword());
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource());
        return dataSourceTransactionManager;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource());

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(Log4j2Impl.class);
        configuration.setSafeRowBoundsEnabled(true);
        AppServerConfig config = AppServerConfigLoader.getServerCfg();
        if (config.isSlowSqlMonitorEnabled()) {
            // add sql monitor plugin
            configuration.addInterceptor(new SqlPerformanceMonitor(config.getSlowSqlTimeInMs()));
        }

        bean.setConfiguration(configuration);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Autowired
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
