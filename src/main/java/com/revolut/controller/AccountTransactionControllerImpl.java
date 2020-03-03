package com.revolut.controller;

import com.google.inject.Inject;
import com.revolut.dom.AccountTransaction;
import com.revolut.repository.AccountTransactionRepository;
import com.revolut.response.StandardResponse;
import com.revolut.response.StatusResponse;
import com.revolut.util.JsonParser;
import spark.Request;
import spark.Response;

import java.util.List;

public class AccountTransactionControllerImpl implements AccountTransactionController {
    private final AccountTransactionRepository accountTransactionRepository;

    @Inject
    public AccountTransactionControllerImpl(AccountTransactionRepository accountTransactionRepository) {
        this.accountTransactionRepository = accountTransactionRepository;
    }

    @Override
    public StandardResponse listAllTransaction(Request request, Response response) throws Exception {
        Long id = Long.parseLong(request.params("id"));
        List<AccountTransaction> accountTransactionList = this.accountTransactionRepository.getAllAccountTransactionListByAccount(id);
        return new StandardResponse(StatusResponse.SUCCESS,accountTransactionList);

    }
}
