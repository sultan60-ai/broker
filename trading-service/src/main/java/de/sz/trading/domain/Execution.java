package de.sz.trading.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="executions", indexes = {@Index(name="idx_exec_order", columnList="orderId")})
public class Execution {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String executionId;

  @Column(length = 64, nullable = false)
  private String orderId;

  @Column(nullable = false, precision = 19, scale = 6)
  private BigDecimal quantity;

  @Column(nullable = false, precision = 19, scale = 6)
  private BigDecimal price;

  @Column(nullable = false)
  private Instant executedAt;

  public Execution() {}

  public String getExecutionId() { return executionId; }
  public void setExecutionId(String executionId) { this.executionId = executionId; }

  public String getOrderId() { return orderId; }
  public void setOrderId(String orderId) { this.orderId = orderId; }

  public BigDecimal getQuantity() { return quantity; }
  public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

  public BigDecimal getPrice() { return price; }
  public void setPrice(BigDecimal price) { this.price = price; }

  public Instant getExecutedAt() { return executedAt; }
  public void setExecutedAt(Instant executedAt) { this.executedAt = executedAt; }
}
