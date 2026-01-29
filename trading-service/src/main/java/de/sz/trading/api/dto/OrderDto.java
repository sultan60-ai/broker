package de.sz.trading.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderDto(
    String orderId,
    String clientOrderId,
    String customerId,
    String accountId,
    String side,
    String isin,
    BigDecimal quantity,
    String orderType,
    BigDecimal limitPrice,
    String status,
    String bankOrderId,
    String rejectReason,
    Instant createdAt,
    Instant updatedAt
) {}
