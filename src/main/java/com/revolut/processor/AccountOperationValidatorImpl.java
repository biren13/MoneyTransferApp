package com.revolut.processor;

import com.revolut.dom.Account;

import java.math.BigDecimal;

public class AccountOperationValidatorImpl implements AccountOperationValidator {
    @Override
    public boolean thereIsMoneyToWithdraw(Account account, BigDecimal amount) {
        if(account.getBalance()!= null && account.getBalance().compareTo(amount) >0 )
        return true;
        return false;
    }

    @Override
    public boolean isCrossCurrencyTransaction(String accountCurrenccy, String transactionCurrency) {
        if(accountCurrenccy == null)
        {
            return true;
        }
        return !accountCurrenccy.equalsIgnoreCase(transactionCurrency);
    }
}
