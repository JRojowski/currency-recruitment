package io.github.JRojowski.currency_recruitment.infrastructure.adapter;

import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class InMemoryAccountRepository implements AccountRepository {

    private final HashMap<UUID, Account> hashMap = new HashMap<>();

    @Override
    public Account save(Account account) {
        hashMap.put(account.getId(), account);
        return account;
    }

    @Override
    public List<Account> findAllByBankUserId(UUID id) {
        return hashMap.values().stream().toList();
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(hashMap.getOrDefault(id, null));
    }
}
