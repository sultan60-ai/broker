package de.sz.trading.bank;

import de.sz.trading.api.dto.BankOrderStatusCallback;
import de.sz.trading.repo.TradeOrderRepository;
import de.sz.trading.service.OrderCommandService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class StubBankSimulator {

  private final TradeOrderRepository orderRepo;
  private final OrderCommandService command;

  @Value("${app.bank.mode:stub}")
  private String mode;

  private final Random rnd = new Random();

  public StubBankSimulator(TradeOrderRepository orderRepo, OrderCommandService command) {
    this.orderRepo = orderRepo;
    this.command = command;
  }

  @Scheduled(fixedDelay = 3000)
  public void simulate() {
    if (!"stub".equalsIgnoreCase(mode)) return;

    var candidates = orderRepo.findAll().stream()
        .filter(o -> "ACCEPTED".equals(o.getStatus()) || "PARTIALLY_FILLED".equals(o.getStatus()))
        .limit(10)
        .toList();

    for (var o : candidates) {
      if (rnd.nextBoolean()) {
        var cb = new BankOrderStatusCallback(
            o.getOrderId(),
            "FILLED",
            o.getBankOrderId(),
            null,
            o.getQuantity(),
            new BigDecimal("180.12")
        );
        command.applyBankCallback(cb);
      } else {
        var cb = new BankOrderStatusCallback(
            o.getOrderId(),
            "PARTIALLY_FILLED",
            o.getBankOrderId(),
            null,
            o.getQuantity().divide(new BigDecimal("2")),
            new BigDecimal("180.12")
        );
        command.applyBankCallback(cb);
      }
    }
  }
}
