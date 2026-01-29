package de.sz.trading.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "outbox_events",
    indexes = {
        @Index(name = "idx_outbox_status_created", columnList = "status,createdAt"),
        @Index(name = "idx_outbox_aggregate", columnList = "aggregateType,aggregateId")
    })
public class OutboxEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(length = 64, nullable = false)
  private String aggregateType;

  @Column(length = 64, nullable = false)
  private String aggregateId;

  @Column(length = 64, nullable = false)
  private String eventType;

  @Lob
  @Column(nullable = false)
  private String payloadJson;

  @Column(length = 16, nullable = false)
  private String status;

  @Column(nullable = false)
  private Instant createdAt;

  private Instant publishedAt;

  @Column(length = 512)
  private String lastError;

  public OutboxEvent() {}

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getAggregateType() { return aggregateType; }
  public void setAggregateType(String aggregateType) { this.aggregateType = aggregateType; }

  public String getAggregateId() { return aggregateId; }
  public void setAggregateId(String aggregateId) { this.aggregateId = aggregateId; }

  public String getEventType() { return eventType; }
  public void setEventType(String eventType) { this.eventType = eventType; }

  public String getPayloadJson() { return payloadJson; }
  public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public Instant getPublishedAt() { return publishedAt; }
  public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

  public String getLastError() { return lastError; }
  public void setLastError(String lastError) { this.lastError = lastError; }
}
