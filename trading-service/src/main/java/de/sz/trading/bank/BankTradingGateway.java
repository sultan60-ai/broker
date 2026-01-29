package de.sz.trading.bank;

import de.sz.trading.domain.TradeOrder;

public interface BankTradingGateway {
  BankSubmissionResult submitOrder(TradeOrder order);
  BankCancelResult cancelOrder(TradeOrder order);
}
