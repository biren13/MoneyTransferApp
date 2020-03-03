package com.revolut.processor;

import com.revolut.dom.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountOperationValidatorImplTest {

    private AccountOperationValidator validator;
    @Before
    public void setUp() throws Exception {
        validator =  new AccountOperationValidatorImpl();
    }

    @Test
    public  void testThereIsMoneyToWithdraw()
    {
        Account account = new Account();
        account.setBalance(BigDecimal.TEN);
        assertTrue(this.validator.thereIsMoneyToWithdraw(account,BigDecimal.ONE));
    }

    @Test
    public  void testThereIsNoMoneyToWithdraw()
    {
        Account account = new Account();
        account.setBalance(BigDecimal.ONE);
        assertFalse(this.validator.thereIsMoneyToWithdraw(account,BigDecimal.TEN));
    }
    @Test
    public void testCrossCurrency()
    {
        assertTrue(this.validator.isCrossCurrencyTransaction("USD","EUR"));
        assertFalse(this.validator.isCrossCurrencyTransaction("USD","USD"));
        assertTrue(this.validator.isCrossCurrencyTransaction(null,"EUR"));
        assertTrue(this.validator.isCrossCurrencyTransaction("USD",null));
    }

}