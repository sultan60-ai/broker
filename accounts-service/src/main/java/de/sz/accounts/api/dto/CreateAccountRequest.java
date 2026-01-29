package de.sz.accounts.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
    @NotBlank @Size(max = 64) String accountId,
    @NotBlank @Size(max = 64) String customerId,
    @Size(max = 34) String iban
) {}
