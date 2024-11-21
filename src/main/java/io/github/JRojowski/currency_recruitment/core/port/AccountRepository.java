package io.github.JRojowski.currency_recruitment.core.port;

import io.github.JRojowski.currency_recruitment.core.domain.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account save(Account account);
    List<Account> findAllByUserId(UUID id);
    Optional<Account> findById(UUID id);
}
