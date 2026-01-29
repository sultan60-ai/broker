package de.sz.trading.bank;

import de.sz.trading.domain.TradeOrder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StubBankTradingGateway implements BankTradingGateway {

  @Override
  public BankSubmissionResult submitOrder(TradeOrder order) {
    String bankOrderId = "bko_" + UUID.randomUUID().toString().substring(0, 10);
    return new BankSubmissionResult(true, bankOrderId, null);
  }

  @Override
  public BankCancelResult cancelOrder(TradeOrder order) {
    return new BankCancelResult(true, null);
  }
}
