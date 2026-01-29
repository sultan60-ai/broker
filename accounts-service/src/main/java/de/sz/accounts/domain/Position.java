package de.sz.accounts.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "positions", indexes = {@Index(name="idx_positions_account", columnList="accountId")})
public class Position {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(length = 64, nullable = false)
  private String accountId;

  @Column(length = 12, nullable = false)
  private String isin;

  @Column(nullable = false, precision = 19, scale = 6)
  private BigDecimal quantityTotal;

  @Column(nullable = false, precision = 19, scale = 6)
  private BigDecimal availableQty;

  @Column(nullable = false, precision = 19, scale = 6)
  private BigDecimal blockedQty;

  @Column(nullable = false, precision = 19, scale = 6)
  private BigDecimal avgCostAmount;

  @Column(nullable = false, length = 3)
  private String avgCostCcy;

  @Column(nullable = false)
  private Instant asOf;

  public Position() {}

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getAccountId() { return accountId; }
  public void setAccountId(String accountId) { this.accountId = accountId; }

  public String getIsin() { return isin; }
  public void setIsin(String isin) { this.isin = isin; }

  public BigDecimal getQuantityTotal() { return quantityTotal; }
  public void setQuantityTotal(BigDecimal quantityTotal) { this.quantityTotal = quantityTotal; }

  public BigDecimal getAvailableQty() { return availableQty; }
  public void setAvailableQty(BigDecimal availableQty) { this.availableQty = availableQty; }

  public BigDecimal getBlockedQty() { return blockedQty; }
  public void setBlockedQty(BigDecimal blockedQty) { this.blockedQty = blockedQty; }

  public BigDecimal getAvgCostAmount() { return avgCostAmount; }
  public void setAvgCostAmount(BigDecimal avgCostAmount) { this.avgCostAmount = avgCostAmount; }

  public String getAvgCostCcy() { return avgCostCcy; }
  public void setAvgCostCcy(String avgCostCcy) { this.avgCostCcy = avgCostCcy; }

  public Instant getAsOf() { return asOf; }
  public void setAsOf(Instant asOf) { this.asOf = asOf; }
}
