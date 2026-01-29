package de.sz.accounts.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "cash_ledger",
    indexes = {@Index(name = "idx_cash_ledger_account_time", columnList = "accountId,bookingDateTime")})
public class CashLedgerEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String bookingId;

  @Column(length = 64, nullable = false)
  private String accountId;

  @Column(length = 32, nullable = false)
  private String type; // TRADE, FEE, TAX, SEPA_IN, SEPA_OUT, ...

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal amount;

  @Column(nullable = false, length = 3)
  private String currency;

  @Column(nullable = false)
  private Instant bookingDateTime;

  @Column(nullable = false)
  private Instant valueDate;

  @Column(length = 140)
  private String reference;

  public CashLedgerEntry() {}

  public String getBookingId() { return bookingId; }
  public void setBookingId(String bookingId) { this.bookingId = bookingId; }

  public String getAccountId() { return accountId; }
  public void setAccountId(String accountId) { this.accountId = accountId; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }

  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }

  public Instant getBookingDateTime() { return bookingDateTime; }
  public void setBookingDateTime(Instant bookingDateTime) { this.bookingDateTime = bookingDateTime; }

  public Instant getValueDate() { return valueDate; }
  public void setValueDate(Instant valueDate) { this.valueDate = valueDate; }

  public String getReference() { return reference; }
  public void setReference(String reference) { this.reference = reference; }
}
