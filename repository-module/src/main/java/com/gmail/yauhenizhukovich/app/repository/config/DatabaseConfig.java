package com.gmail.yauhenizhukovich.app.repository.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import static com.gmail.yauhenizhukovich.app.repository.constant.DatabaseConstant.ENTITY_PACKAGE;

@Configuration
public class DatabaseConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource);
        entityManager.setPackagesToScan(ENTITY_PACKAGE);
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return entityManager;
    }

}
