package com.revolut.processor;

import com.revolut.dom.AccountTransaction;
import com.revolut.dom.MoneyTransferCommand;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AccountTransactionProcessorImpl  implements  AccountTransactionProcessor{
    @Override
    public List<AccountTransaction> createAccountTransactionList(MoneyTransferCommand command) {
        Date valuedate = Calendar.getInstance().getTime();
        AccountTransaction transaction1 = new AccountTransaction();
        transaction1.setAccountId(command.getFromAccountId());
        transaction1.setCreditDebitIndicator('D');
        transaction1.setAmount(command.getAmount());
        transaction1.setCurrency(command.getCurrency());
        transaction1.setBaseAmount(command.getAmount());
        transaction1.setBaseCurrency(command.getCurrency());
        transaction1.setValueDate(valuedate);
        transaction1.setDescription("Transfer money");
        AccountTransaction transaction2 = new AccountTransaction();
        transaction2.setAccountId(command.getToAccountId());
        transaction2.setCreditDebitIndicator('C');
        transaction2.setAmount(command.getAmount());
        transaction2.setCurrency(command.getCurrency());
        transaction2.setBaseAmount(command.getAmount());
        transaction2.setBaseCurrency(command.getCurrency());
        transaction1.setValueDate(valuedate);
        transaction2.setDescription("Receive money");

        return List.of(transaction1,transaction2) ;
    }
}
