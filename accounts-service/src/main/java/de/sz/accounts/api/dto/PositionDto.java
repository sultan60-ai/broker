package de.sz.accounts.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PositionDto(
    String isin,
    BigDecimal quantityTotal,
    BigDecimal availableQty,
    BigDecimal blockedQty,
    BigDecimal avgCostAmount,
    String avgCostCcy,
    Instant asOf
) {}
