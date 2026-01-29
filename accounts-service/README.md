# accounts-service (Spring Boot 3, Java 17) – Domain: Accounts

Enthält:
- REST APIs: Accounts, Cash Balances, Positions
- OpenAPI/Swagger UI
- OAuth2 Resource Server (JWT) + `dev` Profil ohne Auth
- ETag (ShallowEtagHeaderFilter)

Hinweis: Dieses Paket ist *ohne Lombok*, um JDK-internen javac-Inkompatibilitäten vorzubeugen.

## Start (DEV ohne Auth)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
Swagger UI: http://localhost:8080/swagger-ui.html  
H2 Console:  http://localhost:8080/h2

## Start (PROD mit JWT)
Konfiguriere `spring.security.oauth2.resourceserver.jwt.issuer-uri` **oder** `jwk-set-uri` in `application.yml`.

Erwartete Scopes:
- GET `/v1/**`  => `SCOPE_accounts.read`
- POST `/v1/**` => `SCOPE_accounts.write`

## Test Calls (dev)
```bash
curl "http://localhost:8080/v1/accounts?customerId=cust_1"
curl "http://localhost:8080/v1/accounts/acc_1001"
curl "http://localhost:8080/v1/accounts/acc_1001/cash-balances"
curl "http://localhost:8080/v1/accounts/acc_1001/positions"
```

## Events / Outbox Pattern

Dieses Projekt implementiert ein simples **Outbox Pattern**:
- Bei Write-Operations werden Domain Events in `outbox_events` als `PENDING` gespeichert.
- Optionaler Publisher liest `PENDING` Events und veröffentlicht sie nach Kafka.

### Outbox Publisher aktivieren
Standardmäßig ist der Publisher AUS (kein Kafka nötig). Aktivieren mit:

```bash
./mvnw -f pom.xml spring-boot:run -Dspring-boot.run.profiles=dev   -Dspring-boot.run.arguments="--app.outbox.publisher.enabled=true"
```

### Kafka lokal starten (optional)
```bash
docker compose -f docker-compose.kafka.yml up -d
```

Topic (Default): `bank.events.accounts`

### Demo: Events erzeugen
```bash
curl -X POST "http://localhost:8082/v1/accounts" -H "Content-Type: application/json" -d '{
  "accountId":"acc_2001","customerId":"cust_2","iban":"DE02100100101234567890"
}'
curl -X POST "http://localhost:8082/v1/accounts/acc_2001/positions" -H "Content-Type: application/json" -d '{
  "isin":"US0378331005","quantityTotal":3,"availableQty":3,"blockedQty":0,"avgCostAmount":190.12,"avgCostCcy":"EUR"
}'
curl -X POST "http://localhost:8082/v1/accounts/acc_2001/cash-bookings" -H "Content-Type: application/json" -d '{
  "type":"SEPA_IN","amount":1000.00,"currency":"EUR","reference":"Deposit"
}'
curl "http://localhost:8082/v1/accounts/acc_2001/outbox"
```
