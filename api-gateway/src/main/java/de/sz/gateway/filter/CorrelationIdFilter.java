package de.sz.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

  public static final String HEADER = "X-Correlation-Id";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    HttpHeaders headers = exchange.getRequest().getHeaders();
    if (headers.containsKey(HEADER)) {
      return chain.filter(exchange);
    }
    String id = UUID.randomUUID().toString();
    ServerWebExchange mutated = exchange.mutate()
        .request(r -> r.headers(h -> h.add(HEADER, id)))
        .build();
    return chain.filter(mutated);
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
