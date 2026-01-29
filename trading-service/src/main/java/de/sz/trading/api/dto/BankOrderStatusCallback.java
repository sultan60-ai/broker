package de.sz.trading.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record BankOrderStatusCallback(
    @NotBlank @Size(max=64) String orderId,
    @NotBlank @Size(max=32) String status,
    String bankOrderId,
    String rejectReason,
    BigDecimal lastFillQty,
    BigDecimal lastFillPrice
) {}
