package de.sz.accounts.service;

import de.sz.accounts.api.dto.CreateAccountRequest;
import de.sz.accounts.api.dto.CreateCashBookingRequest;
import de.sz.accounts.api.dto.UpsertPositionRequest;
import de.sz.accounts.domain.*;
import de.sz.accounts.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Service
public class AccountCommandService {

  private final AccountRepository accountRepo;
  private final CashBalanceRepository cashRepo;
  private final PositionRepository posRepo;
  private final CashLedgerRepository ledgerRepo;
  private final OutboxService outbox;

  public AccountCommandService(AccountRepository accountRepo,
                               CashBalanceRepository cashRepo,
                               PositionRepository posRepo,
                               CashLedgerRepository ledgerRepo,
                               OutboxService outbox) {
    this.accountRepo = accountRepo;
    this.cashRepo = cashRepo;
    this.posRepo = posRepo;
    this.ledgerRepo = ledgerRepo;
    this.outbox = outbox;
  }

  @Transactional
  public Account createAccount(CreateAccountRequest req) {
    if (accountRepo.existsById(req.accountId())) {
      throw new IllegalArgumentException("Account already exists: " + req.accountId());
    }
    Instant now = Instant.now();

    Account a = new Account(
        req.accountId(),
        req.customerId(),
        "ACTIVE",
        req.iban(),
        true,
        true,
        now,
        now
    );
    accountRepo.save(a);

    CashBalance cb = new CashBalance(
        a.getAccountId(),
        "EUR",
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        now
    );
    cashRepo.save(cb);

    outbox.enqueueAccountEvent(a.getAccountId(), "ACCOUNT_CREATED", Map.of(
        "eventType", "ACCOUNT_CREATED",
        "occurredAt", now.toString(),
        "accountId", a.getAccountId(),
        "customerId", a.getCustomerId(),
        "iban", a.getIban()
    ));

    return a;
  }

  @Transactional
  public Position upsertPosition(String accountId, UpsertPositionRequest req) {
    accountRepo.findById(accountId)
        .orElseThrow(() -> new NotFoundException("Account not found: " + accountId));

    Instant now = Instant.now();

    Position p = posRepo.findByAccountIdOrderByIsinAsc(accountId).stream()
        .filter(x -> x.getIsin().equals(req.isin()))
        .findFirst()
        .orElseGet(Position::new);

    if (p.getId() == null) {
      p.setAccountId(accountId);
      p.setIsin(req.isin());
    }

    p.setQuantityTotal(req.quantityTotal());
    p.setAvailableQty(req.availableQty());
    p.setBlockedQty(req.blockedQty());
    p.setAvgCostAmount(req.avgCostAmount());
    p.setAvgCostCcy(req.avgCostCcy());
    p.setAsOf(now);

    posRepo.save(p);

    outbox.enqueueAccountEvent(accountId, "POSITION_CHANGED", Map.of(
        "eventType", "POSITION_CHANGED",
        "occurredAt", now.toString(),
        "accountId", accountId,
        "isin", req.isin(),
        "quantityTotal", req.quantityTotal(),
        "availableQty", req.availableQty(),
        "blockedQty", req.blockedQty()
    ));

    return p;
  }

  @Transactional
  public CashLedgerEntry createCashBooking(String accountId, CreateCashBookingRequest req) {
    accountRepo.findById(accountId)
        .orElseThrow(() -> new NotFoundException("Account not found: " + accountId));

    Instant now = Instant.now();

    CashLedgerEntry entry = new CashLedgerEntry();
    entry.setAccountId(accountId);
    entry.setType(req.type());
    entry.setAmount(req.amount());
    entry.setCurrency(req.currency());
    entry.setBookingDateTime(now);
    entry.setValueDate(now);
    entry.setReference(req.reference());
    ledgerRepo.save(entry);

    CashBalance cb = cashRepo.findById(accountId)
        .orElseThrow(() -> new NotFoundException("Cash balance not found for account: " + accountId));
    cb.setBookBalance(cb.getBookBalance().add(req.amount()));
    cb.setAvailableBalance(cb.getAvailableBalance().add(req.amount()));
    cb.setAsOf(now);
    cashRepo.save(cb);

    outbox.enqueueAccountEvent(accountId, "CASH_BOOKED", Map.of(
        "eventType", "CASH_BOOKED",
        "occurredAt", now.toString(),
        "accountId", accountId,
        "bookingId", entry.getBookingId(),
        "type", entry.getType(),
        "amount", entry.getAmount(),
        "currency", entry.getCurrency(),
        "reference", entry.getReference()
    ));

    return entry;
  }
}
