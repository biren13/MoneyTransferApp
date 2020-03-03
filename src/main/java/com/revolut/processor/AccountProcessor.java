package com.revolut.processor;

import com.revolut.dom.Account;

import java.math.BigDecimal;

public interface AccountProcessor {
    Account processWithdraw(Account account, BigDecimal amount,String currency);
    Account processDeposit(Account account,BigDecimal amount,String currency);
}
