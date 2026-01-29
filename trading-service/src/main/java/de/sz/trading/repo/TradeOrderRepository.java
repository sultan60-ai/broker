package de.sz.trading.repo;

import de.sz.trading.domain.TradeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TradeOrderRepository extends JpaRepository<TradeOrder, String> {
  Optional<TradeOrder> findByClientOrderId(String clientOrderId);
  List<TradeOrder> findTop200ByCustomerIdOrderByCreatedAtDesc(String customerId);
}
