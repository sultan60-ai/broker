package de.sz.trading.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "trade_orders",
    indexes = {
        @Index(name="idx_orders_customer", columnList="customerId"),
        @Index(name="idx_orders_account", columnList="accountId"),
        @Index(name="idx_orders_status", columnList="status")
    })
public class TradeOrder {

  @Id
  @Column(length = 64)
  private String orderId;

  @Column(length = 64, nullable = false)
  private String clientOrderId;

  @Column(length = 64, nullable = false)
  private String customerId;

  @Column(length = 64, nullable = false)
  private String accountId;

  @Column(length = 8, nullable = false)
  private String side; // BUY/SELL

  @Column(length = 12, nullable = false)
  private String isin;

  @Column(nullable = false, precision = 19, scale = 6)
  private BigDecimal quantity;

  @Column(length = 16, nullable = false)
  private String orderType; // MARKET/LIMIT

  @Column(precision = 19, scale = 6)
  private BigDecimal limitPrice;

  @Column(length = 32, nullable = false)
  private String status;

  @Column(length = 64)
  private String bankOrderId;

  @Column(length = 256)
  private String rejectReason;

  private Instant createdAt;
  private Instant updatedAt;

  public TradeOrder() {}

  public String getOrderId() { return orderId; }
  public void setOrderId(String orderId) { this.orderId = orderId; }

  public String getClientOrderId() { return clientOrderId; }
  public void setClientOrderId(String clientOrderId) { this.clientOrderId = clientOrderId; }

  public String getCustomerId() { return customerId; }
  public void setCustomerId(String customerId) { this.customerId = customerId; }

  public String getAccountId() { return accountId; }
  public void setAccountId(String accountId) { this.accountId = accountId; }

  public String getSide() { return side; }
  public void setSide(String side) { this.side = side; }

  public String getIsin() { return isin; }
  public void setIsin(String isin) { this.isin = isin; }

  public java.math.BigDecimal getQuantity() { return quantity; }
  public void setQuantity(java.math.BigDecimal quantity) { this.quantity = quantity; }

  public String getOrderType() { return orderType; }
  public void setOrderType(String orderType) { this.orderType = orderType; }

  public java.math.BigDecimal getLimitPrice() { return limitPrice; }
  public void setLimitPrice(java.math.BigDecimal limitPrice) { this.limitPrice = limitPrice; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public String getBankOrderId() { return bankOrderId; }
  public void setBankOrderId(String bankOrderId) { this.bankOrderId = bankOrderId; }

  public String getRejectReason() { return rejectReason; }
  public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
