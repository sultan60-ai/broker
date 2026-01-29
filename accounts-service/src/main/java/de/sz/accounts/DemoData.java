package de.sz.accounts;

import de.sz.accounts.domain.Account;
import de.sz.accounts.domain.CashBalance;
import de.sz.accounts.domain.Position;
import de.sz.accounts.repo.AccountRepository;
import de.sz.accounts.repo.CashBalanceRepository;
import de.sz.accounts.repo.PositionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;

@Configuration
public class DemoData {

  @Bean
  CommandLineRunner seed(AccountRepository ar, CashBalanceRepository cr, PositionRepository pr) {
    return args -> {
      if (ar.existsById("acc_1001")) return;
      var now = Instant.now();

      var account = new Account(
          "acc_1001",
          "cust_1",
          "ACTIVE",
          "DE02100100101234567890",
          true,
          true,
          now,
          now
      );
      ar.save(account);

      var cash = new CashBalance(
          "acc_1001",
          "EUR",
          new BigDecimal("10000.00"),
          new BigDecimal("9200.00"),
          new BigDecimal("800.00"),
          now
      );
      cr.save(cash);

      var pos = new Position();
      pos.setAccountId("acc_1001");
      pos.setIsin("US0378331005");
      pos.setQuantityTotal(new BigDecimal("5"));
      pos.setAvailableQty(new BigDecimal("5"));
      pos.setBlockedQty(new BigDecimal("0"));
      pos.setAvgCostAmount(new BigDecimal("180.12"));
      pos.setAvgCostCcy("EUR");
      pos.setAsOf(now);
      pr.save(pos);
    };
  }
}
