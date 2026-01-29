package de.sz.accounts.repo;

import de.sz.accounts.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
  List<Account> findByCustomerId(String customerId);
}
