package de.sz.trading.repo;

import de.sz.trading.domain.Execution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecutionRepository extends JpaRepository<Execution, String> {
  List<Execution> findByOrderIdOrderByExecutedAtAsc(String orderId);
}
