package de.sz.accounts.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {

  @Id
  @Column(length = 64)
  private String accountId;

  @Column(length = 64, nullable = false)
  private String customerId;

  @Column(length = 32, nullable = false)
  private String status; // ACTIVE, SUSPENDED, CLOSED

  @Column(length = 34)
  private String iban;

  private boolean tradingEnabled;
  private boolean paymentsEnabled;

  private Instant createdAt;
  private Instant updatedAt;

  public Account() {}

  public Account(String accountId, String customerId, String status, String iban,
                 boolean tradingEnabled, boolean paymentsEnabled,
                 Instant createdAt, Instant updatedAt) {
    this.accountId = accountId;
    this.customerId = customerId;
    this.status = status;
    this.iban = iban;
    this.tradingEnabled = tradingEnabled;
    this.paymentsEnabled = paymentsEnabled;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public String getAccountId() { return accountId; }
  public void setAccountId(String accountId) { this.accountId = accountId; }

  public String getCustomerId() { return customerId; }
  public void setCustomerId(String customerId) { this.customerId = customerId; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public String getIban() { return iban; }
  public void setIban(String iban) { this.iban = iban; }

  public boolean isTradingEnabled() { return tradingEnabled; }
  public void setTradingEnabled(boolean tradingEnabled) { this.tradingEnabled = tradingEnabled; }

  public boolean isPaymentsEnabled() { return paymentsEnabled; }
  public void setPaymentsEnabled(boolean paymentsEnabled) { this.paymentsEnabled = paymentsEnabled; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
