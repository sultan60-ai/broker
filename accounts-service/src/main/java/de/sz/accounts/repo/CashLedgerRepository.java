package de.sz.accounts.repo;

import de.sz.accounts.domain.CashLedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CashLedgerRepository extends JpaRepository<CashLedgerEntry, String> {
  List<CashLedgerEntry> findTop200ByAccountIdOrderByBookingDateTimeDesc(String accountId);
}
