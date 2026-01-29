# api-gateway – Spring Cloud Gateway

## Routing
- `http://localhost:8081/accounts/**` → `accounts-service /v1/**` (default http://localhost:8082)
- `http://localhost:8081/trading/**`  → `trading-service  /v1/**`  (default http://localhost:8084)

## Start (DEV ohne Auth)
```bash
./mvnw -f pom.xml spring-boot:run -Dspring-boot.run.profiles=dev
```

## Beispiele
```bash
curl http://localhost:8081/actuator/health

# accounts-service via gateway
curl "http://localhost:8081/accounts/accounts?customerId=cust_1"

# trading-service via gateway
curl -X POST "http://localhost:8081/trading/orders" -H "Content-Type: application/json" -d '{
  "clientOrderId":"gw_1",
  "customerId":"cust_1",
  "accountId":"acc_1001",
  "side":"BUY",
  "isin":"US0378331005",
  "quantity": 1,
  "orderType":"MARKET"
}'
```
