package com.revolut.repository;

import com.revolut.dom.AccountTransaction;

import java.util.List;

public interface AccountTransactionRepository {
    void saveAccountTransaction(AccountTransaction accountTransaction);
    List<AccountTransaction> getAllAccountTransactionListByAccount(Long accountId);
}
