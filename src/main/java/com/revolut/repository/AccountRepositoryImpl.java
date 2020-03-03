package com.revolut.repository;

import com.revolut.dom.Account;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository{

    private EntityManager entityManager;

    @Inject
    public AccountRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveAccount(Account account) {
        entityManager.persist(account);
    }

    public void createAccount(Account account)
    {
        entityManager.merge(account);
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return Optional.ofNullable(entityManager.find(Account.class,id));
    }

    @Override
    public List<Account> getAllAccountList() {
        Query query = entityManager.createQuery("from " + Account.class.getName() + " a");

        List<Account> accountList = query.getResultList();
        return accountList;
    }
}
