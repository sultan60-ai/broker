package de.sz.trading.bank;

public record BankSubmissionResult(boolean accepted, String bankOrderId, String rejectReason) {}
