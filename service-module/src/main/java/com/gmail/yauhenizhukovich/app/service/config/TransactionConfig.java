package com.gmail.yauhenizhukovich.app.service.config;

import java.lang.invoke.MethodHandles;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
public class TransactionConfig {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        logger.debug("Transaction manager manager bean initialized and ready to use.");
        return transactionManager;
    }

}
