package de.sz.trading.service;

import de.sz.trading.api.dto.ExecutionDto;
import de.sz.trading.api.dto.OrderDto;
import de.sz.trading.domain.TradeOrder;
import de.sz.trading.repo.ExecutionRepository;
import de.sz.trading.repo.TradeOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderQueryService {

  private final TradeOrderRepository orderRepo;
  private final ExecutionRepository execRepo;

  public OrderQueryService(TradeOrderRepository orderRepo, ExecutionRepository execRepo) {
    this.orderRepo = orderRepo;
    this.execRepo = execRepo;
  }

  public OrderDto getOrder(String orderId) {
    var o = orderRepo.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
    return toDto(o);
  }

  public List<OrderDto> listOrdersByCustomer(String customerId) {
    return orderRepo.findTop200ByCustomerIdOrderByCreatedAtDesc(customerId).stream().map(this::toDto).toList();
  }

  public List<ExecutionDto> listExecutions(String orderId) {
    return execRepo.findByOrderIdOrderByExecutedAtAsc(orderId).stream()
        .map(e -> new ExecutionDto(e.getExecutionId(), e.getQuantity(), e.getPrice(), e.getExecutedAt()))
        .toList();
  }

  private OrderDto toDto(TradeOrder o) {
    return new OrderDto(
        o.getOrderId(),
        o.getClientOrderId(),
        o.getCustomerId(),
        o.getAccountId(),
        o.getSide(),
        o.getIsin(),
        o.getQuantity(),
        o.getOrderType(),
        o.getLimitPrice(),
        o.getStatus(),
        o.getBankOrderId(),
        o.getRejectReason(),
        o.getCreatedAt(),
        o.getUpdatedAt()
    );
  }
}
