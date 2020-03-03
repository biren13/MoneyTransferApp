package com.revolut.controller;

import com.revolut.dom.AccountTransaction;
import com.revolut.repository.AccountTransactionRepository;
import com.revolut.response.StandardResponse;
import com.revolut.response.StatusResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spark.Request;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class AccountTransactionControllerImplUnitTest {
    private AccountTransactionController accountTransactionController;
    @Mock
    AccountTransactionRepository accountTransactionRepository;
    @Mock
    Request request;
    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.accountTransactionController = new AccountTransactionControllerImpl(this.accountTransactionRepository);
    }
    @Test
    public void testSuccessfulOperation() throws Exception {
        when(request.params("id")).thenReturn("1");

        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setAccountId(1L);
        accountTransaction.setCreditDebitIndicator('D');
        accountTransaction.setBaseAmount(BigDecimal.ONE);
        accountTransaction.setAmount(BigDecimal.ONE);
        accountTransaction.setCurrency("USD");
        accountTransaction.setBaseCurrency("USD");

        when(this.accountTransactionRepository.getAllAccountTransactionListByAccount(1L)).thenReturn(List.of(accountTransaction));

        StandardResponse response = this.accountTransactionController.listAllTransaction(request,null);
        assertEquals(StatusResponse.SUCCESS , response.getStatus());
        assertEquals(response.getData(),List.of(accountTransaction));
    }
}
