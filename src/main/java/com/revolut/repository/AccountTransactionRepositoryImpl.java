package com.revolut.repository;

import com.revolut.dom.AccountTransaction;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class AccountTransactionRepositoryImpl implements AccountTransactionRepository {
    private final EntityManager entityManager;

    @Inject
    public AccountTransactionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }



    @Override
    public void saveAccountTransaction(AccountTransaction accountTransaction) {
        this.entityManager.persist(accountTransaction);
    }

    @Override
    public List<AccountTransaction> getAllAccountTransactionListByAccount(Long accountId) {
        this.entityManager.getTransaction().begin();
        List<AccountTransaction> accountTransactionList =entityManager.createQuery("Select t from " + AccountTransaction.class.getName() + " t where t.accountId = :accountId")
                .setParameter("accountId",accountId)
                .getResultList();
        this.entityManager.getTransaction().commit();
        return  accountTransactionList;
    }
}
