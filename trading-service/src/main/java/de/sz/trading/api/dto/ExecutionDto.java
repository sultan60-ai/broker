package de.sz.trading.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ExecutionDto(
    String executionId,
    BigDecimal quantity,
    BigDecimal price,
    Instant executedAt
) {}
