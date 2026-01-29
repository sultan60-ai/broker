package de.sz.accounts.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CashBalanceDto(
    String accountId,
    String currency,
    BigDecimal bookBalance,
    BigDecimal availableBalance,
    BigDecimal blockedAmount,
    Instant asOf
) {}
