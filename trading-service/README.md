# trading-service (Bank-driven) – Spring Boot 3, Java 17

## Start (DEV ohne Auth)
```bash
./mvnw -f pom.xml spring-boot:run -Dspring-boot.run.profiles=dev -Dspring-boot.run.arguments="--server.port=8082"
```

Swagger UI: http://localhost:8082/swagger-ui.html  
H2 Console:  http://localhost:8082/h2

## Security (PROD)
- GET `/v1/**`  => `SCOPE_trading.read`
- POST `/v1/**` => `SCOPE_trading.write`

## APIs
- `POST /v1/orders`
- `GET  /v1/orders/{orderId}`
- `GET  /v1/orders?customerId=...`
- `POST /v1/orders/{orderId}/cancel`
- `POST /v1/bank/callbacks/order-status` (Bank -> Service callback)

## Outbox
Outbox Events landen in `outbox_events` (PENDING). Publisher optional nach Kafka.

Publisher aktivieren:
```bash
./mvnw -f pom.xml spring-boot:run -Dspring-boot.run.profiles=dev \
  -Dspring-boot.run.arguments="--server.port=8082 --app.outbox.publisher.enabled=true"
```

Kafka lokal:
```bash
docker compose -f docker-compose.kafka.yml up -d
```

## Demo Flow (Stub Bank)
```bash
curl -X POST "http://localhost:8082/v1/orders" -H "Content-Type: application/json" -d '{
  "clientOrderId":"cl_123",
  "customerId":"cust_1",
  "accountId":"acc_1001",
  "side":"BUY",
  "isin":"US0378331005",
  "quantity": 2,
  "orderType":"MARKET"
}'
```

Hinweis: Stub simuliert (Scheduler) Partial/Filled Updates.
