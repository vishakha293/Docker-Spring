package com.main.DockerLearning.Configurations;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DataSourceConfig {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.jpa.database-platform}")
    private String hibernateDialect;

    @Bean
    public DataSource dataSource() {
        log.info("Initializing DataSource with URL: {}", url);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);

        log.info("DataSource initialized successfully with Driver: {}", driverClassName);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        log.info("Setting up EntityManagerFactory");

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.main.DockerLearning"); // Update with your package name
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", hibernateDialect);
        jpaProperties.put("hibernate.hbm2ddl.auto", "update"); // Use "create", "update", or "validate" based on your requirements
        factoryBean.setJpaProperties(jpaProperties);

        log.info("EntityManagerFactory setup completed");
        return factoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        log.info("Configuring TransactionManager");
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        log.info("TransactionManager configured successfully");
        return transactionManager;
    }

}
