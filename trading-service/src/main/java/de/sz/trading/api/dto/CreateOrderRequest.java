package de.sz.trading.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateOrderRequest(
    @NotBlank @Size(max=64) String clientOrderId,
    @NotBlank @Size(max=64) String customerId,
    @NotBlank @Size(max=64) String accountId,
    @NotBlank @Pattern(regexp = "BUY|SELL") String side,
    @NotBlank @Size(min=12, max=12) String isin,
    @NotNull @Positive BigDecimal quantity,
    @NotBlank @Pattern(regexp = "MARKET|LIMIT") String orderType,
    BigDecimal limitPrice
) {}
