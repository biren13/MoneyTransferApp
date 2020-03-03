package com.revolut.processor;

import com.revolut.dom.AccountTransaction;
import com.revolut.dom.MoneyTransferCommand;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class AccountTransactionProcessorImplTest {
    private AccountTransactionProcessor accountTransactionProcess;
    @Before
    public void setUp()
    {
        this.accountTransactionProcess = new AccountTransactionProcessorImpl();
    }
    @Test
    public void testTransactionCreation()
    {
        MoneyTransferCommand command = new MoneyTransferCommand();
        command.setFromAccountId(1L);
        command.setToAccountId(2L);
        command.setAmount(BigDecimal.TEN);
        List<AccountTransaction> accountTransactionList =
        this.accountTransactionProcess.createAccountTransactionList(command);
        assertTrue(accountTransactionList.size() == 2);
        AccountTransaction transaction1 = accountTransactionList.get(0);
        AccountTransaction transaction2 = accountTransactionList.get(1);
        compareAccountTransaction(transaction1,1L,'D',BigDecimal.TEN,"Transfer money");

        compareAccountTransaction(transaction2,2L,'C',BigDecimal.TEN,"Receive money");
    }
    private void compareAccountTransaction(AccountTransaction accountTransaction,Long accountId,Character creditDebit,BigDecimal amount,String description)
    {
        assertTrue(accountTransaction.getAccountId().compareTo(accountId)==0);
        assertTrue(accountTransaction.getAmount().compareTo(amount)==0);
        assertTrue(accountTransaction.getCreditDebitIndicator() == creditDebit);
        assertTrue(accountTransaction.getDescription().equalsIgnoreCase(description));
    }
}