package de.sz.accounts.service;

import de.sz.accounts.repo.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@ConditionalOnProperty(prefix = "app.outbox.publisher", name = "enabled", havingValue = "true")
public class OutboxPublisher {

  private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

  private final OutboxEventRepository repo;
  private final KafkaTemplate<String, String> kafka;

  @Value("${app.outbox.kafka.topic:bank.events.accounts}")
  private String topic;

  @Value("${app.outbox.publisher.batchSize:50}")
  private int batchSize;

  public OutboxPublisher(OutboxEventRepository repo, KafkaTemplate<String, String> kafka) {
    this.repo = repo;
    this.kafka = kafka;
  }

  @Scheduled(fixedDelayString = "${app.outbox.publisher.fixedDelayMs:2000}")
  @Transactional
  public void publishBatch() {
    var batch = repo.findTop50ByStatusOrderByCreatedAtAsc("PENDING");
    if (batch.isEmpty()) return;

    for (var evt : batch) {
      try {
        kafka.send(topic, evt.getAggregateId(), evt.getPayloadJson());
        evt.setStatus("PUBLISHED");
        evt.setPublishedAt(Instant.now());
        evt.setLastError(null);
      } catch (Exception e) {
        evt.setStatus("FAILED");
        evt.setLastError(e.toString());
        log.warn("Outbox publish failed (kept FAILED). id={} err={}", evt.getId(), e.toString());
      }
      repo.save(evt);
    }
  }
}
