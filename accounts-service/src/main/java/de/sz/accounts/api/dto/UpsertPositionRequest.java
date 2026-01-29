package de.sz.accounts.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpsertPositionRequest(
    @NotBlank @Size(min = 12, max = 12) String isin,
    @NotNull @PositiveOrZero BigDecimal quantityTotal,
    @NotNull @PositiveOrZero BigDecimal availableQty,
    @NotNull @PositiveOrZero BigDecimal blockedQty,
    @NotNull @PositiveOrZero BigDecimal avgCostAmount,
    @NotBlank @Size(min = 3, max = 3) String avgCostCcy
) {}
