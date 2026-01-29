package de.sz.accounts.api;

import de.sz.accounts.api.dto.*;
import de.sz.accounts.repo.CashLedgerRepository;
import de.sz.accounts.repo.OutboxEventRepository;
import de.sz.accounts.service.AccountCommandService;
import de.sz.accounts.service.AccountQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class AccountsController {

  private final AccountQueryService query;
  private final AccountCommandService command;
  private final OutboxEventRepository outboxRepo;
  private final CashLedgerRepository ledgerRepo;

  public AccountsController(AccountQueryService query,
                            AccountCommandService command,
                            OutboxEventRepository outboxRepo,
                            CashLedgerRepository ledgerRepo) {
    this.query = query;
    this.command = command;
    this.outboxRepo = outboxRepo;
    this.ledgerRepo = ledgerRepo;
  }

  // ------------------------
  // READ APIs
  // ------------------------

  @GetMapping("/accounts")
  public List<AccountDto> listAccounts(@RequestParam @NotBlank String customerId) {
    return query.getAccountsByCustomer(customerId);
  }

  @GetMapping("/accounts/{accountId}")
  public AccountDto getAccount(@PathVariable String accountId) {
    return query.getAccount(accountId);
  }

  @GetMapping("/accounts/{accountId}/cash-balances")
  public CashBalanceDto getCashBalances(@PathVariable String accountId) {
    return query.getCashBalance(accountId);
  }

  @GetMapping("/accounts/{accountId}/positions")
  public Map<String, Object> getPositions(@PathVariable String accountId,
                                         @RequestParam(required = false) String asOf) {
    var positions = query.getPositions(accountId);
    return Map.of("accountId", accountId, "asOf", asOf, "positions", positions);
  }

  @GetMapping("/accounts/{accountId}/cash-ledger")
  public Map<String, Object> getCashLedger(@PathVariable String accountId) {
    var entries = ledgerRepo.findTop200ByAccountIdOrderByBookingDateTimeDesc(accountId);
    return Map.of("accountId", accountId, "entries", entries);
  }

  // ------------------------
  // WRITE APIs (Demo)
  // ------------------------

  @PostMapping("/accounts")
  public AccountDto createAccount(@RequestBody @Valid CreateAccountRequest req) {
    command.createAccount(req);
    return query.getAccount(req.accountId());
  }

  @PostMapping("/accounts/{accountId}/positions")
  public Map<String, Object> upsertPosition(@PathVariable String accountId,
                                            @RequestBody @Valid UpsertPositionRequest req) {
    var p = command.upsertPosition(accountId, req);
    return Map.of("accountId", accountId, "isin", p.getIsin(), "asOf", p.getAsOf().toString());
  }

  @PostMapping("/accounts/{accountId}/cash-bookings")
  public Map<String, Object> createCashBooking(@PathVariable String accountId,
                                               @RequestBody @Valid CreateCashBookingRequest req) {
    var b = command.createCashBooking(accountId, req);
    return Map.of("accountId", accountId, "bookingId", b.getBookingId());
  }

  // ------------------------
  // Outbox APIs (Debug/Support)
  // ------------------------

  @GetMapping("/accounts/{accountId}/outbox")
  public Map<String, Object> listOutbox(@PathVariable String accountId) {
    var events = outboxRepo.findTop200ByAggregateIdOrderByCreatedAtDesc(accountId);
    return Map.of("accountId", accountId, "events", events);
  }
}
