package de.sz.accounts.api.dto;

import java.time.Instant;

public record AccountDto(
    String accountId,
    String customerId,
    String status,
    String iban,
    boolean tradingEnabled,
    boolean paymentsEnabled,
    Instant createdAt,
    Instant updatedAt
) {}
