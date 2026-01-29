    package de.sz.accounts.service;

    import de.sz.accounts.api.dto.*;
    import de.sz.accounts.domain.Account;
    import de.sz.accounts.repo.AccountRepository;
    import de.sz.accounts.repo.CashBalanceRepository;
    import de.sz.accounts.repo.PositionRepository;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class AccountQueryService {

      private final AccountRepository accountRepo;
  private final CashBalanceRepository cashRepo;
  private final PositionRepository posRepo;

      public AccountQueryService(AccountRepository accountRepo, CashBalanceRepository cashRepo, PositionRepository posRepo) {
        this.accountRepo = accountRepo;
    this.cashRepo = cashRepo;
    this.posRepo = posRepo;
      }

    public List<AccountDto> getAccountsByCustomer(String customerId) {
  return accountRepo.findByCustomerId(customerId).stream().map(this::toDto).toList();
}

public AccountDto getAccount(String accountId) {
  var a = accountRepo.findById(accountId)
      .orElseThrow(() -> new NotFoundException("Account not found: " + accountId));
  return toDto(a);
}

public CashBalanceDto getCashBalance(String accountId) {
  var cb = cashRepo.findById(accountId)
      .orElseThrow(() -> new NotFoundException("Cash balance not found for account: " + accountId));
  return new CashBalanceDto(cb.getAccountId(), cb.getCurrency(), cb.getBookBalance(),
      cb.getAvailableBalance(), cb.getBlockedAmount(), cb.getAsOf());
}

public List<PositionDto> getPositions(String accountId) {
  if (!accountRepo.existsById(accountId)) {
    throw new NotFoundException("Account not found: " + accountId);
  }
  return posRepo.findByAccountIdOrderByIsinAsc(accountId).stream()
      .map(p -> new PositionDto(p.getIsin(), p.getQuantityTotal(), p.getAvailableQty(),
          p.getBlockedQty(), p.getAvgCostAmount(), p.getAvgCostCcy(), p.getAsOf()))
      .toList();
}

private AccountDto toDto(Account a) {
  return new AccountDto(a.getAccountId(), a.getCustomerId(), a.getStatus(), a.getIban(),
      a.isTradingEnabled(), a.isPaymentsEnabled(), a.getCreatedAt(), a.getUpdatedAt());
}

    }
