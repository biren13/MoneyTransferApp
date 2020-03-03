package com.revolut.controller;

import com.revolut.dom.Account;
import com.revolut.dom.AccountTransaction;
import com.revolut.dom.MoneyTransferCommand;
import com.revolut.exception.AccountOperationProccessingException;
import com.revolut.exception.InvalidInput;
import com.revolut.processor.*;
import com.revolut.repository.AccountRepository;
import com.revolut.repository.AccountTransactionRepository;
import com.revolut.response.StandardResponse;
import com.revolut.response.StatusResponse;
import com.revolut.util.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spark.Request;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MoneyTransferCommandControllerImplUnitTest {
    private MoneyTransferCommandController moneyTransferCommandController;
    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityTransaction entityTransaction;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountTransactionRepository accountTransactionRepository;
    private AccountProcessor accountProcessor;
    private AccountOperationValidator accountOperationValidator;
    private AccountTransactionProcessor accountTransactionProcessor;
    @Mock
    private JsonParser jsonParser;
    @Mock
    private Request request;
    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.accountOperationValidator = new AccountOperationValidatorImpl();
        this.accountProcessor= new AccountProcessorImpl(this.accountOperationValidator);
        this.accountTransactionProcessor = new AccountTransactionProcessorImpl();

        moneyTransferCommandController = new MoneyTransferCommandControllerImpl(entityManager, accountProcessor,
                 jsonParser, accountRepository, accountTransactionRepository, accountTransactionProcessor);

    }
    @Test
    public void testSuccessfulTransfer() throws Exception {
        when(request.body()).thenReturn("");
        MoneyTransferCommand command = new MoneyTransferCommand();
        command.setFromAccountId(1L);
        command.setToAccountId(2L);
        command.setAmount(BigDecimal.TEN);
        command.setCurrency("USD");
        when(jsonParser.toPojoFromJson(any(String.class),any(Class.class))).thenReturn(command);
        Account accountFrom = new Account();
        accountFrom.setId(1L);
        accountFrom.setName("biren");
        accountFrom.setBalance(new BigDecimal("314.15"));
        accountFrom.setCurrency("USD");

        Account accountTo = new Account();
        accountTo.setId(2L);
        accountTo.setName("benas");
        accountTo.setBalance(new BigDecimal("300.0"));
        accountTo.setCurrency("USD");

        when(entityManager.getTransaction()).thenReturn(entityTransaction);


        when(this.accountRepository.getAccountById(1L)).thenReturn(java.util.Optional.of(accountFrom));
        when(this.accountRepository.getAccountById(2L)).thenReturn(java.util.Optional.of(accountTo));

        StandardResponse response = this.moneyTransferCommandController.doMoneyTransfer(request,null);
        assertTrue(StatusResponse.SUCCESS.equals(response.getStatus()));

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(this.accountRepository,times(2)).saveAccount(accountArgumentCaptor.capture());

        List<Account> capturedAccountList = accountArgumentCaptor.getAllValues();
        assertTrue(capturedAccountList.size() == 2);
        Account accountFromReturned = capturedAccountList.get(0);
        Account accountToReturned = capturedAccountList.get(1);

        assertTrue(accountFromReturned.getBalance().compareTo(new BigDecimal("304.15"))==0);
        assertTrue(accountToReturned.getBalance().compareTo(new BigDecimal("310.0"))==0);

        ArgumentCaptor<AccountTransaction> accountTransactionArgumentCaptor = ArgumentCaptor.forClass(AccountTransaction.class);
        verify(this.accountTransactionRepository,times(2)).saveAccountTransaction(accountTransactionArgumentCaptor.capture());

        List<AccountTransaction> capturedAccountTransactionList = accountTransactionArgumentCaptor.getAllValues();
        assertTrue(capturedAccountTransactionList.size()==2);
        AccountTransaction accountTransactionFrom = capturedAccountTransactionList.get(0);
        AccountTransaction accountTransactionTo = capturedAccountTransactionList.get(1);

        assertTrue(accountTransactionFrom.getAccountId() == 1L);
        assertTrue(BigDecimal.TEN.compareTo(accountTransactionFrom.getAmount())==0);
        assertTrue(BigDecimal.TEN.compareTo(accountTransactionFrom.getBaseAmount())==0);
        assertTrue('D' == accountTransactionFrom.getCreditDebitIndicator());
        assertTrue("USD".equalsIgnoreCase(accountTransactionFrom.getCurrency()));
        assertTrue("USD".equalsIgnoreCase(accountTransactionFrom.getBaseCurrency()));

        assertTrue(accountTransactionTo.getAccountId() == 2L);
        assertTrue(BigDecimal.TEN.compareTo(accountTransactionTo.getAmount())==0);
        assertTrue(BigDecimal.TEN.compareTo(accountTransactionTo.getBaseAmount())==0);
        assertTrue('C' == accountTransactionTo.getCreditDebitIndicator());
        assertTrue("USD".equalsIgnoreCase(accountTransactionTo.getCurrency()));
        assertTrue("USD".equalsIgnoreCase(accountTransactionTo.getBaseCurrency()));
    }

    @Test(expected = InvalidInput.class)
    public void testInputValidationExceptionThrown() throws Exception {
        when(request.body()).thenReturn("");
        MoneyTransferCommand command = new MoneyTransferCommand();
        command.setFromAccountId(12L);
        command.setToAccountId(22L);
        command.setAmount(BigDecimal.TEN);
        command.setCurrency("USD");
        when(jsonParser.toPojoFromJson(any(String.class),any(Class.class))).thenReturn(command);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);

        when(this.accountRepository.getAccountById(anyLong())).thenReturn(Optional.ofNullable(null));
        this.moneyTransferCommandController.doMoneyTransfer(request,null);
    }

    @Test(expected = AccountOperationProccessingException.class)
    public void testAccountOperationExceptionForNotEnoughMoneyThrown() throws Exception {
        when(request.body()).thenReturn("");
        MoneyTransferCommand command = new MoneyTransferCommand();
        command.setFromAccountId(1L);
        command.setToAccountId(2L);
        command.setAmount(BigDecimal.TEN);
        command.setCurrency("USD");
        when(jsonParser.toPojoFromJson(any(String.class),any(Class.class))).thenReturn(command);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);

        Account accountFrom = new Account();
        accountFrom.setId(1L);
        accountFrom.setName("biren");
        accountFrom.setBalance(BigDecimal.ONE);
        accountFrom.setCurrency("USD");

        Account accountTo = new Account();
        accountTo.setId(2L);
        accountTo.setName("benas");
        accountTo.setBalance(new BigDecimal("300.0"));
        accountTo.setCurrency("USD");


        when(this.accountRepository.getAccountById(1L)).thenReturn(Optional.of(accountFrom));
        when(this.accountRepository.getAccountById(2L)).thenReturn(Optional.of(accountTo));
        this.moneyTransferCommandController.doMoneyTransfer(request,null);

    }
    @Test(expected = AccountOperationProccessingException.class)
    public void testAccountOperationExceptionForCrossCurrenccyThrown() throws Exception
    {
        when(request.body()).thenReturn("");
        MoneyTransferCommand command = new MoneyTransferCommand();
        command.setFromAccountId(1L);
        command.setToAccountId(2L);
        command.setAmount(BigDecimal.TEN);
        command.setCurrency("EUR");
        when(jsonParser.toPojoFromJson(any(String.class),any(Class.class))).thenReturn(command);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);

        Account accountFrom = new Account();
        accountFrom.setId(1L);
        accountFrom.setName("biren");
        accountFrom.setBalance(BigDecimal.ONE);
        accountFrom.setCurrency("USD");

        Account accountTo = new Account();
        accountTo.setId(2L);
        accountTo.setName("benas");
        accountTo.setBalance(new BigDecimal("300.0"));
        accountTo.setCurrency("USD");


        when(this.accountRepository.getAccountById(1L)).thenReturn(Optional.of(accountFrom));
        when(this.accountRepository.getAccountById(2L)).thenReturn(Optional.of(accountTo));
        this.moneyTransferCommandController.doMoneyTransfer(request,null);

    }
}
