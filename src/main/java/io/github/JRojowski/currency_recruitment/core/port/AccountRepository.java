package io.github.JRojowski.currency_recruitment.core.port;

import io.github.JRojowski.currency_recruitment.core.domain.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account save(Account entity);
    List<Account> findAllByBankUserId(UUID id);
    Optional<Account> findById(UUID id);
}
