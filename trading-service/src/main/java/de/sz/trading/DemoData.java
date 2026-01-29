package de.sz.trading;

import de.sz.trading.api.dto.CreateOrderRequest;
import de.sz.trading.service.OrderCommandService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DemoData {

  @Bean
  CommandLineRunner seed(OrderCommandService command) {
    return args -> {
      command.createOrder(new CreateOrderRequest(
          "seed_1",
          "cust_1",
          "acc_1001",
          "BUY",
          "US0378331005",
          new BigDecimal("1"),
          "MARKET",
          null
      ));
    };
  }
}
