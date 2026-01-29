package de.sz.trading.api;

import de.sz.trading.api.dto.*;
import de.sz.trading.repo.OutboxEventRepository;
import de.sz.trading.service.OrderCommandService;
import de.sz.trading.service.OrderQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class OrdersController {

  private final OrderQueryService query;
  private final OrderCommandService command;
  private final OutboxEventRepository outboxRepo;

  public OrdersController(OrderQueryService query, OrderCommandService command, OutboxEventRepository outboxRepo) {
    this.query = query;
    this.command = command;
    this.outboxRepo = outboxRepo;
  }

  @PostMapping("/orders")
  public OrderDto create(@RequestBody @Valid CreateOrderRequest req) {
    var o = command.createOrder(req);
    return query.getOrder(o.getOrderId());
  }

  @GetMapping("/orders/{orderId}")
  public Map<String, Object> get(@PathVariable String orderId) {
    var order = query.getOrder(orderId);
    var execs = query.listExecutions(orderId);
    return Map.of("order", order, "executions", execs);
  }

  @GetMapping("/orders")
  public List<OrderDto> list(@RequestParam @NotBlank String customerId) {
    return query.listOrdersByCustomer(customerId);
  }

  @PostMapping("/orders/{orderId}/cancel")
  public OrderDto cancel(@PathVariable String orderId) {
    var o = command.requestCancel(orderId);
    return query.getOrder(o.getOrderId());
  }

  @GetMapping("/orders/{orderId}/outbox")
  public Map<String, Object> outbox(@PathVariable String orderId) {
    var events = outboxRepo.findTop200ByAggregateIdOrderByCreatedAtDesc(orderId);
    return Map.of("orderId", orderId, "events", events);
  }

  @PostMapping("/bank/callbacks/order-status")
  public OrderDto bankCallback(@RequestBody @Valid BankOrderStatusCallback cb) {
    var o = command.applyBankCallback(cb);
    return query.getOrder(o.getOrderId());
  }
}
