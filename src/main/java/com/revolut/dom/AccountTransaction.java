package com.revolut.dom;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "AccountTransaction")
public class AccountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="accountId")
    private Long accountId;

    @Column(name="creditDebitIndicator")
    private Character creditDebitIndicator;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="currency")
    private String currency;

    @Column(name="baseAmount")
    private BigDecimal baseAmount;

    @Column(name="baseCurrency")
    private String baseCurrency;

    @Column(name="description")
    private String description;

    @Column(name="valueDate")
    private Date valueDate;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Character getCreditDebitIndicator() {
        return creditDebitIndicator;
    }

    public void setCreditDebitIndicator(Character creditDebitIndicator) {
        this.creditDebitIndicator = creditDebitIndicator;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    @Override
    public String toString() {
        return "AccountTransaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", creditDebitIndicator=" + creditDebitIndicator +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", baseAmount=" + baseAmount +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", description='" + description + '\'' +
                ", valueDate=" + valueDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountTransaction that = (AccountTransaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
