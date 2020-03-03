package com.revolut.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.revolut.controller.AccountTransactionController;
import com.revolut.controller.AccountTransactionControllerImpl;
import com.revolut.controller.MoneyTransferCommandController;
import com.revolut.controller.MoneyTransferCommandControllerImpl;
import com.revolut.processor.*;
import com.revolut.repository.AccountRepository;
import com.revolut.repository.AccountRepositoryImpl;
import com.revolut.repository.AccountTransactionRepository;
import com.revolut.repository.AccountTransactionRepositoryImpl;
import com.revolut.util.JsonParser;
import com.revolut.util.JsonParserImpl;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AppModule extends AbstractModule {
    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<EntityManager>();

    @Override
    protected void configure() {
        bind(MoneyTransferCommandController.class).to(MoneyTransferCommandControllerImpl.class);
        bind(AccountTransactionController.class).to(AccountTransactionControllerImpl.class);
        bind(AccountRepository.class).to(AccountRepositoryImpl.class);
        bind(AccountTransactionRepository.class).to(AccountTransactionRepositoryImpl.class);
        bind(JsonParser.class).to(JsonParserImpl.class);
        bind(AccountProcessor.class).to(AccountProcessorImpl.class);
        bind(AccountOperationValidator.class).to(AccountOperationValidatorImpl.class);
        bind(AccountTransactionProcessor.class).to(AccountTransactionProcessorImpl.class);
    }
    @Provides
    @Singleton
    public EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("db-manager");
    }

    @Provides
    @Singleton
    public EntityManager createEntityManager(
            EntityManagerFactory entityManagerFactory) {
        return
        entityManagerFactory.createEntityManager();

    }
}
