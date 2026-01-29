package de.sz.accounts.repo;

import de.sz.accounts.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, String> {

  List<OutboxEvent> findTop50ByStatusOrderByCreatedAtAsc(String status);

  List<OutboxEvent> findTop200ByAggregateIdOrderByCreatedAtDesc(String aggregateId);
}
