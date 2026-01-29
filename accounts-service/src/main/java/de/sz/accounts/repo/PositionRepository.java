package de.sz.accounts.repo;

import de.sz.accounts.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, String> {
  List<Position> findByAccountIdOrderByIsinAsc(String accountId);
}
