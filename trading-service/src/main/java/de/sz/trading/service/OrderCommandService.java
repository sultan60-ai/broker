package de.sz.trading.service;

import de.sz.trading.api.dto.BankOrderStatusCallback;
import de.sz.trading.api.dto.CreateOrderRequest;
import de.sz.trading.bank.BankTradingGateway;
import de.sz.trading.domain.Execution;
import de.sz.trading.domain.TradeOrder;
import de.sz.trading.repo.ExecutionRepository;
import de.sz.trading.repo.TradeOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderCommandService {

private static java.util.Map<String, Object> mapNonNull(Object... kv) {
  java.util.HashMap<String, Object> m = new java.util.HashMap<>();
  for (int i = 0; i + 1 < kv.length; i += 2) {
    String k = String.valueOf(kv[i]);
    Object v = kv[i + 1];
    if (v != null) m.put(k, v);
  }
  return m;
}


  private final TradeOrderRepository orderRepo;
  private final ExecutionRepository execRepo;
  private final BankTradingGateway bank;
  private final OutboxService outbox;

  public OrderCommandService(TradeOrderRepository orderRepo,
                             ExecutionRepository execRepo,
                             BankTradingGateway bank,
                             OutboxService outbox) {
    this.orderRepo = orderRepo;
    this.execRepo = execRepo;
    this.bank = bank;
    this.outbox = outbox;
  }

  @Transactional
  public TradeOrder createOrder(CreateOrderRequest req) {
    var existing = orderRepo.findByClientOrderId(req.clientOrderId());
    if (existing.isPresent()) {
      return existing.get();
    }

    Instant now = Instant.now();

    TradeOrder o = new TradeOrder();
    o.setOrderId("ord_" + UUID.randomUUID().toString().substring(0, 12));
    o.setClientOrderId(req.clientOrderId());
    o.setCustomerId(req.customerId());
    o.setAccountId(req.accountId());
    o.setSide(req.side());
    o.setIsin(req.isin());
    o.setQuantity(req.quantity());
    o.setOrderType(req.orderType());
    o.setLimitPrice(req.limitPrice());
    o.setStatus("NEW");
    o.setCreatedAt(now);
    o.setUpdatedAt(now);

    orderRepo.save(o);

    java.util.Map<String, Object> payload = new java.util.HashMap<>();
payload.put("eventType", "ORDER_SUBMITTED");
payload.put("occurredAt", now.toString());
payload.put("orderId", o.getOrderId());
payload.put("clientOrderId", o.getClientOrderId());
payload.put("customerId", o.getCustomerId());
payload.put("accountId", o.getAccountId());
payload.put("side", o.getSide());
payload.put("isin", o.getIsin());
payload.put("quantity", o.getQuantity());
payload.put("orderType", o.getOrderType());
if (o.getLimitPrice() != null) {
  payload.put("limitPrice", o.getLimitPrice());
}

outbox.enqueueOrderEvent(o.getOrderId(), "ORDER_SUBMITTED", payload);

var res = bank.submitOrder(o);
    if (res.accepted()) {
      o.setStatus("ACCEPTED");
      o.setBankOrderId(res.bankOrderId());
      o.setUpdatedAt(Instant.now());
      orderRepo.save(o);

      outbox.enqueueOrderEvent(o.getOrderId(), "ORDER_ACCEPTED", mapNonNull(
          "eventType", "ORDER_ACCEPTED",
          "occurredAt", Instant.now().toString(),
          "orderId", o.getOrderId(),
          "bankOrderId", o.getBankOrderId()
      ));
    } else {
      o.setStatus("REJECTED");
      o.setRejectReason(res.rejectReason());
      o.setUpdatedAt(Instant.now());
      orderRepo.save(o);

      outbox.enqueueOrderEvent(o.getOrderId(), "ORDER_REJECTED", mapNonNull(
          "eventType", "ORDER_REJECTED",
          "occurredAt", Instant.now().toString(),
          "orderId", o.getOrderId(),
          "rejectReason", o.getRejectReason()
      ));
    }

    return o;
  }

  @Transactional
  public TradeOrder requestCancel(String orderId) {
    var o = orderRepo.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
    if (o.getStatus().equals("FILLED") || o.getStatus().equals("CANCELED") || o.getStatus().equals("REJECTED")) {
      return o;
    }
    o.setStatus("CANCEL_REQUESTED");
    o.setUpdatedAt(Instant.now());
    orderRepo.save(o);

    outbox.enqueueOrderEvent(orderId, "ORDER_CANCEL_REQUESTED", mapNonNull(
        "eventType", "ORDER_CANCEL_REQUESTED",
        "occurredAt", Instant.now().toString(),
        "orderId", orderId
    ));

    var res = bank.cancelOrder(o);
    if (!res.accepted()) {
      o.setRejectReason(res.rejectReason());
      o.setUpdatedAt(Instant.now());
      orderRepo.save(o);
    }
    return o;
  }

  @Transactional
  public TradeOrder applyBankCallback(BankOrderStatusCallback cb) {
    var o = orderRepo.findById(cb.orderId()).orElseThrow(() -> new NotFoundException("Order not found: " + cb.orderId()));

    if (cb.bankOrderId() != null && !cb.bankOrderId().isBlank()) {
      o.setBankOrderId(cb.bankOrderId());
    }
    if (cb.rejectReason() != null && !cb.rejectReason().isBlank()) {
      o.setRejectReason(cb.rejectReason());
    }

    o.setStatus(cb.status());
    o.setUpdatedAt(Instant.now());
    orderRepo.save(o);

    if (cb.lastFillQty() != null && cb.lastFillPrice() != null
        && cb.lastFillQty().compareTo(BigDecimal.ZERO) > 0) {
      Execution e = new Execution();
      e.setOrderId(o.getOrderId());
      e.setQuantity(cb.lastFillQty());
      e.setPrice(cb.lastFillPrice());
      e.setExecutedAt(Instant.now());
      execRepo.save(e);

      outbox.enqueueOrderEvent(o.getOrderId(), "ORDER_EXECUTED", mapNonNull(
          "eventType", "ORDER_EXECUTED",
          "occurredAt", Instant.now().toString(),
          "orderId", o.getOrderId(),
          "fillQty", cb.lastFillQty(),
          "fillPrice", cb.lastFillPrice()
      ));
    }

    outbox.enqueueOrderEvent(o.getOrderId(), "ORDER_STATUS_CHANGED", mapNonNull(
        "eventType", "ORDER_STATUS_CHANGED",
        "occurredAt", Instant.now().toString(),
        "orderId", o.getOrderId(),
        "status", o.getStatus(),
        "bankOrderId", o.getBankOrderId(),
        "rejectReason", o.getRejectReason()
    ));

    return o;
  }
}
