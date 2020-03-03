package com.revolut.processor;

import com.revolut.dom.Account;

import java.math.BigDecimal;

public interface AccountOperationValidator {
    boolean thereIsMoneyToWithdraw(Account account, BigDecimal amount);
    boolean isCrossCurrencyTransaction(String accountCurrenccy,String transactionCurrency);
}
