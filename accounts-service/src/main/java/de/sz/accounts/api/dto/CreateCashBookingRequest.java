package de.sz.accounts.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateCashBookingRequest(
    @NotBlank @Size(max = 32) String type,
    @NotNull BigDecimal amount,
    @NotBlank @Size(min = 3, max = 3) String currency,
    @Size(max = 140) String reference
) {}
