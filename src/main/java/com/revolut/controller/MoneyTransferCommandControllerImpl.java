package com.revolut.controller;

import com.revolut.RevolutMoneyTransferApp;
import com.revolut.dom.Account;
import com.revolut.dom.AccountTransaction;
import com.revolut.dom.MoneyTransferCommand;
import com.revolut.exception.InvalidInput;
import com.revolut.processor.AccountProcessor;
import com.revolut.processor.AccountTransactionProcessor;
import com.revolut.repository.AccountRepository;
import com.revolut.repository.AccountTransactionRepository;
import com.revolut.response.StandardResponse;
import com.revolut.response.StatusResponse;
import com.revolut.util.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class MoneyTransferCommandControllerImpl implements MoneyTransferCommandController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final EntityManager entityManager;
    private final AccountProcessor accountProcessor;
    private final JsonParser jsonParser;
    private final AccountRepository accountRepository;
    private final AccountTransactionRepository accountTransactionRepository;
    private final AccountTransactionProcessor accountTransactionProcessor;

    @Inject
    public MoneyTransferCommandControllerImpl(EntityManager entityManager, AccountProcessor accountProcessor,
                                              JsonParser jsonParser, AccountRepository accountRepository,
                                              AccountTransactionRepository accountTransactionRepository,
                                              AccountTransactionProcessor accountTransactionProcessor) {
        this.entityManager = entityManager;
        this.accountProcessor = accountProcessor;
        this.jsonParser = jsonParser;
        this.accountRepository = accountRepository;
        this.accountTransactionRepository = accountTransactionRepository;
        this.accountTransactionProcessor = accountTransactionProcessor;
    }

    @Override
    public StandardResponse doMoneyTransfer(Request request, Response response) throws Exception {
        MoneyTransferCommand command = jsonParser.toPojoFromJson(request.body(),MoneyTransferCommand.class);
        logger.info("Command Received " + command);
        entityManager.getTransaction().begin();
        Account fromAccount = this.accountRepository.getAccountById(command.getFromAccountId()).orElseThrow( ()-> new InvalidInput("From Account does not exist"));
        Account toAccount = this.accountRepository.getAccountById(command.getToAccountId()).orElseThrow( ()-> new InvalidInput("To Account does not exist"));
        fromAccount =
        accountProcessor.processWithdraw(fromAccount,command.getAmount(),command.getCurrency());
        toAccount = accountProcessor.processDeposit(toAccount,command.getAmount(),command.getCurrency());
        this.accountRepository.saveAccount(fromAccount);
        this.accountRepository.saveAccount(toAccount);

        List<AccountTransaction> accountTransactionList =
                accountTransactionProcessor.createAccountTransactionList(command);
        for(AccountTransaction accountTransaction: accountTransactionList)
        {
            this.accountTransactionRepository.saveAccountTransaction(accountTransaction);
        }

        entityManager.getTransaction().commit();
        logger.info("successfully completed");
        return new StandardResponse(StatusResponse.SUCCESS,"successful") ;
    }


}
