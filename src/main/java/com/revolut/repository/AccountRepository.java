package com.revolut.repository;

import com.revolut.dom.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    void saveAccount(Account account);
    Optional<Account> getAccountById(Long id);
    List<Account> getAllAccountList();
}
