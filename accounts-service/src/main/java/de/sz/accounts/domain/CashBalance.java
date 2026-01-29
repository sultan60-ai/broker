package de.sz.accounts.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "cash_balances")
public class CashBalance {

  @Id
  @Column(length = 64)
  private String accountId;

  @Column(nullable = false, length = 3)
  private String currency; // EUR

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal bookBalance;

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal availableBalance;

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal blockedAmount;

  @Column(nullable = false)
  private Instant asOf;

  public CashBalance() {}

  public CashBalance(String accountId, String currency, BigDecimal bookBalance, BigDecimal availableBalance,
                     BigDecimal blockedAmount, Instant asOf) {
    this.accountId = accountId;
    this.currency = currency;
    this.bookBalance = bookBalance;
    this.availableBalance = availableBalance;
    this.blockedAmount = blockedAmount;
    this.asOf = asOf;
  }

  public String getAccountId() { return accountId; }
  public void setAccountId(String accountId) { this.accountId = accountId; }

  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }

  public BigDecimal getBookBalance() { return bookBalance; }
  public void setBookBalance(BigDecimal bookBalance) { this.bookBalance = bookBalance; }

  public BigDecimal getAvailableBalance() { return availableBalance; }
  public void setAvailableBalance(BigDecimal availableBalance) { this.availableBalance = availableBalance; }

  public BigDecimal getBlockedAmount() { return blockedAmount; }
  public void setBlockedAmount(BigDecimal blockedAmount) { this.blockedAmount = blockedAmount; }

  public Instant getAsOf() { return asOf; }
  public void setAsOf(Instant asOf) { this.asOf = asOf; }
}
