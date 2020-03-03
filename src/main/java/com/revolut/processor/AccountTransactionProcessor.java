package com.revolut.processor;

import com.revolut.dom.AccountTransaction;
import com.revolut.dom.MoneyTransferCommand;

import java.util.List;

public interface AccountTransactionProcessor {
    List<AccountTransaction> createAccountTransactionList(MoneyTransferCommand command);
}
