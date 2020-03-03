package com.revolut;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revolut.controller.AccountTransactionController;
import com.revolut.controller.MoneyTransferCommandController;
import com.revolut.module.AppModule;
import com.revolut.exception.AccountOperationProccessingException;
import com.revolut.repository.AccountRepository;
import com.revolut.response.StandardResponse;
import com.revolut.util.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.persistence.EntityManager;

import java.io.*;
import java.net.URL;

import static spark.Spark.*;

public class RevolutMoneyTransferApp {
    private static Logger logger = LoggerFactory.getLogger(RevolutMoneyTransferApp.class);

    public static void main(String args[]) {

        Injector injector = Guice.createInjector(new AppModule());
        JsonParser jsonParser = injector.getInstance(JsonParser.class);
        MoneyTransferCommandController moneyTransferCommandController = injector.getInstance(MoneyTransferCommandController.class);
        AccountTransactionController accountTransactionController = injector.getInstance(AccountTransactionController.class);
        EntityManager entityManager = injector.getInstance(EntityManager.class);

        initDatabaseSetup(entityManager);

        get("/Transfer/:id", (req, res) -> {
            StandardResponse response = accountTransactionController.listAllTransaction(req, res);

            return jsonParser.toJsonFromPojo(response);
        });

        post("/Transfer", (req, res) -> {
            StandardResponse response = moneyTransferCommandController.doMoneyTransfer(req, res);
            return jsonParser.toJsonFromPojo(response);
        });
        exception(Exception.class, (exception, request, response) -> {
            response.status(400);
            response.body(exception.getMessage());
        });
    }

    private static void initDatabaseSetup(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        entityManager.createNativeQuery("DROP TABLE IF EXISTS Account;").executeUpdate();
        entityManager.createNativeQuery("CREATE TABLE Account (id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,name VARCHAR(30),balance DECIMAL(19,4),currency VARCHAR(3));").executeUpdate();
        entityManager.createNativeQuery("DROP TABLE IF EXISTS AccountTransaction;").executeUpdate();
        entityManager.createNativeQuery("CREATE TABLE AccountTransaction (id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,accountId Long, creditDebitIndicator Char(1),amount DECIMAL(19,4),currency VARCHAR(3),baseAmount DECIMAL(19,4),baseCurrency VARCHAR(3),valueDate date ,description VARCHAR(250));").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO Account (name,balance,currency) VALUES ('biren',3141592653.59,'USD');").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO Account (name,balance,currency) VALUES ('benas',1000000.0000,'USD');").executeUpdate();

        entityManager.getTransaction().commit();


    }

    public static void startApp() {
        RevolutMoneyTransferApp.main(null);

    }

    public static void stopApp() {
        stop();
        awaitStop();
    }
}
