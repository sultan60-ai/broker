package de.sz.accounts.repo;

import de.sz.accounts.domain.CashBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashBalanceRepository extends JpaRepository<CashBalance, String> {}
