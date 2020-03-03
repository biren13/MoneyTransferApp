package com.revolut.processor;

import com.revolut.dom.Account;
import com.revolut.exception.AccountOperationProccessingException;

import javax.inject.Inject;
import java.math.BigDecimal;

public class AccountProcessorImpl implements AccountProcessor{
    private final AccountOperationValidator accountOperationValidator;

    @Inject
    public AccountProcessorImpl(AccountOperationValidator accountOperationValidator) {
        this.accountOperationValidator = accountOperationValidator;
    }

    //seperate methods will be helpful if we start processing cross currency transaction.
    @Override
    public Account processWithdraw(Account account, BigDecimal amount,String currency) {
        if(accountOperationValidator.isCrossCurrencyTransaction(account.getCurrency(),currency))
        {
            throw new AccountOperationProccessingException(String.format("Account %s can not process foreign currency %s transaction",account.getId(),currency));
        }
        if(accountOperationValidator.thereIsMoneyToWithdraw(account,amount))
        {
            BigDecimal newBalance = account.getBalance().subtract(amount);
            account.setBalance(newBalance);
            return account;
        }
        else{
            throw new AccountOperationProccessingException(String.format("Account %s not able to withdraw %s. Balance is %s",account.getId(), amount, account.getBalance()));
        }

    }

    @Override
    public Account processDeposit(Account account, BigDecimal amount,String currency) {
        if(accountOperationValidator.isCrossCurrencyTransaction(account.getCurrency(),currency))
        {
            throw new AccountOperationProccessingException(String.format("Account %s can not process foreign currency %s transaction",account.getId(),currency));
        }
        if(BigDecimal.ZERO.compareTo(amount) > 0 )
        {
            throw new AccountOperationProccessingException(String.format("Account %s can not process %s for deposit",account.getId(), amount));

        }
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        return account;
    }
}
