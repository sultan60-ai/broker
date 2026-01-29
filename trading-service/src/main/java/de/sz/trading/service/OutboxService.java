package de.sz.trading.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.sz.trading.domain.OutboxEvent;
import de.sz.trading.repo.OutboxEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Service
public class OutboxService {

  private final OutboxEventRepository repo;
  private final ObjectMapper objectMapper;

  public OutboxService(OutboxEventRepository repo, ObjectMapper objectMapper) {
    this.repo = repo;
    this.objectMapper = objectMapper;
  }

  @Transactional
  public OutboxEvent enqueueOrderEvent(String orderId, String eventType, Map<String, Object> payload) {
    try {
      String json = objectMapper.writeValueAsString(payload);

      OutboxEvent evt = new OutboxEvent();
      evt.setAggregateType("ORDER");
      evt.setAggregateId(orderId);
      evt.setEventType(eventType);
      evt.setPayloadJson(json);
      evt.setStatus("PENDING");
      evt.setCreatedAt(Instant.now());

      return repo.save(evt);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize outbox payload", e);
    }
  }
}
