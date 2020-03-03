package com.revolut.processor;

import com.revolut.dom.Account;
import com.revolut.exception.AccountOperationProccessingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.persistence.MapKeyColumn;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AccountProcessorImplTest {
    @Mock
    private AccountOperationValidator accountOperationValidator;
    private AccountProcessor accountProcessor;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        accountProcessor = new AccountProcessorImpl(accountOperationValidator);
    }
    @Test
    public void testWithdrawOperation()
    {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        account.setCurrency("USD");
        BigDecimal amount = BigDecimal.TEN;

        when(accountOperationValidator.thereIsMoneyToWithdraw(account, amount)).thenReturn(true);
        Account resultedAccount =
        this.accountProcessor.processWithdraw(account,BigDecimal.TEN,"USD");
        assertNotNull(resultedAccount);
        assertTrue(new BigDecimal("90.0").compareTo(resultedAccount.getBalance()) == 0);
    }
    @Test(expected =  AccountOperationProccessingException.class)
    public void testCrossCurrencyWithdraw()
    {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        account.setCurrency("USD");
        BigDecimal amount = BigDecimal.TEN;

        when(accountOperationValidator.isCrossCurrencyTransaction(account.getCurrency(),"EUR")).thenReturn(true);
        when(accountOperationValidator.thereIsMoneyToWithdraw(account, amount)).thenReturn(true);
        Account resultedAccount =
                this.accountProcessor.processWithdraw(account,BigDecimal.TEN,"EUR");
    }
    @Test(expected =  AccountOperationProccessingException.class)
    public void testNotEnoughMoneyToWithdraw()
    {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        account.setCurrency("USD");
        BigDecimal amount = BigDecimal.TEN;

        when(accountOperationValidator.thereIsMoneyToWithdraw(account, amount)).thenReturn(false);
        Account resultedAccount =
                this.accountProcessor.processWithdraw(account,BigDecimal.TEN,"USD");
    }
    @Test(expected = AccountOperationProccessingException.class)
    public  void testCrossCurrencyDeposit()
    {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        account.setCurrency("USD");
        BigDecimal amount = new BigDecimal("10.0");
        when(accountOperationValidator.isCrossCurrencyTransaction(account.getCurrency(),"EUR")).thenReturn(true);
        Account resultedAccount =
                this.accountProcessor.processDeposit(account,amount,"EUR");

    }
    @Test(expected = AccountOperationProccessingException.class)
    public  void testDepositNegativeAmountException()
    {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        BigDecimal amount = new BigDecimal("-10.0");
        Account resultedAccount =
                this.accountProcessor.processDeposit(account,amount,"USD");
    }
    @Test
    public  void testDeposit()
    {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        BigDecimal amount = BigDecimal.TEN;
        Account resultedAccount =
                this.accountProcessor.processDeposit(account,amount,"USD");
        assertNotNull(resultedAccount);
        assertTrue(new BigDecimal("110.0").compareTo(resultedAccount.getBalance())==0);
    }
}