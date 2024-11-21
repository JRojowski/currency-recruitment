package io.github.JRojowski.currency_recruitment.infrastructure.adapter;

import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


class InMemoryAccountRepository implements AccountRepository {
    @Override
    public Account save(Account account) {
        return null;
    }

    @Override
    public List<Account> findAllByUserId(UUID id) {
        return null;
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.empty();
    }
}
